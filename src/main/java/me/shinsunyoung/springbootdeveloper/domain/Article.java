package me.shinsunyoung.springbootdeveloper.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class) // JPA Auditing 기능을 사용하기 위한 애노테이션
@Entity // JPA 엔티티임을 나타내는 애노테이션
@Getter // Lombok을 사용하여 getter 메서드를 자동으로 생성하는 애노테이션
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 인자 없는 생성자를 자동으로 생성하지만, 외부에서 호출할 수 없도록 제한하는 애노테이션
public class Article {
    @Id // 기본 키(PK) 필드를 나타내는 애노테이션
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 생성되는 PK 값을 데이터베이스에서 관리하는 방식을 설정하는 애노테이션
    @Column(name = "id", updatable = false) // 데이터베이스 테이블의 컬럼을 나타내는 애노테이션
    private Long id;

    @Column(name = "title", nullable = false) // 데이터베이스 테이블의 컬럼을 나타내는 애노테이션. NOT NULL 제약 조건이 설정된 필드
    private String title;

    @Column(name = "content", nullable = false) // 데이터베이스 테이블의 컬럼을 나타내는 애노테이션. NOT NULL 제약 조건이 설정된 필드
    private String content;

    @Column(name = "author", nullable = false)
    private String author;

    @CreatedDate // 엔티티가 생성될 때 자동으로 현재 시간 값이 할당됨
    @Column(name = "created_at") // 데이터베이스 컬럼 이름 지정
    private LocalDateTime createdAt;

    @LastModifiedDate // 엔티티가 수정될 때 자동으로 현재 시간 값이 할당됨
    @Column(name = "updated_at") // 데이터베이스 컬럼 이름 지정
    private LocalDateTime updatedAt;

    @Builder // 빌더 패턴을 사용하여 객체를 생성할 수 있는 빌더 메서드를 자동으로 생성하는 애노테이션
    public Article(String author, String title, String content) {
        this.author = author;
        this.title = title;
        this.content = content;
    }

    // 엔티티의 필드 값을 업데이트하는 메서드
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
