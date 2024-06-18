package me.shinsunyoung.springbootdeveloper.dto;

import lombok.Getter;
import me.shinsunyoung.springbootdeveloper.domain.Article;
import me.shinsunyoung.springbootdeveloper.domain.Comment;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ArticleResponse {

    private String author; // 글의 작성자
    private String title; // 글의 제목
    private String content; // 글의 내용
    private LocalDateTime createdAt; // 글 작성 시간
    private List<Comment> comments;

    /**
     * Article 객체를 기반으로 ArticleResponse 객체를 생성하는 생성자입니다.
     *
     * @param article Article 객체
     */
    public ArticleResponse(Article article) {
        this.author = article.getAuthor(); // Article 객체에서 작성자 정보를 가져와 설정합니다.
        this.title = article.getTitle(); // Article 객체에서 제목 정보를 가져와 설정합니다.
        this.content = article.getContent(); // Article 객체에서 내용 정보를 가져와 설정합니다.
        this.createdAt = article.getCreatedAt(); // Article 객체에서 작성 시간 정보를 가져와 설정합니다.
        this.comments = article.getComments();
    }
}
