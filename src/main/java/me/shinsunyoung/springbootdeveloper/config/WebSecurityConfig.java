//package me.shinsunyoung.springbootdeveloper.config;
//
//import lombok.RequiredArgsConstructor;
//import me.shinsunyoung.springbootdeveloper.service.UserDetailService;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.ProviderManager;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//@Configuration // 스프링 설정 클래스임을 나타내는 애노테이션
//@EnableWebSecurity // 웹 보안 설정을 활성화하는 애노테이션
//@RequiredArgsConstructor // Lombok을 사용하여 필수 생성자를 자동으로 생성하는 애노테이션
//public class WebSecurityConfig {
//
//    private final UserDetailService userService; // 사용자 서비스 객체
//
//    @Bean
//    public WebSecurityCustomizer configure() {
//        // 특정 요청 경로에 대해 보안 설정을 무시하도록 설정
//        return (web) -> web.ignoring()
//                .requestMatchers(new AntPathRequestMatcher("/static/**")); // 정적 리소스에 대한 요청 무시
//    }
//
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        // HTTP 보안 설정
//        return http
//                .authorizeRequests(auth -> auth
//                        .requestMatchers(
//                                new AntPathRequestMatcher("/login"), // 로그인 페이지
//                                new AntPathRequestMatcher("/signup"), // 회원가입 페이지
//                                new AntPathRequestMatcher("/user") // 사용자 정보 페이지
//                        ).permitAll() // 위 경로는 모두 접근 허용
//                        .anyRequest().authenticated()) // 그 외의 요청은 인증 필요
//                .formLogin(formLogin -> formLogin
//                        .loginPage("/login") // 로그인 페이지 설정
//                        .defaultSuccessUrl("/articles") // 로그인 성공 시 리디렉션 경로
//                )
//                .logout(logout -> logout
//                        .logoutSuccessUrl("/login") // 로그아웃 성공 시 리디렉션 경로
//                        .invalidateHttpSession(true) // 세션 무효화
//                )
//                .csrf(AbstractHttpConfigurer::disable) // CSRF 보호 비활성화
//                .build();
//    }
//
//    // 인증 관리자 관련 설정
//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailService userDetailService) throws Exception {
//        // 사용자 인증을 위한 AuthenticationManager 설정
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userService); // 사용자 서비스 설정
//        authProvider.setPasswordEncoder(bCryptPasswordEncoder); // 비밀번호 인코더 설정
//        return new ProviderManager(authProvider); // ProviderManager를 사용하여 AuthenticationManager 반환
//    }
//
//    // 패스워드 인코더로 사용할 빈 등록
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        // BCrypt 비밀번호 인코더 빈 설정
//        return new BCryptPasswordEncoder();
//    }
//}
