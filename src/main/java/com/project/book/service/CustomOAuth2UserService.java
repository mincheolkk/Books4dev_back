package com.project.book.service;

import com.project.book.configuration.jwt.JwtTokenProvider;
import com.project.book.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Iterator;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final JwtTokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    public CustomOAuth2UserService(JwtTokenProvider tokenProvider, MemberRepository memberRepository) {
        this.tokenProvider = tokenProvider;
        this.memberRepository = memberRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        DefaultOAuth2UserService defaultOAuth2UserService = new DefaultOAuth2UserService();

        // oAuth2User.getName() = 211912313112
        // oAuth2User.getAttributes() = {id=211912313112, connected_at=2022-02-14T03:33:17Z}
        // oAuth2User.getAuthorities() = [ROLE_USER, SCOPE_profile_nickname]
        OAuth2User oAuth2User = defaultOAuth2UserService.loadUser(userRequest);
        Iterator<? extends GrantedAuthority> iterator = (oAuth2User.getAuthorities()).iterator();
        System.out.println("iterator = " + iterator);
        System.out.println("iterator.toString() = " + iterator.toString());
        System.out.println("iterator.next().getAuthority() = " + iterator.next().getAuthority());


        // registraionId = kakao
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        System.out.println("registrationId = " + registrationId);

        // userNmaeAttributeName = id
        String userNameAttributeName = userRequest
                                            .getClientRegistration()
                                            .getProviderDetails()
                                            .getUserInfoEndpoint()
                                            .getUserNameAttributeName();
        System.out.println("userNameAttributeName = " + userNameAttributeName);

        System.out.println("3332342342423424234");

        return null;
    }


}
