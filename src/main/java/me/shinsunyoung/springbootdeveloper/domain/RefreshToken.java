package me.shinsunyoung.springbootdeveloper.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 리프레시 토큰을 나타내는 엔터티 클래스입니다.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 인자 없는 생성자를 자동으로 생성하는 Lombok 어노테이션입니다.
@Getter // 필드의 getter 메서드를 자동으로 생성하는 Lombok 어노테이션입니다.
@Entity // JPA 엔터티 클래스임을 나타내는 어노테이션입니다.
public class RefreshToken {

    @Id // 엔터티의 주키(PK)임을 나타내는 어노테이션입니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 주키의 자동 생성 전략을 IDENTITY로 설정하는 어노테이션입니다.
    @Column(name = "id", updatable = false) // 데이터베이스 열과 매핑되는 필드를 나타내는 어노테이션입니다. 업데이트 불가능하며, "id"라는 열에 매핑됩니다.
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true) // 데이터베이스 열과 매핑되는 필드를 나타내는 어노테이션입니다. null이 아니며, 유일해야 합니다.
    private Long userId; // 사용자 ID를 저장하는 필드입니다.

    @Column(name = "refresh_token", nullable = false) // 데이터베이스 열과 매핑되는 필드를 나타내는 어노테이션입니다. null이 아니어야 합니다.
    private String refreshToken; // 리프레시 토큰을 저장하는 필드입니다.

    /**
     * RefreshToken 객체의 생성자입니다.
     * @param userId 사용자 ID
     * @param refreshToken 리프레시 토큰 값
     */
    public RefreshToken(Long userId, String refreshToken) {
        this.userId = userId;
        this.refreshToken = refreshToken;
    }

    /**
     * 새로운 리프레시 토큰으로 업데이트합니다.
     * @param newRefreshToken 새로운 리프레시 토큰 값
     * @return 업데이트된 RefreshToken 객체
     */
    public RefreshToken update(String newRefreshToken) {
        this.refreshToken = newRefreshToken;
        return this;
    }
}
