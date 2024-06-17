//package me.shinsunyoung.springbootdeveloper.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import me.shinsunyoung.springbootdeveloper.domain.Article;
//import me.shinsunyoung.springbootdeveloper.domain.User;
//import me.shinsunyoung.springbootdeveloper.dto.AddArticleRequest;
//import me.shinsunyoung.springbootdeveloper.dto.UpdateArticleRequest;
//import me.shinsunyoung.springbootdeveloper.repository.BlogRepository;
//import me.shinsunyoung.springbootdeveloper.repository.UserRepository;
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
//}
