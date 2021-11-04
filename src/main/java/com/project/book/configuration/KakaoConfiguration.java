package com.project.book.configuration;

import com.project.book.configuration.properties.KakaoProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
@RequiredArgsConstructor
public class KakaoConfiguration {

    private final KakaoProperties properties;

    @Bean
    public WebClient kakaoWebClient() {
        return WebClient.builder().baseUrl("https://dapi.kakao.com")
                .defaultHeader("Authorization", "KakaoAK " + properties.getRestapi()).build();
    }
}
