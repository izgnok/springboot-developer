package me.shinsunyoung.springbootdeveloper.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.SerializationUtils;

import java.util.Base64;

public class CookieUtil {
    // HttpServletResponse 객체에 쿠키를 추가합니다.
    // 쿠키의 이름(name), 값(value), 최대 수명(maxAge)을 입력받아 쿠키를 생성하여 추가합니다.
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value); // 쿠키 객체 생성
        cookie.setPath("/"); // 쿠키의 경로 설정 (루트 경로로 설정)
        cookie.setMaxAge(maxAge); // 쿠키의 수명 설정 (초 단위)
        response.addCookie(cookie); // HttpServletResponse에 쿠키 추가
    }

    // HttpServletRequest와 HttpServletResponse 객체를 사용하여 쿠키를 삭제합니다.
    // 쿠키의 이름(name)을 입력받아 해당 이름을 가진 쿠키를 찾아 삭제합니다.
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies(); // 요청으로부터 모든 쿠키 배열을 가져옵니다.
        if (cookies == null) { // 쿠키 배열이 null일 경우 (쿠키가 없을 경우)
            return; // 메서드 종료
        }

        for (Cookie cookie : cookies) { // 모든 쿠키를 순회하며
            if (name.equals(cookie.getName())) { // 입력받은 이름과 쿠키의 이름이 일치할 경우
                cookie.setValue(""); // 쿠키의 값을 빈 문자열로 설정하여 삭제
                cookie.setPath(""); // 쿠키의 경로 설정 (빈 문자열로 설정하여 루트 경로로 설정)
                cookie.setMaxAge(0); // 쿠키의 수명을 0으로 설정하여 즉시 만료되도록 설정
                response.addCookie(cookie); // HttpServletResponse에 변경된 쿠키를 추가하여 삭제 반영
            }
        }
    }

    // 객체를 직렬화하여 쿠키의 값으로 변환합니다.
    public static String serialize(Object obj) {
        byte[] serializedObject = SerializationUtils.serialize(obj); // 객체를 직렬화하여 바이트 배열로 변환
        return Base64.getUrlEncoder().encodeToString(serializedObject); // Base64 인코딩하여 문자열로 변환하여 반환
    }

    // 쿠키의 값을 역직렬화하여 객체로 변환합니다.
    // Cookie 객체와 변환할 객체의 클래스를 입력받아 역직렬화하여 반환합니다.
    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        byte[] decodedCookieValue = Base64.getUrlDecoder().decode(cookie.getValue()); // 쿠키의 값을 Base64 디코딩하여 바이트 배열로 변환
        return cls.cast(SerializationUtils.deserialize(decodedCookieValue)); // 바이트 배열을 역직렬화하여 입력받은 클래스로 캐스팅하여 반환
    }
}
