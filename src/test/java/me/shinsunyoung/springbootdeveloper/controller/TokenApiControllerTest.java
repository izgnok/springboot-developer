package me.shinsunyoung.springbootdeveloper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.shinsunyoung.springbootdeveloper.config.jwt.JwtFactory;
import me.shinsunyoung.springbootdeveloper.config.jwt.JwtProperties;
import me.shinsunyoung.springbootdeveloper.domain.RefreshToken;
import me.shinsunyoung.springbootdeveloper.domain.User;
import me.shinsunyoung.springbootdeveloper.dto.CreateAccessTokenRequest;
import me.shinsunyoung.springbootdeveloper.repository.RefreshTokenRepository;
import me.shinsunyoung.springbootdeveloper.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * TokenApiController 테스트 클래스입니다.
 */
@SpringBootTest
@AutoConfigureMockMvc
class TokenApiControllerTest {

    @Autowired
    protected MockMvc mockMvc; // MockMvc 객체를 주입받습니다.

    @Autowired
    protected ObjectMapper objectMapper; // ObjectMapper 객체를 주입받습니다.

    @Autowired
    private WebApplicationContext context; // 웹 애플리케이션 컨텍스트 객체를 주입받습니다.

    @Autowired
    JwtProperties jwtProperties; // JwtProperties 객체를 주입받습니다.

    @Autowired
    UserRepository userRepository; // UserRepository 객체를 주입받습니다.

    @Autowired
    RefreshTokenRepository refreshTokenRepository; // RefreshTokenRepository 객체를 주입받습니다.

    /**
     * 각 테스트 메서드 실행 전에 MockMvc 설정을 초기화합니다.
     */
    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        userRepository.deleteAll(); // 모든 사용자 데이터를 삭제하여 테스트 초기 상태를 설정합니다.
    }

    /**
     * 새로운 액세스 토큰을 발급하는 테스트 메서드입니다.
     *
     * @throws Exception 예외가 발생할 수 있습니다.
     */
    @DisplayName("createNewAccessToken: 새로운 액세스 토큰을 발급한다.")
    @Test
    public void createNewAccessToken() throws Exception {
        // given
        final String url = "/tokenapi/token";

        // 테스트용 사용자 데이터를 생성하고 저장합니다.
        User testUser = userRepository.save(User.builder()
                .email("user@gmail.com")
                .password("test")
                .build());

        // JWT 리프레시 토큰을 생성합니다.
        String refreshToken = JwtFactory.builder()
                .claims(Map.of("id", testUser.getId()))
                .build()
                .createToken(jwtProperties);

        // 생성한 리프레시 토큰을 저장합니다.
        refreshTokenRepository.save(new RefreshToken(testUser.getId(), refreshToken));

        // 액세스 토큰 생성 요청 객체를 생성합니다.
        CreateAccessTokenRequest request = new CreateAccessTokenRequest();
        request.setRefreshToken(refreshToken);
        final String requestBody = objectMapper.writeValueAsString(request);

        // when
        // MockMvc를 사용하여 POST 요청을 수행합니다.
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // then
        // 응답 상태와 응답 본문을 검증합니다.
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }

}
