package me.shinsunyoung.springbootdeveloper.config.oauth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.shinsunyoung.springbootdeveloper.util.CookieUtil;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.web.util.WebUtils;

/**
 * OAuth2 인증 요청을 쿠키를 사용하여 저장하고 관리하는 리포지토리 클래스입니다.
 */
public class OAuth2AuthorizationRequestBasedOnCookieRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    /**
     * OAuth2 인증 요청을 저장할 쿠키의 이름
     */
    public final static String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";

    /**
     * 쿠키의 만료 시간(초)
     */
    private final static int COOKIE_EXPIRE_SECONDS = 18000;

    /**
     * HTTP 요청에서 OAuth2 인증 요청을 제거하고 반환합니다.
     *
     * @param request  HTTP 요청 객체
     * @param response HTTP 응답 객체
     * @return 제거된 OAuth2AuthorizationRequest 객체
     */
    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        return this.loadAuthorizationRequest(request);
    }

    /**
     * HTTP 요청에서 OAuth2 인증 요청을 로드하여 반환합니다.
     *
     * @param request HTTP 요청 객체
     * @return 로드된 OAuth2AuthorizationRequest 객체
     */
    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        return CookieUtil.deserialize(cookie, OAuth2AuthorizationRequest.class);
    }

    /**
     * HTTP 요청에서 OAuth2 인증 요청을 저장합니다.
     *
     * @param authorizationRequest OAuth2 인증 요청 객체
     * @param request              HTTP 요청 객체
     * @param response             HTTP 응답 객체
     */
    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest == null) {
            removeAuthorizationRequestCookies(request, response);
            return;
        }
        CookieUtil.addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME,
                CookieUtil.serialize(authorizationRequest), COOKIE_EXPIRE_SECONDS);
    }

    /**
     * HTTP 요청에서 OAuth2 인증 요청을 저장한 쿠키를 제거합니다.
     *
     * @param request  HTTP 요청 객체
     * @param response HTTP 응답 객체
     */
    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
    }
}
