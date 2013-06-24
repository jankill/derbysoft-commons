package com.derbysoft.common.web.listener;

import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

/**
 * @author zhupan
 * @version 1.5
 */
public class WebContextLoaderListener extends ContextLoaderListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        super.contextInitialized(event);
        ServletContext application = event.getServletContext();
        application.setAttribute("contextPath", application.getContextPath());

    }
}