//package me.shinsunyoung.springbootdeveloper.config.jwt;
//
//import io.jsonwebtoken.Jwts;
//import me.shinsunyoung.springbootdeveloper.domain.User;
//import me.shinsunyoung.springbootdeveloper.repository.UserRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.time.Duration;
//import java.util.Date;
//import java.util.Map;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//class TokenProviderTest {
//
//    @Autowired
//    private TokenProvider tokenProvider; // 주입받을 TokenProvider 객체입니다.
//
//    @Autowired
//    private UserRepository userRepository; // 주입받을 UserRepository 객체입니다.
//
//    @Autowired
//    private JwtProperties jwtProperties; // 주입받을 JwtProperties 객체입니다.
//
//    @DisplayName("generateToken(): 유저 정보와 만료 기간을 전달해 토큰을 만들 수 있다.")
//    @Test
//    void generateToken() {
//        // given
//        User testUser = userRepository.save(User.builder()
//                .email("user@gmail.com")
//                .password("test")
//                .build());
//
//        // when
//        String token = tokenProvider.generateToken(testUser, Duration.ofDays(14)); // 특정 유저 정보와 14일 유효 기간을 가진 토큰 생성
//
//        // then
//        Long userId = Jwts.parser()
//                .setSigningKey(jwtProperties.getSecretKey())
//                .parseClaimsJws(token)
//                .getBody()
//                .get("id", Long.class); // 토큰에서 추출한 유저 ID
//
//        assertThat(userId).isEqualTo(testUser.getId()); // 생성된 토큰의 유저 ID가 예상과 일치하는지 검증
//    }
//
//    @DisplayName("validToken(): 만료된 토큰인 경우에 유효성 검증에 실패한다.")
//    @Test
//    void validToken_invalidToken() {
//        // given
//        String token = JwtFactory.builder()
//                .expiration(new Date(new Date().getTime() - Duration.ofDays(7).toMillis())) // 7일 전 시점으로 만료된 토큰 생성
//                .build()
//                .createToken(jwtProperties);
//
//        // when
//        boolean result = tokenProvider.validToken(token); // 토큰의 유효성 검증 수행
//
//        // then
//        assertThat(result).isFalse(); // 만료된 토큰이므로 유효성 검증 결과는 false 여야 함
//    }
//
//
//    @DisplayName("validToken(): 유효한 토큰인 경우에 유효성 검증에 성공한다.")
//    @Test
//    void validToken_validToken() {
//        // given
//        String token = JwtFactory.withDefaultValues()
//                .createToken(jwtProperties); // 기본값으로 유효한 토큰 생성
//
//        // when
//        boolean result = tokenProvider.validToken(token); // 토큰의 유효성 검증 수행
//
//        // then
//        assertThat(result).isTrue(); // 유효한 토큰이므로 유효성 검증 결과는 true 여야 함
//    }
//
//
//    @DisplayName("getAuthentication(): 토큰 기반으로 인증정보를 가져올 수 있다.")
//    @Test
//    void getAuthentication() {
//        // given
//        String userEmail = "user@email.com"; // 사용자 이메일 주소
//        String token = JwtFactory.builder()
//                .subject(userEmail) // 주제(subject)로 사용자 이메일 설정
//                .build()
//                .createToken(jwtProperties); // 주어진 이메일을 포함한 토큰 생성
//
//        // when
//        Authentication authentication = tokenProvider.getAuthentication(token); // 토큰을 기반으로 인증 정보 가져오기
//
//        // then
//        assertThat(((UserDetails) authentication.getPrincipal()).getUsername()).isEqualTo(userEmail); // 인증된 사용자의 이메일이 예상과 일치하는지 검증
//    }
//
//    @DisplayName("getUserId(): 토큰으로 유저 ID를 가져올 수 있다.")
//    @Test
//    void getUserId() {
//        // given
//        Long userId = 1L; // 특정 사용자 ID
//        String token = JwtFactory.builder()
//                .claims(Map.of("id", userId)) // 클레임으로 주어진 사용자 ID를 포함한 토큰 생성
//                .build()
//                .createToken(jwtProperties);
//
//        // when
//        Long userIdByToken = tokenProvider.getUserId(token); // 토큰으로부터 추출한 사용자 ID 가져오기
//
//        // then
//        assertThat(userIdByToken).isEqualTo(userId); // 추출된 사용자 ID가 예상과 일치하는지 검증
//    }
//}
