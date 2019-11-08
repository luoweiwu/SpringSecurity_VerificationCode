package com.Gary.GaryRESTful.properties;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
//让我们的配置生效
@EnableConfigurationProperties(GarySecurityProperties.class)
public class GarySecurityConfig {

}
