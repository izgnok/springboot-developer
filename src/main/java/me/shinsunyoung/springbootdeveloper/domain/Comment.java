package me.shinsunyoung.springbootdeveloper.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Table(name = "comments") // 데이터베이스 테이블 이름을 지정합니다.
@EntityListeners(AuditingEntityListener.class) // JPA 감시 리스너를 설정합니다.
@Entity // JPA 엔티티임을 나타냅니다.
@Getter // Lombok을 사용하여 Getter 메서드를 자동 생성합니다.
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Lombok을 사용하여 기본 생성자를 생성하며, protected 접근 제어자로 설정합니다.
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 생성되는 식별자(strategy = GenerationType.IDENTITY)를 설정합니다.
    @Column(name = "id", updatable = false) // 데이터베이스 컬럼 이름을 지정하고, 업데이트가 되지 않도록 설정합니다.
    private Long id;

    @Column(name = "author", nullable = false) // 데이터베이스 컬럼 이름을 지정하고, null이 허용되지 않도록 설정합니다.
    private String author;

    @Column(name = "content", nullable = false) // 데이터베이스 컬럼 이름을 지정하고, null이 허용되지 않도록 설정합니다.
    private String content;

    @CreatedDate // Spring Data JPA에서 제공하는 생성일자를 자동으로 매핑합니다.
    @Column(name = "created_at") // 데이터베이스 컬럼 이름을 지정합니다.
    private LocalDateTime createdAt;

    @ManyToOne // 다대일 관계를 설정합니다.
    private Article article; // 해당 댓글이 속하는 게시글을 나타냅니다.

    @Builder // 빌더 패턴을 사용하여 객체를 생성할 수 있는 빌더 메서드를 생성합니다.
    public Comment(Article article, String author, String content) {
        this.article = article;
        this.author = author;
        this.content = content;
    }
}
