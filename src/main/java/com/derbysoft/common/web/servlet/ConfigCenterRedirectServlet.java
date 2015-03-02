package com.derbysoft.common.web.servlet;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

public class ConfigCenterRedirectServlet extends RedirectServlet {
    private static final String CONFIGURATION_CENTER_PATH = "CCS-Path";
    private String redirectURL;

    @Override
    protected String getRedirectURL(HttpServletRequest httpServletRequest) {
        String path = httpServletRequest.getHeader(CONFIGURATION_CENTER_PATH);
        if (path == null) {
            path = httpServletRequest.getParameter(CONFIGURATION_CENTER_PATH);
        }
        return redirectURL + path;
    }

    @Override
    protected Set<String> getSkipHeaders() {
        Set<String> skipHeaders = super.getSkipHeaders();
        skipHeaders.add(CONFIGURATION_CENTER_PATH);
        return skipHeaders;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        try {
            Properties environmentMappings = PropertiesLoaderUtils.loadAllProperties("ccs.properties");
            if (environmentMappings == null) {
                throw new IllegalArgumentException("Loading environment.properties suffer error !!");
            }
            String ccsURL = environmentMappings.getProperty("ccs.url");
            if (StringUtils.isBlank(ccsURL)) {
                throw new IllegalStateException("Not found 'CCS URL' Properties !!");
            }
            this.redirectURL = ccsURL;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
