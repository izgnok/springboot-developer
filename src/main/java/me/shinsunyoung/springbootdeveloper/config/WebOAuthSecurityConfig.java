package me.shinsunyoung.springbootdeveloper.config;

import lombok.RequiredArgsConstructor;
import me.shinsunyoung.springbootdeveloper.config.jwt.TokenProvider;
import me.shinsunyoung.springbootdeveloper.config.oauth.OAuth2AuthorizationRequestBasedOnCookieRepository;
import me.shinsunyoung.springbootdeveloper.config.oauth.OAuth2SuccessHandler;
import me.shinsunyoung.springbootdeveloper.config.oauth.OAuth2UserCustomService;
import me.shinsunyoung.springbootdeveloper.repository.RefreshTokenRepository;
import me.shinsunyoung.springbootdeveloper.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

/**
 * 웹 보안 관련 설정 클래스입니다.
 */
@RequiredArgsConstructor
@Configuration
public class WebOAuthSecurityConfig {

    private final OAuth2UserCustomService oAuth2UserCustomService;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    /**
     * 웹 보안을 설정하기 위한 커스터마이저를 빈으로 등록합니다.
     *
     * @return WebSecurityCustomizer 객체
     */
    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers(
                        new AntPathRequestMatcher("/static/img/**"),
                        new AntPathRequestMatcher("/css/**"),
                        new AntPathRequestMatcher("/js/**")
                ); // 정적 리소스 접근을 무시합니다.
    }

    /**
     * 보안 필터 체인을 설정합니다.
     *
     * @param http HttpSecurity 객체
     * @return SecurityFilterChain 객체
     * @throws Exception 예외 처리
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // CSRF 보호 비활성화
                .httpBasic(AbstractHttpConfigurer::disable) // 기본 HTTP 인증 비활성화
                .formLogin(AbstractHttpConfigurer::disable) // 폼 기반 로그인 비활성화
                .logout(AbstractHttpConfigurer::disable) // 로그아웃 설정 비활성화
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 관리 정책 설정
                .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class) // 토큰 인증 필터 추가
                .authorizeRequests(auth -> auth
                        .requestMatchers(new AntPathRequestMatcher("/tokenapi/token")).permitAll() // /api/token 요청은 인증 없이 허용
                        .requestMatchers(new AntPathRequestMatcher("/blogapi/**")).authenticated() // /api/** 요청은 인증이 필요함
                        .anyRequest().permitAll()) // 그 외의 요청은 모두 허용
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login") // 로그인 페이지 설정
                        .authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint.authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())) // OAuth2 인증 요청 리포지토리 설정
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.userService(oAuth2UserCustomService)) // OAuth2 사용자 정보 엔드포인트 설정
                        .successHandler(oAuth2SuccessHandler()) // OAuth2 로그인 성공 핸들러 설정
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .defaultAuthenticationEntryPointFor(
                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED), // 인증되지 않은 사용자에게 401 UNAUTHORIZED 응답 설정
                                new AntPathRequestMatcher("/blogapi/**") // /api/** 패턴에 대해서만 적용
                        ))
                .build();
    }

    /**
     * OAuth2 로그인 성공 핸들러를 빈으로 등록합니다.
     *
     * @return OAuth2SuccessHandler 객체
     */
    @Bean
    public OAuth2SuccessHandler oAuth2SuccessHandler() {
        return new OAuth2SuccessHandler(tokenProvider,
                refreshTokenRepository,
                oAuth2AuthorizationRequestBasedOnCookieRepository(),
                userService
        );
    }

    /**
     * 토큰 인증 필터를 빈으로 등록합니다.
     *
     * @return TokenAuthenticationFilter 객체
     */
    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider);
    }

    /**
     * OAuth2 인증 요청을 쿠키 기반으로 처리하는 리포지토리를 빈으로 등록합니다.
     *
     * @return OAuth2AuthorizationRequestBasedOnCookieRepository 객체
     */
    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    /**
     * BCryptPasswordEncoder를 빈으로 등록합니다.
     *
     * @return BCryptPasswordEncoder 객체
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
