package com.project.book.common.config.jwt;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Objects;

@Component
public class AuthorizationExtractor {

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER_TYPE = "Bearer";
    public static final String ACCESS_TOKEN_TYPE =
            AuthorizationExtractor.class.getSimpleName() + ".ACCESS_TOKEN_TYPE";


    public static String extract(HttpServletRequest request) {
        System.out.println("request = " + request.toString());
        Enumeration<String> headers = request.getHeaders(AUTHORIZATION);
        System.out.println("headers = " + headers);
        while (headers.hasMoreElements()) {
            String value = headers.nextElement();
            if ((value.toLowerCase().startsWith(BEARER_TYPE.toLowerCase()))) {
                String authHeaderValue = value.substring(BEARER_TYPE.length()).trim();
                request.setAttribute(ACCESS_TOKEN_TYPE,
                        value.substring(0, BEARER_TYPE.length()).trim());
                int commaIndex = authHeaderValue.indexOf(',');
                if (commaIndex > 0) {
                    authHeaderValue = authHeaderValue.substring(0, commaIndex);
                }
                System.out.println("authHeaderValue = " + authHeaderValue);
                return authHeaderValue;
            }
        }

        return Strings.EMPTY;
    }

//    private static final String AUTHORIZATION_HEADER = "Authorization";
//    private static final String TOKEN_TYPE = "Bearer";
//    private static final int TOKEN_TYPE_INDEX = 0;
//    private static final int TOKEN_INDEX = 1;
//    private static final int VALID_AUTHORIZATION_LENGTH = 2;
//
//    public String extract(final HttpServletRequest request) {
//        final String authorization = request.getHeader(AUTHORIZATION_HEADER);
//        if (Objects.isNull(authorization)) {
//            System.out.println("로그인 필요");
//        }
//        final String[] splitToken = authorization.split(" ");
//
//        if (splitToken.length != VALID_AUTHORIZATION_LENGTH || !splitToken[TOKEN_TYPE_INDEX].equals(TOKEN_TYPE)) {
//            System.out.println("유효하지 않은 토큰입니다.");
//        }
//        return splitToken[TOKEN_INDEX];
//    }
}
