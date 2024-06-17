package me.shinsunyoung.springbootdeveloper.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import me.shinsunyoung.springbootdeveloper.domain.Article;
import me.shinsunyoung.springbootdeveloper.dto.AddArticleRequest;
import me.shinsunyoung.springbootdeveloper.dto.ArticleResponse;
import me.shinsunyoung.springbootdeveloper.dto.UpdateArticleRequest;
import me.shinsunyoung.springbootdeveloper.service.BlogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Tag(name = "Blog", description = "Blog 관련 API 입니다.") // Swagger 태그와 설명을 설정
@RequiredArgsConstructor // Lombok 애노테이션을 사용하여 final 필드에 대한 생성자를 자동으로 생성
@RestController // HTTP Response Body에 객체 데이터를 JSON 형식으로 반환하는 컨트롤러
@RequestMapping("/blogapi") // 기본 URL 경로를 /blogapi로 설정
public class BlogApiController {

    private final BlogService blogService; // BlogService 주입

    @Operation(
            summary = "글 등록", // 요약 설명
            description = "블로그의 글을 등록 합니다." // 상세 설명
    )
    @PostMapping("/articles") // HTTP POST 요청을 /articles 경로와 매핑
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request, Principal principal) {
        // 요청 본문에 포함된 AddArticleRequest 객체를 받아서 블로그 글을 저장
        Article savedArticle = blogService.save(request, principal.getName());
        // 요청한 자원이 성공적으로 생성되었으며 저장된 블로그 글 정보를 응답 객체에 담아 전송
        return ResponseEntity.ok(savedArticle);
    }

    @Operation(
            summary = "글 목록", // 요약 설명
            description = "블로그의 전체 글목록을 반환 합니다." // 상세 설명
    )
    @GetMapping("/articles") // HTTP GET 요청을 /articles 경로와 매핑
    public ResponseEntity<List<ArticleResponse>> findAllArticles() {
        // 모든 블로그 글을 조회하여 ArticleResponse 객체 리스트로 변환
        List<ArticleResponse> articles = blogService.findAll().stream().map(ArticleResponse::new).toList();
        // 변환된 리스트를 응답 객체에 담아 전송
        return ResponseEntity.ok(articles);
    }

    @Operation(
            summary = "글 조회", // 요약 설명
            description = "해당 ID의 글 1개를 반환 합니다." // 상세 설명
    )
    @GetMapping("/articles/{id}") // HTTP GET 요청을 /articles/{id} 경로와 매핑
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable long id) {
        // 경로 변수로 전달된 ID를 사용하여 블로그 글을 조회
        Article article = blogService.findById(id);
        // 조회된 글을 ArticleResponse 객체로 변환하여 응답 객체에 담아 전송
        return ResponseEntity.ok(new ArticleResponse(article));
    }

    @Operation(
            summary = "글 삭제", // 요약 설명
            description = "해당 ID의 글을 삭제 합니다." // 상세 설명
    )
    @DeleteMapping("/articles/{id}") // HTTP DELETE 요청을 /articles/{id} 경로와 매핑
    public ResponseEntity<Void> deleteArticle(@PathVariable long id) {
        // 경로 변수로 전달된 ID를 사용하여 블로그 글을 삭제
        blogService.delete(id);
        // 성공적으로 삭제된 경우 응답 본문 없이 200 OK 상태 코드 반환
        return ResponseEntity.ok()
                .build();
    }

    @Operation(
            summary = "글 수정", // 요약 설명
            description = "해당 ID의 글을 수정 합니다." // 상세 설명
    )
    @PutMapping("/articles/{id}") // HTTP PUT 요청을 /articles/{id} 경로와 매핑
    public ResponseEntity<Article> updateArticle(@PathVariable long id,
                                                 @RequestBody UpdateArticleRequest request) {
        // 경로 변수로 전달된 ID와 요청 본문에 포함된 UpdateArticleRequest 객체를 사용하여 블로그 글을 수정
        Article updatedArticle = blogService.update(id, request);
        // 수정된 글 정보를 응답 객체에 담아 전송
        return ResponseEntity.ok(updatedArticle);
    }
}
