package com.demo.application.beans.collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class BeanA implements Strategy {
    private static final Logger log = LoggerFactory.getLogger(BeanA.class);

    @Override
    public void print() {
        log.info("BEAN A");
    }
}
