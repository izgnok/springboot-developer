package me.shinsunyoung.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.shinsunyoung.springbootdeveloper.config.jwt.TokenProvider;
import me.shinsunyoung.springbootdeveloper.domain.User;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * 토큰 생성 및 관리를 담당하는 서비스 클래스입니다.
 */
@RequiredArgsConstructor
@Service
public class TokenService {

    // 의존성 주입을 통해 TokenProvider 객체를 주입받습니다.
    private final TokenProvider tokenProvider;

    // 의존성 주입을 통해 RefreshTokenService 객체를 주입받습니다.
    private final RefreshTokenService refreshTokenService;

    // 의존성 주입을 통해 UserService 객체를 주입받습니다.
    private final UserService userService;

    /**
     * 주어진 리프레시 토큰을 사용하여 새로운 액세스 토큰을 생성합니다.
     * 리프레시 토큰의 유효성을 검사하고, 유효한 경우 새로운 액세스 토큰을 반환합니다.
     *
     * @param refreshToken 유효성을 검사할 리프레시 토큰
     * @return 새로 생성된 액세스 토큰
     * @throws IllegalArgumentException 주어진 리프레시 토큰이 유효하지 않을 경우 예외를 발생시킵니다.
     */
    public String createNewAccessToken(String refreshToken) {
        // 리프레시 토큰 유효성 검사에 실패하면 예외 발생
        if (!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }

        // 리프레시 토큰으로부터 사용자 ID를 조회
        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();

        // 사용자 ID로 사용자 정보를 조회
        User user = userService.findById(userId);

        // 사용자 정보를 기반으로 새로운 액세스 토큰 생성
        return tokenProvider.generateToken(user, Duration.ofHours(2));
    }
}
