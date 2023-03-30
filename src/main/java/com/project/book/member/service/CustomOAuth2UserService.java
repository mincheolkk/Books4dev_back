package com.project.book.member.service;

import com.project.book.member.domain.Member;
import com.project.book.member.domain.Nickname;
import com.project.book.member.repository.MemberRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    public CustomOAuth2UserService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    @Override
    public OAuth2User loadUser(final OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        DefaultOAuth2UserService defaultOAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = defaultOAuth2UserService.loadUser(userRequest); //kakao 에서 가저온 유저 정보를 담고 있음

        String id = String.valueOf(oAuth2User.getAttributes().get("id"));

        if (!memberRepository.existsByoAuth(id)) {
            Nickname nickname = new Nickname(id);
            Member newMember = Member.builder()
                    .oAuth(id)
                    .nickname(nickname)
                    .build();

            memberRepository.save(newMember);
        }

        return oAuth2User;
    }
}
