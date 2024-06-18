package me.shinsunyoung.springbootdeveloper.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.shinsunyoung.springbootdeveloper.domain.Article;
import me.shinsunyoung.springbootdeveloper.domain.Comment;

import java.time.LocalDateTime;
import java.util.List;

// 임시 화면용
@NoArgsConstructor
@Getter
public class ArticleViewResponse {
    private Long id;
    private String author;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private List<Comment> comments;

    public ArticleViewResponse(Article article) {
        this.id = article.getId();
        this.author = article.getAuthor();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.createdAt = article.getCreatedAt();
        this.comments = article.getComments();
    }
}