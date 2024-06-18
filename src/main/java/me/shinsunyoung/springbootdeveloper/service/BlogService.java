package me.shinsunyoung.springbootdeveloper.service;

import lombok.RequiredArgsConstructor; // Lombok 애너테이션: final 필드가 있는 생성자 자동 생성
import me.shinsunyoung.springbootdeveloper.config.error.exception.ArticleNotFoundException; // ArticleNotFoundException import
import me.shinsunyoung.springbootdeveloper.domain.Article; // Article 엔티티 import
import me.shinsunyoung.springbootdeveloper.domain.Comment; // Comment 엔티티 import
import me.shinsunyoung.springbootdeveloper.dto.AddArticleRequest; // AddArticleRequest DTO import
import me.shinsunyoung.springbootdeveloper.dto.AddCommentRequest; // AddCommentRequest DTO import
import me.shinsunyoung.springbootdeveloper.dto.UpdateArticleRequest; // UpdateArticleRequest DTO import
import me.shinsunyoung.springbootdeveloper.repository.BlogRepository; // BlogRepository import
import me.shinsunyoung.springbootdeveloper.repository.CommentRepository; // CommentRepository import
import org.springframework.security.core.context.SecurityContextHolder; // SecurityContextHolder import
import org.springframework.stereotype.Service; // Service 빈으로 등록
import org.springframework.transaction.annotation.Transactional; // 트랜잭션 애너테이션 import

import java.util.List; // List import

/**
 * 블로그 글 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 */
@RequiredArgsConstructor // final 필드가 있는 생성자 자동 생성
@Service // 빈으로 등록
public class BlogService {

    private final BlogRepository blogRepository; // BlogRepository 필드
    private final CommentRepository commentRepository; // CommentRepository 필드

    /**
     * 주어진 요청과 작성자 이름을 바탕으로 새로운 블로그 글을 저장합니다.
     *
     * @param request   추가할 글의 요청 데이터
     * @param userName  글의 작성자 이름
     * @return          저장된 Article 객체
     */
    public Article save(AddArticleRequest request, String userName) {
        return blogRepository.save(request.toEntity(userName));
    }

    /**
     * 모든 블로그 글 목록을 조회합니다.
     *
     * @return  모든 블로그 글 목록
     */
    public List<Article> findAll() {
        return blogRepository.findAll();
    }

    /**
     * 주어진 ID에 해당하는 블로그 글을 조회합니다.
     *
     * @param id    조회할 블로그 글의 ID
     * @return      조회된 Article 객체
     * @throws IllegalArgumentException 주어진 ID에 해당하는 글을 찾을 수 없을 때 발생합니다.
     */
    public Article findById(long id) {
        return blogRepository.findById(id)
                .orElseThrow(ArticleNotFoundException::new);
    }

    /**
     * 주어진 ID에 해당하는 블로그 글을 삭제합니다.
     *
     * @param id    삭제할 블로그 글의 ID
     * @throws IllegalArgumentException 주어진 ID에 해당하는 글을 찾을 수 없을 때 발생합니다.
     */
    public void delete(long id) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
        authorizeArticleAuthor(article);
        blogRepository.delete(article);
    }

    /**
     * 주어진 ID에 해당하는 블로그 글을 수정합니다.
     *
     * @param id        수정할 블로그 글의 ID
     * @param request   수정할 글의 요청 데이터
     * @return          수정된 Article 객체
     * @throws IllegalArgumentException 주어진 ID에 해당하는 글을 찾을 수 없을 때 발생합니다.
     */
    @Transactional // 트랜잭션 메서드
    public Article update(long id, UpdateArticleRequest request) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));

        authorizeArticleAuthor(article);
        article.update(request.getTitle(), request.getContent());
        return article;
    }

    /**
     * 게시글을 작성한 사용자인지 확인합니다.
     *
     * @param article   확인할 게시글 Article 객체
     * @throws IllegalArgumentException 작성자가 아닐 경우 발생합니다.
     */
    private static void authorizeArticleAuthor(Article article) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!article.getAuthor().equals(userName)) {
            throw new IllegalArgumentException("not authorized");
        }
    }

    /**
     * 주어진 요청과 사용자 이름을 바탕으로 새로운 댓글을 추가합니다.
     *
     * @param request   추가할 댓글의 요청 데이터
     * @param userName  댓글의 작성자 이름
     * @return          저장된 Comment 객체
     */
    public Comment addComment(AddCommentRequest request, String userName) {
        // 요청에서 게시물 ID를 사용하여 해당 게시물을 불러옴
        Article article = blogRepository.findById(request.getArticleId())
                .orElseThrow(() -> new IllegalArgumentException("not found : " + request.getArticleId()));

        // 받아온 게시물과 사용자 이름을 사용하여 댓글 엔티티 생성 후 저장
        return commentRepository.save(request.toEntity(userName, article));
    }
}
