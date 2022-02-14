package com.project.book.configuration.auth;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistration.Builder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import static org.springframework.security.oauth2.core.ClientAuthenticationMethod.CLIENT_SECRET_POST;

public enum CustomOAuth2Provider {

    KAKAO {
        @Override
        public ClientRegistration.Builder getBuilder(String registrationId) {
            ClientRegistration.Builder builder = getBuilder(registrationId, ClientAuthenticationMethod.CLIENT_SECRET_BASIC, DEFAULT_REDIRECT_URL);
            return getBuilder("kakao", ClientAuthenticationMethod.CLIENT_SECRET_POST, DEFAULT_REDIRECT_URL)
                    .scope("profile")
                    .authorizationUri("https://kauth.kakao.com/oauth/authorize")
                    .tokenUri("https://kauth.kakao.com/oauth/token")
                    .userInfoUri("https://kapi.kakao.com/v2/user/me")
                    .userNameAttributeName("id")
                    .clientName("Kakao");
        }
    };

    private static final String BASE_URL = "https://localhost:8080";
    private static final String DEFAULT_REDIRECT_URL = BASE_URL + "/login/oauth2/code/{registrationId}";

    protected final Builder getBuilder(String registrationId, ClientAuthenticationMethod method, String redirectUri) {
        Builder builder = ClientRegistration.withRegistrationId(registrationId);
        builder.clientAuthenticationMethod(method);
        builder.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE);
        builder.redirectUri(redirectUri);
        return builder;
    }

    public abstract Builder getBuilder(String registraionId);
}