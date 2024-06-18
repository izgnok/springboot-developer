package me.shinsunyoung.springbootdeveloper.config.error.exception;

import me.shinsunyoung.springbootdeveloper.config.error.ErrorCode;

public class NotFoundException extends BusinessBaseException {

    // 특정 ErrorCode를 받아서 메시지와 함께 예외를 생성하는 생성자
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode); // 부모 클래스인 BusinessBaseException의 생성자를 호출하여 메시지와 에러 코드를 설정합니다.
    }

    // 기본적으로 NOT_FOUND 에러 코드를 사용하여 예외를 생성하는 생성자
    public NotFoundException() {
        super(ErrorCode.NOT_FOUND); // 부모 클래스인 BusinessBaseException의 생성자를 호출하여 NOT_FOUND 에러 코드를 사용하여 예외를 생성합니다.
    }
}
