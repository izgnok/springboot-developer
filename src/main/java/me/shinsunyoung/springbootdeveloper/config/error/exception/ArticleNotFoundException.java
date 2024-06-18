package me.shinsunyoung.springbootdeveloper.config.error.exception;

import me.shinsunyoung.springbootdeveloper.config.error.ErrorCode;

public class ArticleNotFoundException extends NotFoundException {
    // 기본 생성자에서 ARTICLE_NOT_FOUND 에러 코드를 사용하여 부모 클래스인 NotFoundException의 생성자를 호출합니다.
    public ArticleNotFoundException() {
        super(ErrorCode.ARTICLE_NOT_FOUND);
    }
}
