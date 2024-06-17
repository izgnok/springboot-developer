package me.shinsunyoung.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.shinsunyoung.springbootdeveloper.domain.Article;
import me.shinsunyoung.springbootdeveloper.dto.AddArticleRequest;
import me.shinsunyoung.springbootdeveloper.dto.UpdateArticleRequest;
import me.shinsunyoung.springbootdeveloper.repository.BlogRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 블로그 글 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 */
@RequiredArgsConstructor // final이 붙거나 @NotNull이 붙은 필드의 생성자 추가
@Service // 빈으로 등록
public class BlogService {

    private final BlogRepository blogRepository;

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
                .orElseThrow(() -> new IllegalArgumentException("not fount: " + id));
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
}
