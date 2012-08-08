package com.derbysoft.common.util.concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.derbysoft.common.domain.ValueObject;

public abstract class ExecutorUtils {
	
	private static Log logger = LogFactory.getLog(ExecutorUtils.class);

	private static final long SHUT_DOWN_TIMEOUT = 1000L;

	public static <T> List<T> batchExecute(Executor executor, Collection<Callable<T>> tasks) {
		return batchExecute(executor, tasks, false, false, false);
	}

	public static <T> List<T> batchExecute(Executor executor, Collection<Callable<T>> tasks, long singleTaskTimeout) {
		CompletionService<SequencedObject<T>> service = new ExecutorCompletionService<SequencedObject<T>>(executor);
		List<Future<SequencedObject<T>>> futures = submitSequencedTasks(tasks, service);
		int taskSize = tasks.size();
		List<SequencedObject<T>> unsequencedResults = new ArrayList<SequencedObject<T>>(taskSize);
		
		StopWatch sw = new StopWatch();
		sw.start();
		
		for (int i = 0; i < taskSize; i++) {
			try {
				long remainingTime = singleTaskTimeout - sw.getTime();
				if (remainingTime < 0) {
					remainingTime = 0;
				}
				Future<SequencedObject<T>> future = service.poll(remainingTime, TimeUnit.MILLISECONDS);
				if (future == null) {
					if (logger.isWarnEnabled()) {
						logger.warn("Timout occoured in task execution, a null result will be added to results");
					}
					unsequencedResults.add(null);
				} else {
					futures.remove(future);
					unsequencedResults.add(future.get());
				}
			} catch (Throwable e) {
				if (logger.isErrorEnabled()) {
					logger.error("Exception occured during task execution, a null result will be added to results", e);
				}
				unsequencedResults.add(null);
			}
		}
		
		sw.stop();
		cancelTimeouted(futures);
		return collect(resequence(unsequencedResults));
	}

	private static <T> List<Future<SequencedObject<T>>> submitSequencedTasks(
		Collection<Callable<T>> tasks, CompletionService<SequencedObject<T>> service) {
		
		final AtomicInteger sequence = new AtomicInteger(-1);
		List<Future<SequencedObject<T>>> futures = new ArrayList<Future<SequencedObject<T>>>();
		
		for (final Callable<T> task : tasks) {
			final int sequenceNumber = sequence.incrementAndGet();
			
			Future<SequencedObject<T>> future = service.submit(new Callable<SequencedObject<T>>() {
				@Override
				public SequencedObject<T> call() throws Exception {
					T result = task.call();
					return new SequencedObject<T>(sequenceNumber, result);
				}
			});
			
			futures.add(future);
		}
		
		return futures;
	}
	
	private static <T> void cancelTimeouted(List<Future<SequencedObject<T>>> futures) {
		for (Future<SequencedObject<T>> future : futures) {
			future.cancel(false);
		}
	}
	
	@SuppressWarnings("unchecked")
	private static <T> List<SequencedObject<T>> resequence(List<SequencedObject<T>> unsequencedResults) {
		int size = unsequencedResults.size();
		List<SequencedObject<T>> resequenced = new ArrayList<SequencedObject<T>>(size);
		
		for (int index = 0; index < size; index++) {
			final int sequenceNumber = index;
			
			SequencedObject<T> found = (SequencedObject<T>) CollectionUtils.find(unsequencedResults, new Predicate() {
				@Override
				public boolean evaluate(Object object) {
					if (object == null) {
						return false;
					}
					return ((SequencedObject) object).getSequenceNumber() == sequenceNumber;
				}
			});
			
			resequenced.add(found);
			unsequencedResults.remove(found);
		}
		
		return resequenced;
	}
	
	@SuppressWarnings("unchecked")
	private static <T> List<T> collect(List<SequencedObject<T>> sequenced) {
		return (List<T>) CollectionUtils.collect(sequenced, new Transformer() {
			@Override
			public Object transform(Object input) {
				if (input == null) {
					return null;
				}
				return ((SequencedObject) input).getResult();
			}
		});
	}
	
	
	private static final class SequencedObject<T> extends ValueObject {

		private static final long serialVersionUID = 7756056501654415367L;
		private final int sequenceNumber;
		private final T result;

		public SequencedObject(int sequenceNumber, T result) {
			this.sequenceNumber = sequenceNumber;
			this.result = result;
		}

		public T getResult() {
			return result;
		}

		public int getSequenceNumber() {
			return sequenceNumber;
		}
	}
	
	public static <T> List<T> batchExecute(
		Executor executor, 
		Collection<Callable<T>> tasks, 
		boolean eliminateNullResult,
		boolean ignoreInterruptedException, 
		boolean ignoreExecutionException) {
	
		Collection<Future<T>> futures = submitTasks(executor, tasks);
		List<T> results = new ArrayList<T>(tasks.size());
		
		for (Future<T> future : futures) {
			try {
				T result = future.get();
				if (eliminateNullResult && result == null) {
					continue;
				}
				results.add(result);
			} catch (InterruptedException e) {
				if (ignoreInterruptedException) {
					if (logger.isErrorEnabled()) {
						logger.error(e.getMessage(), e);
					}
				} else {
					Thread.currentThread().interrupt();
				}
			} catch (ExecutionException e) {
				if (ignoreExecutionException) {
					if (logger.isErrorEnabled()) {
						logger.error(e.getMessage(), e);
					}
				} else {
					throw new RuntimeException("Execution Exception [" + e.getMessage() + "]", e);
				}
			}
		}
		
		return results;
	}	
	
	
	private static <T> List<Future<T>> submitTasks(Executor executor, Collection<Callable<T>> tasks) {
		CompletionService<T> service = new ExecutorCompletionService<T>(executor);
		List<Future<T>> futures = new ArrayList<Future<T>>(tasks.size());
		
		for (Callable<T> task : tasks) {
			Future<T> future = service.submit(task);
			futures.add(future);
		}
		
		return futures;
	}
	
	public static List<Runnable> awaitShutdownNow(ExecutorService executorService) throws InterruptedException {
		List<Runnable> neverCommenced = executorService.shutdownNow();		
		while (!executorService.awaitTermination(SHUT_DOWN_TIMEOUT, TimeUnit.MILLISECONDS)) {
			logger.debug("Await for " + executorService + " termination ...");
		}
		return neverCommenced;
	}


	
	
}
