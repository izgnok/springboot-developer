package me.shinsunyoung.springbootdeveloper.config.oauth;

import lombok.RequiredArgsConstructor;
import me.shinsunyoung.springbootdeveloper.domain.User;
import me.shinsunyoung.springbootdeveloper.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * OAuth2 로그인을 처리하는 사용자 정의 서비스입니다.
 */
@RequiredArgsConstructor
@Service
public class OAuth2UserCustomService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    /**
     * OAuth2UserRequest를 기반으로 OAuth2User를 로드합니다.
     * 로드된 OAuth2User를 저장하거나 업데이트합니다.
     *
     * @param userRequest OAuth2 사용자 요청 객체
     * @return OAuth2User 정보
     * @throws OAuth2AuthenticationException OAuth2 인증 예외 발생 시
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        saveOrUpdate(user);
        return user;
    }

    /**
     * OAuth2User 정보를 기반으로 사용자 정보를 저장하거나 업데이트합니다.
     *
     * @param oAuth2User OAuth2 사용자 정보
     * @return 저장 또는 업데이트된 사용자 정보
     */
    private User saveOrUpdate(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        // 이메일을 기준으로 사용자를 검색하여 이미 존재하는 경우 업데이트하고, 존재하지 않는 경우 새로 생성합니다.
        User user = userRepository.findByEmail(email)
                .map(entity -> entity.update(name)) // 이미 존재하는 사용자인 경우 이름을 업데이트
                .orElse(User.builder()
                        .email(email)
                        .nickname(name) // 존재하지 않는 경우 닉네임을 설정하여 사용자 생성
                        .build());

        return userRepository.save(user); // 사용자 정보를 저장하고 반환
    }
}
