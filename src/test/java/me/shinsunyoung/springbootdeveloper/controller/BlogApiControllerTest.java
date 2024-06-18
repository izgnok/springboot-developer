//package me.shinsunyoung.springbootdeveloper.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import me.shinsunyoung.springbootdeveloper.config.error.ErrorCode;
//import me.shinsunyoung.springbootdeveloper.domain.Article;
//import me.shinsunyoung.springbootdeveloper.domain.Comment;
//import me.shinsunyoung.springbootdeveloper.domain.User;
//import me.shinsunyoung.springbootdeveloper.dto.AddArticleRequest;
//import me.shinsunyoung.springbootdeveloper.dto.AddCommentRequest;
//import me.shinsunyoung.springbootdeveloper.dto.UpdateArticleRequest;
//import me.shinsunyoung.springbootdeveloper.repository.BlogRepository;
//import me.shinsunyoung.springbootdeveloper.repository.CommentRepository;
//import me.shinsunyoung.springbootdeveloper.repository.UserRepository;
//import net.datafaker.Faker;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.security.Principal;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//class BlogApiControllerTest {
//
//    @Autowired
//    protected MockMvc mockMvc;
//
//    @Autowired
//    protected ObjectMapper objectMapper;
//
//    @Autowired
//    private WebApplicationContext context;
//
//    @Autowired
//    BlogRepository blogRepository;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    CommentRepository commentRepository;
//
//    User user;
//
//    /**
//     * 각 테스트 실행 전 MockMvc 설정을 초기화하고, 블로그 글 데이터를 모두 삭제합니다.
//     */
//    @BeforeEach
//    public void mockMvcSetUp() {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
//                .build();
//        blogRepository.deleteAll();
//        commentRepository.deleteAll();
//    }
//
//    /**
//     * 각 테스트 실행 전 사용자를 생성하고, SecurityContext에 사용자 인증 정보를 설정합니다.
//     */
//    @BeforeEach
//    void setSecurityContext() {
//        userRepository.deleteAll();
//        user = userRepository.save(User.builder()
//                .email("user@gmail.com")
//                .password("test")
//                .build());
//
//        SecurityContext context = SecurityContextHolder.getContext();
//        context.setAuthentication(new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities()));
//    }
//
//    /**
//     * 아티클 추가 API를 테스트합니다.
//     */
//    @DisplayName("addArticle: 아티클 추가에 성공한다.")
//    @Test
//    public void addArticle() throws Exception {
//        // given
//        final String url = "/blogapi/articles";
//        final String title = "title";
//        final String content = "content";
//        final AddArticleRequest userRequest = new AddArticleRequest(title, content);
//
//        final String requestBody = objectMapper.writeValueAsString(userRequest);
//
//        Principal principal = Mockito.mock(Principal.class);
//        Mockito.when(principal.getName()).thenReturn("username");
//
//        // when
//        ResultActions result = mockMvc.perform(post(url)
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .principal(principal)
//                .content(requestBody));
//
//        // then
//        result.andExpect(status().isOk());
//
//        List<Article> articles = blogRepository.findAll();
//
//        assertThat(articles.size()).isEqualTo(1);
//        assertThat(articles.get(0).getTitle()).isEqualTo(title);
//        assertThat(articles.get(0).getContent()).isEqualTo(content);
//    }
//
//    /**
//     * title이 null일 때 아티클 추가 실패를 테스트합니다.
//     */
//    @DisplayName("addArticle: 아티클 추가할 때 title이 null이면 실패한다.")
//    @Test
//    public void addArticleNullValidation() throws Exception {
//        // given
//        final String url = "/blogapi/articles";
//        final String title = null;
//        final String content = "content";
//        final AddArticleRequest userRequest = new AddArticleRequest(title, content);
//
//        final String requestBody = objectMapper.writeValueAsString(userRequest);
//
//        Principal principal = Mockito.mock(Principal.class);
//        Mockito.when(principal.getName()).thenReturn("username");
//
//        // when
//        ResultActions result = mockMvc.perform(post(url)
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .principal(principal)
//                .content(requestBody));
//
//        // then
//        result.andExpect(status().isBadRequest());
//    }
//
//    /**
//     * title이 10자를 초과할 때 아티클 추가 실패를 테스트합니다.
//     */
//    @DisplayName("addArticle: 아티클 추가할 때 title이 10자를 넘으면 실패한다.")
//    @Test
//    public void addArticleSizeValidation() throws Exception {
//        // given
//        Faker faker = new Faker();
//
//        final String url = "/blogapi/articles";
//        final String title = faker.lorem().characters(11);
//        final String content = "content";
//        final AddArticleRequest userRequest = new AddArticleRequest(title, content);
//
//        final String requestBody = objectMapper.writeValueAsString(userRequest);
//
//        Principal principal = Mockito.mock(Principal.class);
//        Mockito.when(principal.getName()).thenReturn("username");
//
//        // when
//        ResultActions result = mockMvc.perform(post(url)
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .principal(principal)
//                .content(requestBody));
//
//        // then
//        result.andExpect(status().isBadRequest());
//    }
//
//    /**
//     * 모든 아티클 목록 조회 API를 테스트합니다.
//     */
//    @DisplayName("findAllArticles: 아티클 목록 조회에 성공한다.")
//    @Test
//    public void findAllArticles() throws Exception {
//        // given
//        final String url = "/blogapi/articles";
//        Article savedArticle = createDefaultArticle();
//
//        // when
//        final ResultActions resultActions = mockMvc.perform(get(url)
//                .accept(MediaType.APPLICATION_JSON));
//
//        // then
//        resultActions
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].content").value(savedArticle.getContent()))
//                .andExpect(jsonPath("$[0].title").value(savedArticle.getTitle()));
//    }
//
//    /**
//     * 특정 아티클 조회 API를 테스트합니다.
//     */
//    @DisplayName("findArticle: 아티클 단건 조회에 성공한다.")
//    @Test
//    public void findArticle() throws Exception {
//        // given
//        final String url = "/blogapi/articles/{id}";
//        Article savedArticle = createDefaultArticle();
//
//        // when
//        final ResultActions resultActions = mockMvc.perform(get(url, savedArticle.getId()));
//
//        // then
//        resultActions
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.content").value(savedArticle.getContent()))
//                .andExpect(jsonPath("$.title").value(savedArticle.getTitle()));
//    }
//
//    /**
//     * 아티클 삭제 API를 테스트합니다.
//     */
//    @DisplayName("deleteArticle: 아티클 삭제에 성공한다.")
//    @Test
//    public void deleteArticle() throws Exception {
//        // given
//        final String url = "/blogapi/articles/{id}";
//        Article savedArticle = createDefaultArticle();
//
//        // when
//        mockMvc.perform(delete(url, savedArticle.getId()))
//                .andExpect(status().isOk());
//
//        // then
//        List<Article> articles = blogRepository.findAll();
//
//        assertThat(articles).isEmpty();
//    }
//
//    /**
//     * 아티클 수정 API를 테스트합니다.
//     */
//    @DisplayName("updateArticle: 아티클 수정에 성공한다.")
//    @Test
//    public void updateArticle() throws Exception {
//        // given
//        final String url = "/blogapi/articles/{id}";
//        Article savedArticle = createDefaultArticle();
//
//        final String newTitle = "new title";
//        final String newContent = "new content";
//
//        UpdateArticleRequest request = new UpdateArticleRequest(newTitle, newContent);
//
//        // when
//        ResultActions result = mockMvc.perform(put(url, savedArticle.getId())
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(objectMapper.writeValueAsString(request)));
//
//        // then
//        result.andExpect(status().isOk());
//
//        Article article = blogRepository.findById(savedArticle.getId()).get();
//
//        assertThat(article.getTitle()).isEqualTo(newTitle);
//        assertThat(article.getContent()).isEqualTo(newContent);
//    }
//
//    /**
//     * 기본적인 Article 객체를 생성하여 저장합니다.
//     *
//     * @return 저장된 Article 객체
//     */
//    private Article createDefaultArticle() {
//        return blogRepository.save(Article.builder()
//                .title("title")
//                .author(user.getUsername())
//                .content("content")
//                .build());
//    }
//
//    @DisplayName("findArticle: 잘못된 HTTP 메서드로 아티클을 조회하려고 하면 조회에 실패한다.")
//    @Test
//    public void invalidHttpMethod() throws Exception {
//        // given
//        final String url = "/blogapi/articles/{id}";
//
//        // when
//        final ResultActions resultActions = mockMvc.perform((post(url, 1)));
//
//        // then
//        resultActions.andDo(print())
//                .andExpect(status().isMethodNotAllowed())  // HTTP 상태가 잘못된 메서드임을 나타냄
//                .andExpect(jsonPath("$.message").value(ErrorCode.METHOD_NOT_ALLOWED.getMessage()));  // 에러 응답의 메시지가 기대한 메서드 허용 에러임을 검증
//    }
//
//    @DisplayName("findArticle: 존재하지 않는 아티클을 조회하려고 하면 조회에 실패한다.")
//    @Test
//    public void findArticleInvalidArticle() throws Exception {
//        // given
//        final String url = "/blogapi/articles/{id}";
//        final long invalidId = 1;
//
//        // when
//        final ResultActions resultActions = mockMvc.perform(get(url, invalidId));
//
//        // then
//        resultActions
//                .andDo(print())
//                .andExpect(status().isNotFound())  // HTTP 상태가 리소스를 찾을 수 없음을 나타냄
//                .andExpect(jsonPath("$.message").value(ErrorCode.ARTICLE_NOT_FOUND.getMessage()))  // 에러 응답의 메시지가 기대한 아티클을 찾을 수 없음임을 검증
//                .andExpect(jsonPath("$.code").value(ErrorCode.ARTICLE_NOT_FOUND.getCode()));  // 에러 응답의 코드가 기대한 아티클을 찾을 수 없음의 코드임을 검증
//    }
//
//    @DisplayName("addComment: 댓글 추가에 성공한다.")
//    @Test
//    public void addComment() throws Exception {
//        // given
//        final String url = "/blogapi/comments"; // API 엔드포인트 URL
//
//        // 기본적인 Article을 생성하여 저장
//        Article savedArticle = createDefaultArticle();
//        final Long articleId = savedArticle.getId(); // 저장된 Article의 ID
//        final String content = "content"; // 댓글 내용
//        final AddCommentRequest userRequest = new AddCommentRequest(articleId, content); // 사용자가 보낸 댓글 추가 요청 객체
//        final String requestBody = objectMapper.writeValueAsString(userRequest); // 요청 객체를 JSON 문자열로 변환
//
//        Principal principal = Mockito.mock(Principal.class); // Mock 객체로 Principal 생성
//        Mockito.when(principal.getName()).thenReturn("username"); // Mock Principal 객체의 getName() 메서드 동작 정의
//
//        // when
//        ResultActions result = mockMvc.perform(post(url)
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .principal(principal) // 인증된 사용자 Principal 설정
//                .content(requestBody)); // 요청 본문 설정
//
//        // then
//        result.andExpect(status().isOk()); // HTTP 응답 상태가 200 OK인지 검증
//
//        // 추가된 모든 댓글을 검색
//        List<Comment> comments = commentRepository.findAll();
//
//        // 검증
//        assertThat(comments.size()).isEqualTo(1); // 댓글 개수가 1인지 검증
//        assertThat(comments.get(0).getArticle().getId()).isEqualTo(articleId); // 댓글이 올바른 Article에 연결되었는지 검증
//        assertThat(comments.get(0).getContent()).isEqualTo(content); // 댓글 내용이 올바르게 저장되었는지 검증
//    }
//}
