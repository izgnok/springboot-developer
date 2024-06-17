package me.shinsunyoung.springbootdeveloper.repository;

import me.shinsunyoung.springbootdeveloper.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * RefreshToken 엔티티를 관리하기 위한 Spring Data JPA 리포지토리입니다.
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    /**
     * 주어진 사용자 ID에 해당하는 RefreshToken을 찾습니다.
     * @param userId 사용자 ID
     * @return 해당 사용자 ID에 대한 RefreshToken(Optional)
     */
    Optional<RefreshToken> findByUserId(Long userId);

    /**
     * 주어진 리프레시 토큰에 해당하는 RefreshToken을 찾습니다.
     * @param refreshToken 리프레시 토큰 값
     * @return 해당 리프레시 토큰에 대한 RefreshToken(Optional)
     */
    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    /**
     * 주어진 사용자 ID에 해당하는 모든 RefreshToken을 삭제합니다.
     * @param userId 사용자 ID
     */
    void deleteByUserId(Long userId);
}
