package com.derbysoft.common.web.listener;

import com.derbysoft.common.factory.ServiceFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author zhupan
 * @version 1.5
 */
public class InitServiceFactoryListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServiceFactory.setContext(WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext()));
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
    }
}