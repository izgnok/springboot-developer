package me.shinsunyoung.springbootdeveloper.config.error.exception;

import me.shinsunyoung.springbootdeveloper.config.error.ErrorCode;

public class BusinessBaseException extends RuntimeException{

    // 에러 코드를 저장할 필드
    private final ErrorCode errorCode;

    // 메시지와 에러 코드를 인자로 받아 예외를 생성하는 생성자
    public BusinessBaseException(String message, ErrorCode errorCode) {
        super(message); // 부모 클래스인 RuntimeException의 생성자를 호출하여 메시지를 설정합니다.
        this.errorCode = errorCode; // 에러 코드를 설정합니다.
    }

    // 에러 코드를 인자로 받아 예외를 생성하는 생성자
    public BusinessBaseException(ErrorCode errorCode) {
        super(errorCode.getMessage()); // 에러 코드에서 메시지를 가져와 부모 클래스 생성자에 전달합니다.
        this.errorCode = errorCode; // 에러 코드를 설정합니다.
    }

    // 에러 코드를 반환하는 getter 메서드
    public ErrorCode getErrorCode() {
        return errorCode; // 저장된 에러 코드를 반환합니다.
    }
}
