package me.shinsunyoung.springbootdeveloper.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.shinsunyoung.springbootdeveloper.config.jwt.TokenProvider;
import me.shinsunyoung.springbootdeveloper.domain.RefreshToken;
import me.shinsunyoung.springbootdeveloper.repository.RefreshTokenRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * 리프레시 토큰 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 */
@RequiredArgsConstructor
@Service
public class RefreshTokenService {

    // 의존성 주입을 통해 RefreshTokenRepository 객체를 주입받습니다.
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;

    /**
     * 주어진 리프레시 토큰 문자열로 리프레시 토큰을 조회합니다.
     * 토큰이 존재하지 않으면 예외를 발생시킵니다.
     *
     * @param refreshToken 조회할 리프레시 토큰 문자열
     * @return 조회된 RefreshToken 객체
     * @throws IllegalArgumentException 주어진 리프레시 토큰 문자열로 토큰을 찾을 수 없을 때 발생합니다.
     */
    public RefreshToken findByRefreshToken(String refreshToken) {
        // 주어진 리프레시 토큰 문자열로 리프레시 토큰 조회
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected Token"));
    }

    /**
     * 현재 인증된 사용자의 리프레시 토큰을 삭제하는 메서드입니다.
     * 토큰을 삭제할 때 사용자 ID를 추출하고 해당 사용자의 모든 리프레시 토큰을 삭제합니다.
     */
    @Transactional
    public void delete() {
        // 현재 인증된 사용자의 토큰을 가져와서 사용자 ID 추출
        String token = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
        Long userId = tokenProvider.getUserId(token);

        // 해당 사용자의 모든 리프레시 토큰 삭제
        refreshTokenRepository.deleteByUserId(userId);
    }
}
