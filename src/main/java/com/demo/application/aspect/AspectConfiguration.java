package com.demo.application.aspect;

import com.demo.infrastructure.port.input.log.MonitorEditorialController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@Configuration
public class AspectConfiguration {

    @Bean
    public MonitorEditorialController monitorEditorialController(){
        return new MonitorEditorialController();
    }

}
