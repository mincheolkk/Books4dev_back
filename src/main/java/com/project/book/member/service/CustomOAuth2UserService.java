package com.project.book.member.service;

import com.project.book.common.config.jwt.JwtTokenProvider;
import com.project.book.member.domain.Member;
import com.project.book.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final JwtTokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    public CustomOAuth2UserService(JwtTokenProvider tokenProvider, MemberRepository memberRepository) {
        this.tokenProvider = tokenProvider;
        this.memberRepository = memberRepository;
    }

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        DefaultOAuth2UserService defaultOAuth2UserService = new DefaultOAuth2UserService();

        // oAuth2User.getName() = 21191231***
        // oAuth2User.getAttributes() = {id=211912313***, connected_at=2022-02-14T03:33:17Z}
//        oAuth2User.getAttributes() = {id=2119434157, connected_at=2022-02-14T03:33:17Z, properties={nickname=김민철}, kakao_account={profile_nickname_needs_agreement=false, profile={nickname=김민철}, has_email=true, email_needs_agreement=true}}

        OAuth2User oAuth2User = defaultOAuth2UserService.loadUser(userRequest); //kakao 에서 가저온 유저 정보를 담고 있음
        Iterator<? extends GrantedAuthority> iterator = (oAuth2User.getAuthorities()).iterator();
        System.out.println("oAuth2User.getAuthorities() = " + oAuth2User.getAuthorities());
        System.out.println("oAuth2User..getAttributes() = " + oAuth2User.getAttributes());

        // 6.2일 : userEndPoint 출력해보기
        ClientRegistration.ProviderDetails.UserInfoEndpoint userInfoEndpoint = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint();
        System.out.println("userInfoEndpoint = " + userInfoEndpoint);

        // registraionId = kakao
        String registrationId = userRequest
                                    .getClientRegistration()
                                    .getRegistrationId();  // oauth 서비스 이름, 내꺼에선 kakao만 나옴
        System.out.println("registrationId = " + registrationId);
        // userNmaeAttributeName = id
        String userNameAttributeName = userRequest
                                            .getClientRegistration()
                                            .getProviderDetails()
                                            .getUserInfoEndpoint()
                                            .getUserNameAttributeName();  //oauth 로그인 시 pk 가 되는 값
        System.out.println("userNameAttributeName = " + userNameAttributeName);

        String kakaoId = oAuth2User.getName();

        Map<String, Object> attributes = oAuth2User.getAttributes(); // oauth 서비스의 유저 정보들
        System.out.println("attributes = " + attributes);

        System.out.println("kakaoId = " + kakaoId);
        String role = iterator.next().getAuthority();
        System.out.println("role = " + role);

        System.out.println("in customOAuth2UserService");
        String id = String.valueOf(oAuth2User.getAttributes().get("id"));
        System.out.println("id = " + id);

        if (!memberRepository.existsByoAuth(id)) {
//            String nickname = (String)
//                    ((LinkedHashMap)
//                            ((LinkedHashMap) oAuth2User.getAttribute("kakao_account"))
//                                    .get("profile"))
//                            .get("nickname");
            // 여기서 개발자 포지션 고르라는 창으로 보내야할듯


            Member newMember = Member.builder()
//                    .nickname(nickname)
                    .oAuth(id)
                    .build();

            Member saved = memberRepository.save(newMember);

            System.out.println("in customOAuth2UserService -- no MemberRepo");
//            System.out.println("saved = " + saved.getNickname());
            System.out.println("saved.getId() = " + saved.getId());
            Optional<Member> byId = memberRepository.findById(1L);
            System.out.println("byId = " + byId);

        }
        return oAuth2User;
    }
}
