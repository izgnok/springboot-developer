package me.shinsunyoung.springbootdeveloper.repository;

import me.shinsunyoung.springbootdeveloper.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository를 상속받는 BlogRepository 인터페이스
// Article 엔티티를 관리하며, 기본 키는 Long 타입으로 설정됨
public interface BlogRepository extends JpaRepository<Article, Long> {

}
