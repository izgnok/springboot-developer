package me.shinsunyoung.springbootdeveloper.config.error;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter // Lombok 애노테이션으로, 모든 필드에 대한 getter 메서드를 자동 생성합니다.
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Lombok 애노테이션으로, 접근 수준을 PROTECTED로 설정한 기본 생성자를 자동 생성합니다.
public class ErrorResponse {

    private String message; // 에러 메시지
    private String code; // 에러 코드

    // ErrorCode를 인자로 받는 생성자, private으로 접근 제한
    private ErrorResponse(final ErrorCode code) {
        this.message = code.getMessage();
        this.code = code.getCode();
    }

    // ErrorCode와 커스텀 메시지를 인자로 받는 생성자
    public ErrorResponse(final ErrorCode code, final String message) {
        this.message = message;
        this.code = code.getCode();
    }

    // ErrorCode를 인자로 받아 ErrorResponse 객체를 생성하는 static 메서드
    public static ErrorResponse of(final ErrorCode code) {
        return new ErrorResponse(code);
    }

    // ErrorCode와 커스텀 메시지를 인자로 받아 ErrorResponse 객체를 생성하는 static 메서드
    public static ErrorResponse of(final ErrorCode code, final String message) {
        return new ErrorResponse(code, message);
    }
}
