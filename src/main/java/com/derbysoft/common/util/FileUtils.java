package com.derbysoft.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.ClassUtils;

import java.io.*;

/**
 * @author zhupan
 * @version 1.0
 * @since 2009-5-25
 */
public abstract class FileUtils {

    private static Log logger = LogFactory.getLog(FileUtils.class);

    private static File getFile(String name) {
        try {
            PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = pathMatchingResourcePatternResolver.getResources(name);
            if (resources != null && resources.length > 0) {
                return resources[0].getFile();
            }
        } catch (IOException e) {
            throw new FileRuntimeException("FileUtils getFile error", e);
        }
        throw new FileRuntimeException("FileUtils getFile error");
    }

    public static File getFile(Class clazz, String fileName) {
        String packagePath = ClassUtils.getPackageName(clazz).replace(".", "/") + "/";
        String pathName = packagePath + fileName;
        return getFile(pathName);
    }

    public static String getStringValue(Class clazz, String fileName) {
        try {
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(new FileInputStream(getFile(clazz, fileName)), "UTF-8"));
            StringBuilder value = new StringBuilder();
            String line = null;
            while ((line = bufReader.readLine()) != null) {
                value.append(line.trim());
            }
            bufReader.close();
            return value.toString();
        } catch (Exception e) {
            logger.error("FileUtils getStringValue error", e);
            return "";
        }
    }
}