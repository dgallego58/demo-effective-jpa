package com.demo.application.beans.collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BeanB implements Strategy {
    private static final Logger log = LoggerFactory.getLogger(BeanB.class);

    @Override
    public void print() {
        log.info("BEAN B");
    }
}
