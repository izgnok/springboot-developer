package me.shinsunyoung.springbootdeveloper.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 액세스 토큰 생성을 요청할 때 사용되는 DTO 클래스입니다.
 */
@Getter
@Setter
public class CreateAccessTokenRequest {
    /**
     * 리프레시 토큰 값.
     */
    private String refreshToken;
}
