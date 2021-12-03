package com.demo.application.beans.collection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumMap;
import java.util.Map;

@Configuration
public class ConfigBeans {

    @Bean
    public Map<EnumBean, Strategy> beans(Strategy beanA, Strategy beanB) {
        EnumMap<EnumBean, Strategy> beans = new EnumMap<>(EnumBean.class);
        beans.put(EnumBean.BEAN_A, beanA);
        beans.put(EnumBean.BEAN_B, beanB);
        beans.put(EnumBean.BEAN_C, beanC());
        return beans;
    }

    @Bean
    public Strategy beanC() {
        return new BeanC("ES TIPO C");
    }

}
