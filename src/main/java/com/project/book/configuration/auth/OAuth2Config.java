package com.project.book.configuration.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

import java.util.Collections;

import static com.project.book.configuration.auth.CustomOAuth2Provider.KAKAO;

@Configuration
public class OAuth2Config {
//
//    @Value("${security.oauth2.registration.kakao.client-id}")
//    private static String clientId;
//
//    @Value("${security.oauth2.registration.kakao.client-secret}")
//    private static String clientSecret;


    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(
            @Value("${security.oauth2.registration.kakao.client-id}") String clientId,
            @Value("${security.oauth2.registration.kakao.client-secret}") String clientSecret
            ) {

        System.out.println("clientId = " + clientId);
        System.out.println("clientSecret = " + clientSecret);

        final ClientRegistration clientRegistration = CustomOAuth2Provider.KAKAO.getBuilder("kakao")
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();

        return new InMemoryClientRegistrationRepository(Collections.singletonList(
                clientRegistration
        ));
    }
}
