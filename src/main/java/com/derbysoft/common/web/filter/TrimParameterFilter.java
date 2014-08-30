package com.derbysoft.common.web.filter;

import com.derbysoft.common.web.filter.support.TrimModel;
import com.derbysoft.common.web.filter.support.TrimParameterRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TrimParameterFilter extends OncePerRequestFilter {
    private TrimModel trimModel = TrimModel.TRIM;

    public void setTrimModel(TrimModel trimModel) {
        this.trimModel = trimModel;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        filterChain.doFilter(new TrimParameterRequest(request, trimModel), response);
    }
}
