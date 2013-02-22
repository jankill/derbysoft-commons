package com.derbysoft.common.hibernate;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class HibernateInterceptor implements MethodInterceptor {

    protected final Log logger = LogFactory.getLog(getClass());

    private boolean exceptionConversionEnabled = true;

    public static final int FLUSH_NEVER = 0;

    public static final int FLUSH_AUTO = 1;

    public static final int FLUSH_EAGER = 2;

    public static final int FLUSH_COMMIT = 3;

    public static final int FLUSH_ALWAYS = 4;

    private int flushMode = FLUSH_AUTO;

    private SessionFactory sessionFactory;

    public int getFlushMode() {
        return flushMode;
    }

    public void setFlushMode(int flushMode) {
        this.flushMode = flushMode;
    }


    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public SessionFactory getSessionFactory() {
        return this.sessionFactory;
    }

    public void setExceptionConversionEnabled(boolean exceptionConversionEnabled) {
        this.exceptionConversionEnabled = exceptionConversionEnabled;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager.getResource(getSessionFactory());
        boolean existingTransaction = sessionHolder != null;
        Session session;
        if (existingTransaction) {
            logger.debug("Found thread-bound Session for HibernateInterceptor");
            session = sessionHolder.getSession();
        } else {
            session = openSession();
            TransactionSynchronizationManager.bindResource(getSessionFactory(), new SessionHolder(session));
        }
        FlushMode previousFlushMode = null;
        try {
            previousFlushMode = applyFlushMode(session, existingTransaction);
            Object retVal = methodInvocation.proceed();
            flushIfNecessary(session, existingTransaction);
            return retVal;
        } catch (HibernateException ex) {
            if (this.exceptionConversionEnabled) {
                throw SessionFactoryUtils.convertHibernateAccessException(ex);
            }
            throw ex;
        } finally {
            if (existingTransaction) {
                logger.debug("Not closing pre-bound Hibernate Session after HibernateInterceptor");
                if (previousFlushMode != null) {
                    session.setFlushMode(previousFlushMode);
                }
            } else {
                SessionFactoryUtils.closeSession(session);
                if (sessionHolder == null) {
                    TransactionSynchronizationManager.unbindResource(getSessionFactory());
                }
            }
        }
    }

    protected void flushIfNecessary(Session session, boolean existingTransaction) {
        if (getFlushMode() == FLUSH_EAGER || (!existingTransaction && getFlushMode() != FLUSH_NEVER)) {
            logger.debug("Eagerly flushing Hibernate session");
            session.flush();
        }
    }

    protected Session openSession() {
        try {
            Session session = getSessionFactory().openSession();
            session.setFlushMode(FlushMode.MANUAL);
            return session;
        } catch (HibernateException ex) {
            throw new DataAccessResourceFailureException("Could not open Hibernate Session", ex);
        }
    }

    protected FlushMode applyFlushMode(Session session, boolean existingTransaction) {
        if (getFlushMode() == FLUSH_NEVER) {
            if (existingTransaction) {
                FlushMode previousFlushMode = session.getFlushMode();
                if (!previousFlushMode.lessThan(FlushMode.COMMIT)) {
                    session.setFlushMode(FlushMode.MANUAL);
                    return previousFlushMode;
                }
            } else {
                session.setFlushMode(FlushMode.MANUAL);
            }
        } else if (getFlushMode() == FLUSH_EAGER) {
            if (existingTransaction) {
                FlushMode previousFlushMode = session.getFlushMode();
                if (!previousFlushMode.equals(FlushMode.AUTO)) {
                    session.setFlushMode(FlushMode.AUTO);
                    return previousFlushMode;
                }
            }
        } else if (getFlushMode() == FLUSH_COMMIT) {
            if (existingTransaction) {
                FlushMode previousFlushMode = session.getFlushMode();
                if (previousFlushMode.equals(FlushMode.AUTO) || previousFlushMode.equals(FlushMode.ALWAYS)) {
                    session.setFlushMode(FlushMode.COMMIT);
                    return previousFlushMode;
                }
            } else {
                session.setFlushMode(FlushMode.COMMIT);
            }
        } else if (getFlushMode() == FLUSH_ALWAYS) {
            if (existingTransaction) {
                FlushMode previousFlushMode = session.getFlushMode();
                if (!previousFlushMode.equals(FlushMode.ALWAYS)) {
                    session.setFlushMode(FlushMode.ALWAYS);
                    return previousFlushMode;
                }
            } else {
                session.setFlushMode(FlushMode.ALWAYS);
            }
        }
        return null;
    }

}