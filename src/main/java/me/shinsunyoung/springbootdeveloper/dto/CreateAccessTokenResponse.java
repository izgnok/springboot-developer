package me.shinsunyoung.springbootdeveloper.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 액세스 토큰 생성 요청에 대한 응답을 나타내는 DTO 클래스입니다.
 */
@AllArgsConstructor
@Getter
public class CreateAccessTokenResponse {
    /**
     * 생성된 액세스 토큰 값.
     */
    private String accessToken;
}
