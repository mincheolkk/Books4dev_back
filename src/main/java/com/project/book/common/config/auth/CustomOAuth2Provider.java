package com.project.book.common.config.auth;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistration.Builder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

public enum CustomOAuth2Provider {

    KAKAO {
        @Override
        public ClientRegistration.Builder getBuilder(String registrationId) {
            return getBuilder("kakao", ClientAuthenticationMethod.CLIENT_SECRET_POST, DEFAULT_REDIRECT_URL)
                    .authorizationUri("https://kauth.kakao.com/oauth/authorize")
                    .tokenUri("https://kauth.kakao.com/oauth/token")
                    .userInfoUri("https://kapi.kakao.com/v2/user/me")
                    .userNameAttributeName("id")
                    .clientName("kakao");
        }
    };

    private static final String BASE_URL = "http://localhost:8080";
    private static final String DEFAULT_REDIRECT_URL = BASE_URL + "/login/oauth2/code/kakao";

    protected final Builder getBuilder(String registrationId, ClientAuthenticationMethod method, String redirectUri) {
        Builder builder = ClientRegistration.withRegistrationId(registrationId);
        builder.clientAuthenticationMethod(method);
        builder.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE);
        builder.redirectUri(redirectUri);
        return builder;
    }

    public abstract Builder getBuilder(String registraionId);
}
