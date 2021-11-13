package com.demo.infrastructure.port.input.log;

import com.demo.infrastructure.helper.JacksonUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Aspect
public class MonitorEditorialController {

    private static final Logger log = LoggerFactory.getLogger(MonitorEditorialController.class);

    @Pointcut("execution(* com.demo.infrastructure.port.input.controller.swagger.EditorialSwagger+.*(..))")
    public void executeOnEveryUseCaseOfEditorial() { /* aspect */ }

    @Around(value = "executeOnEveryUseCaseOfEditorial()")
    public Object aroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
        String jsonReq = JacksonUtil.STRINGIFY.apply(pjp.getArgs());
        log.info("Request: {}", jsonReq);
        Object proceed = pjp.proceed();
        String jsonResp = JacksonUtil.STRINGIFY.apply(proceed);
        log.info("Response: {}", jsonResp);
        return proceed;
    }
}
