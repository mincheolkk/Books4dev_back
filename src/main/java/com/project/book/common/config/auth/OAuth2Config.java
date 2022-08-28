package com.project.book.common.config.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

import java.util.Collections;


@Configuration
public class OAuth2Config {

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(
            @Value("${security.oauth2.registration.kakao.client-id}") String clientId,
            @Value("${security.oauth2.registration.kakao.client-secret}") String clientSecret
            ) {

        final ClientRegistration clientRegistration = CustomOAuth2Provider.KAKAO.getBuilder("kakao")
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();

        System.out.println("clientRegistration = " + clientRegistration);

        return new InMemoryClientRegistrationRepository(Collections.singletonList(
                clientRegistration
        ));
    }
}
