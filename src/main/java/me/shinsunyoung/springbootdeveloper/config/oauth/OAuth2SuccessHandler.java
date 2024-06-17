package me.shinsunyoung.springbootdeveloper.config.oauth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.shinsunyoung.springbootdeveloper.config.jwt.TokenProvider;
import me.shinsunyoung.springbootdeveloper.domain.RefreshToken;
import me.shinsunyoung.springbootdeveloper.domain.User;
import me.shinsunyoung.springbootdeveloper.repository.RefreshTokenRepository;
import me.shinsunyoung.springbootdeveloper.service.UserService;
import me.shinsunyoung.springbootdeveloper.util.CookieUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;

/**
 * OAuth2 로그인 성공 시 호출되는 핸들러 클래스입니다.
 * 사용자의 인증 성공 후, 리프레시 토큰을 생성하고 쿠키에 저장하며, 액세스 토큰을 생성하여 리디렉션합니다.
 */
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    /**
     * 리프레시 토큰을 저장할 쿠키의 이름
     */
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";

    /**
     * 리프레시 토큰의 유효 기간
     */
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);

    /**
     * 액세스 토큰의 유효 기간
     */
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1);

    /**
     * 로그인 후 리디렉션할 경로
     */
    public static final String REDIRECT_PATH = "/articles";

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;
    private final UserService userService;

    /**
     * OAuth2 로그인 성공 시 호출되는 메서드입니다.
     *
     * @param request        HTTP 요청 객체
     * @param response       HTTP 응답 객체
     * @param authentication 인증 객체
     * @throws IOException 입출력 예외가 발생할 경우
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        User user = userService.findByEmail((String) oAuth2User.getAttributes().get("email"));

        // 리프레시 토큰 생성 및 저장
        String refreshToken = tokenProvider.generateToken(user, REFRESH_TOKEN_DURATION);
        saveRefreshToken(user.getId(), refreshToken);
        addRefreshTokenToCookie(request, response, refreshToken);

        // 액세스 토큰 생성 및 리디렉션
        String accessToken = tokenProvider.generateToken(user, ACCESS_TOKEN_DURATION);
        String targetUrl = getTargetUrl(accessToken);

        // 인증 성공 후에 사용된 인증 속성을 지우고 리디렉션을 수행
        clearAuthenticationAttributes(request, response);

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    /**
     * 사용자의 리프레시 토큰을 저장합니다.
     *
     * @param userId         사용자 ID
     * @param newRefreshToken 새로 생성된 리프레시 토큰
     */
    private void saveRefreshToken(Long userId, String newRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId)
                .map(entity -> entity.update(newRefreshToken))
                .orElse(new RefreshToken(userId, newRefreshToken));

        refreshTokenRepository.save(refreshToken);
    }

    /**
     * 리프레시 토큰을 쿠키에 추가합니다.
     *
     * @param request     HTTP 요청 객체
     * @param response    HTTP 응답 객체
     * @param refreshToken 추가할 리프레시 토큰
     */
    private void addRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        int cookieMaxAge = (int) REFRESH_TOKEN_DURATION.toSeconds();

        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);
        CookieUtil.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken, cookieMaxAge);
    }

    /**
     * 인증에 사용된 모든 속성을 지우는 메서드입니다.
     *
     * @param request  HTTP 요청 객체
     * @param response HTTP 응답 객체
     */
    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    /**
     * 리디렉션할 URL을 생성합니다.
     *
     * @param token 액세스 토큰
     * @return 생성된 URL 문자열
     */
    private String getTargetUrl(String token) {
        return UriComponentsBuilder.fromUriString(REDIRECT_PATH)
                .queryParam("token", token)
                .build()
                .toUriString();
    }
}
