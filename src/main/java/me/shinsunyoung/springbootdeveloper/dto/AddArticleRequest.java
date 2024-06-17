package me.shinsunyoung.springbootdeveloper.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.shinsunyoung.springbootdeveloper.domain.Article;

@NoArgsConstructor // 인자 없는 생성자를 자동으로 생성하는 Lombok 애노테이션
@AllArgsConstructor // 모든 필드를 포함하는 생성자를 자동으로 생성하는 Lombok 애노테이션
@Getter // 모든 필드에 대한 getter 메서드를 자동으로 생성하는 Lombok 애노테이션
public class AddArticleRequest {

    private String title; // 글 제목 필드

    private String content; // 글 내용 필드

    // AddArticleRequest 객체를 Article 엔티티로 변환하는 메서드
    public Article toEntity(String author) {
        return Article.builder() // Article 빌더 객체 생성
                .title(title) // 제목 설정
                .content(content) // 내용 설정
                .author(author) // 작성자 설정
                .build(); // Article 객체 생성 후 반환
    }
}
