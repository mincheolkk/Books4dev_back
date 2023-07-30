package com.project.book.support;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.book.book.controller.BookController;
import com.project.book.book.service.BookService;
import com.project.book.book.service.RankingService;
import com.project.book.book.service.ReadBookService;
import com.project.book.book.service.WishBookService;
import com.project.book.common.config.QuerydslConfiguration;
import com.project.book.common.config.jwt.JwtTokenProvider;
import com.project.book.member.controller.MemberController;
import com.project.book.member.service.AuthService;
import com.project.book.member.service.CustomOAuth2UserService;
import com.project.book.member.service.MemberService;
import com.project.book.member.service.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;


@AutoConfigureDataJpa
@WebMvcTest(
        controllers = {
                BookController.class,
                MemberController.class
        }
)
@Import({QuerydslConfiguration.class})
@ComponentScan(basePackages = {"com.project.book.common.config.auth", "com.project.book.common.config.jwt"})
public abstract class ControllerTest {

    @BeforeEach
    void setUp(final WebApplicationContext context) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .alwaysDo(MockMvcResultHandlers.print())
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected BookService bookService;

    @MockBean
    protected TokenService tokenService;

    @MockBean
    protected AuthService authService;

    @MockBean
    protected MemberService memberService;

    @MockBean
    protected WishBookService wishBookService;

    @MockBean
    protected RankingService rankingService;

    @MockBean
    protected CustomOAuth2UserService customOAuth2UserService;

    @MockBean
    protected ReadBookService readBookService;

    @MockBean
    protected JwtTokenProvider jwtTokenProvider;
}
