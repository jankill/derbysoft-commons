package com.derbysoft.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.ClassUtils;

import java.io.*;
/**
 * @since 2009-5-25
 * @author zhupan
 * @version 1.0
 */
public abstract class FileUtils {

    private static Log logger = LogFactory.getLog(FileUtils.class);

    private static File getFile(String name) throws IOException {
        PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = pathMatchingResourcePatternResolver.getResources(name);
        if (resources != null && resources.length > 0) {
            return resources[0].getFile();
        }
        throw new FileNotFoundException(name);
    }

    public static File getFile(Class clazz, String fileName) {
        String packagePath = ClassUtils.getPackageName(clazz).replace(".", "/") + "/";
        String pathName = packagePath + fileName;
        try {
            return getFile(pathName);
        } catch (IOException e) {
            logger.error(e);
        }
        return null;
    }

    public static String getStringValue(Class clazz, String fileName) {
        try {
            BufferedReader bufReader = new BufferedReader(new FileReader(FileUtils.getFile(clazz, fileName)));
            StringBuilder value = new StringBuilder();
            String line = null;
            while ((line = bufReader.readLine()) != null) {
                value.append(line.trim());
            }
            return value.toString();
        } catch (Exception e) {
            logger.error(e);
        }
        return "";
    }
}
