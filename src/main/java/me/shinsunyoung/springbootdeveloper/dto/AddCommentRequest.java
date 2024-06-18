package me.shinsunyoung.springbootdeveloper.dto;

import lombok.AllArgsConstructor; // Lombok 애너테이션: 모든 필드를 대상으로 한 생성자 생성
import lombok.Getter; // Lombok 애너테이션: 모든 필드에 대한 getter 메서드 생성
import lombok.NoArgsConstructor; // Lombok 애너테이션: 매개변수가 없는 기본 생성자 생성
import lombok.ToString;
import me.shinsunyoung.springbootdeveloper.domain.Article; // Article 엔티티 import
import me.shinsunyoung.springbootdeveloper.domain.Comment; // Comment 엔티티 import

@NoArgsConstructor // 기본 생성자 생성
@AllArgsConstructor // 모든 필드를 매개변수로 받는 생성자 생성
@Getter // 모든 필드에 대한 getter 메서드 생성
@ToString
public class AddCommentRequest {
    private Long articleId; // 댓글이 속한 게시물의 ID
    private String content; // 댓글 내용

    // Comment 엔티티로 변환하는 메서드
    public Comment toEntity(String author, Article article) {

        return Comment.builder() // Comment 객체의 빌더를 이용해 객체 생성
                .article(article) // Comment 객체의 article 필드 설정
                .content(content) // Comment 객체의 content 필드 설정
                .author(author) // Comment 객체의 author 필드 설정
                .build(); // Comment 객체 생성 후 반환
    }
}
