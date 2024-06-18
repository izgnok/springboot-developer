package me.shinsunyoung.springbootdeveloper.config.error;

import lombok.extern.slf4j.Slf4j;
import me.shinsunyoung.springbootdeveloper.config.error.exception.BusinessBaseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j // Slf4j는 각 로깅 메시지에 대한 레벨 (trace, debug, info, warn, error)을 지원하며, 이는 개발 중 디버깅 및 프로그램 실행 중 문제 해결을 돕습니다.
@ControllerAdvice // 모든 컨트롤러에서 발생하는 예외를 잡아서 처리하는 공통 처리 클래스임을 나타냅니다.
public class GlobalExceptionHandler {

    // 지원하지 않는 HTTP 메서드 호출 시 발생하는 예외를 처리합니다.
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class) // HttpRequestMethodNotSupportedException 예외를 잡아서 처리합니다.
    protected ResponseEntity<ErrorResponse> handle(HttpRequestMethodNotSupportedException e) {
        log.error("HttpRequestMethodNotSupportedException", e); // 에러 로그를 남깁니다.
        return createErrorResponseEntity(ErrorCode.METHOD_NOT_ALLOWED); // 적절한 ErrorResponse를 생성하여 반환합니다.
    }

    // 메서드 인자 유효성 검사 실패 시 발생하는 예외를 처리합니다.
    @ExceptionHandler(MethodArgumentNotValidException.class) // MethodArgumentNotValidException 예외를 잡아서 처리합니다.
    protected ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException", e); // 에러 로그를 남깁니다.
        return createErrorResponseEntity(ErrorCode.INVALID_INPUT_VALUE); // 적절한 ErrorResponse를 생성하여 반환합니다.
    }

    // BusinessBaseException을 상속받는 사용자 정의 비즈니스 예외를 처리합니다.
    @ExceptionHandler(BusinessBaseException.class) // BusinessBaseException 예외를 잡아서 처리합니다.
    protected ResponseEntity<ErrorResponse> handle(BusinessBaseException e) {
        log.error("BusinessException", e); // 에러 로그를 남깁니다.
        return createErrorResponseEntity(e.getErrorCode()); // 예외에서 받아온 ErrorCode를 사용하여 ErrorResponse를 생성하여 반환합니다.
    }

    // 그 외 모든 예외를 처리합니다.
    @ExceptionHandler(Exception.class) // Exception 예외를 잡아서 처리합니다.
    protected ResponseEntity<ErrorResponse> handle(Exception e) {
        e.printStackTrace(); // 스택 트레이스를 출력합니다.
        log.error("Exception", e); // 에러 로그를 남깁니다.
        return createErrorResponseEntity(ErrorCode.INTERNAL_SERVER_ERROR); // 내부 서버 오류 코드로 ErrorResponse를 생성하여 반환합니다.
    }

    // ErrorResponse를 생성하여 ResponseEntity로 래핑합니다.
    private ResponseEntity<ErrorResponse> createErrorResponseEntity(ErrorCode errorCode) {
        return new ResponseEntity<>(
                ErrorResponse.of(errorCode), // ErrorCode를 이용해 ErrorResponse 객체를 생성합니다.
                errorCode.getStatus()); // 해당 ErrorCode의 HTTP 상태 코드를 ResponseEntity의 상태로 설정합니다.
    }
}
