package com.project.book.configuration;


import com.project.book.configuration.properties.KakaoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableConfigurationProperties({
        KakaoProperties.class
})
@PropertySource({
        "classpath:kakao.properties"
})
public class PropertiesConfiguration {
}
