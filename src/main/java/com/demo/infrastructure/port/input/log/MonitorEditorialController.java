package com.demo.infrastructure.port.input.log;

import com.demo.infrastructure.helper.JacksonUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


@Aspect
public class MonitorEditorialController {

    private static final Logger log = LoggerFactory.getLogger(MonitorEditorialController.class);
    private MutableObject mutableObject;

    @Pointcut("execution(* com.demo.infrastructure.port.input.controller.swagger.EditorialSwagger+.*(..))")
    public void executeOnEveryUseCaseOfEditorial() { /* aspect */ }

    @Around(value = "executeOnEveryUseCaseOfEditorial()")
    public Object aroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
        mutableObject = new MutableObject();
        String jsonReq = JacksonUtil.STRINGIFY.apply(pjp.getArgs());
        log.info("AROUND Request: {}", jsonReq);
        Object proceed = pjp.proceed();
        String jsonResp = JacksonUtil.STRINGIFY.apply(proceed);
        log.info("AROUND Response: {}", jsonResp);
        return proceed;
    }


    @Before(value = "executeOnEveryUseCaseOfEditorial()")
    public void before(JoinPoint jp) {
        log.info("START BEFORE");
        var requestHeaders = requestHeaders(HttpDelegate.getCurrentHttpRequest());
        mutableObject.setReqHeaders(requestHeaders);
        mutableObject.setBeforeAttr(jp.getArgs());
        log.info("END BEFORE");
    }

    @AfterReturning(pointcut = "executeOnEveryUseCaseOfEditorial()", returning = "retVal")
    public void afterReturning(Object retVal) {
        log.info("START AFTER RETURNING");
        mutableObject.setAfterAtt(retVal);
        var respHeaders = responseHeaders(HttpDelegate.getCurrentResponse());
        mutableObject.setResponseHeaders(respHeaders);
        log.info("END AFTER RETURNING");
    }

    @After(value = "executeOnEveryUseCaseOfEditorial()")
    public void release() {
        log.info("START AFTER");
        var jsonComplete = JacksonUtil.STRINGIFY.apply(this.mutableObject);
        log.info("END AFTER resource {}", jsonComplete);
    }


    public Map<String, String> requestHeaders(HttpServletRequest httpServletRequest) {
        var headers = new HashMap<String, String>();
        Enumeration<String> names = httpServletRequest.getHeaderNames();
        while (names.hasMoreElements()) {
            String headerName = names.nextElement();
            String headerValue = httpServletRequest.getHeader(headerName);
            headers.put(headerName, headerValue);
        }
        return headers;
    }

    public Map<String, String> responseHeaders(HttpServletResponse response) {
        Map<String, String> map = new HashMap<>();
        Collection<String> headerNames = response.getHeaderNames();
        for (String header : headerNames) {
            map.put(header, response.getHeader(header));
        }
        return map;
    }

}
