package me.shinsunyoung.springbootdeveloper.config.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter // Lombok 애노테이션으로, 모든 필드에 대한 getter 메서드를 자동 생성합니다.
public enum ErrorCode {
    // 올바르지 않은 입력값
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "E1", "올바르지 않은 입력값입니다."),
    // 잘못된 HTTP 메서드 호출
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "E2", "잘못된 HTTP 메서드를 호출했습니다."),
    // 서버 에러 발생
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E3", "서버 에러가 발생했습니다."),
    // 존재하지 않는 엔티티
    NOT_FOUND(HttpStatus.NOT_FOUND, "E4", "존재하지 않는 엔티티입니다."),
    // 존재하지 않는 아티클
    ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, "A1", "존재하지 않는 아티클입니다.");

    private final String message; // 에러 메시지
    private final String code; // 에러 코드
    private final HttpStatus status; // HTTP 상태 코드

    // ErrorCode 생성자
    ErrorCode(final HttpStatus status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
