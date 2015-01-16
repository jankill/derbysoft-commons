package com.derbysoft.common.web.servlet;

import com.derbysoft.common.http.HttpClientUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AbstractConfigCenterServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response, String configCenterURL) throws ServletException, IOException {
        setContentType(response);
        String topicId = getTopicId(request);
        if (StringUtils.isBlank(topicId)) {
            response.getWriter().write("TopicId not found !!");
            return;
        }
        String url = configCenterURL + topicId + "/";
        response.getWriter().write(HttpClientUtils.getResult(url, request.getParameterMap()));
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response, String configCenterURL) throws ServletException, IOException {
        setContentType(response);
        StringBuilder url = new StringBuilder(configCenterURL);
        String topicId = getTopicId(request);
        if (StringUtils.isNotBlank(topicId)) {
            url.append(topicId).append("/");
        }
        String operationType = request.getParameter("operationType");
        if (StringUtils.isNotBlank(operationType)) {
            url.append(operationType);
        }
        response.getWriter().write(HttpClientUtils.getResult(url.toString(), request.getParameterMap()));
    }

    private String getTopicId(HttpServletRequest request) {
        return request.getParameter("topicId");
    }

    private void setContentType(HttpServletResponse response) {
        response.setContentType("application/json");
    }

}
