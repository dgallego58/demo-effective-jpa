package com.demo.infrastructure.port.input.log;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class HttpDelegate {

    private HttpDelegate() {
        //final
    }

    public static HttpServletRequest getCurrentHttpRequest() {
        HttpServletRequest request = null;
        var reqAttrib = RequestContextHolder.getRequestAttributes();
        if (reqAttrib instanceof ServletRequestAttributes) {
            request = ((ServletRequestAttributes) reqAttrib).getRequest();
        }
        return request;
    }

    public static HttpServletResponse getCurrentResponse() {
        HttpServletResponse response;
        ServletRequestAttributes reqAttrib = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        response = reqAttrib.getResponse();
        return response;
    }
}
