package me.shinsunyoung.springbootdeveloper.repository;

import me.shinsunyoung.springbootdeveloper.domain.Comment; // Comment 엔티티를 사용하기 위해 도메인 패키지 import
import org.springframework.data.jpa.repository.JpaRepository; // Spring Data JPA에서 제공하는 JpaRepository import

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Comment 엔티티와 Long 타입의 기본키를 가지는 JpaRepository를 상속하는 CommentRepository 인터페이스 선언
}
