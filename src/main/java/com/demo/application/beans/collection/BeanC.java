package com.demo.application.beans.collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeanC implements Strategy {

    private static final Logger log = LoggerFactory.getLogger(BeanC.class);
    private final String myBean;

    public BeanC(String myBean) {
        this.myBean = myBean;
    }

    @Override
    public void print() {
        log.info(myBean);
    }
}
