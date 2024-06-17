package me.shinsunyoung.springbootdeveloper.config.jwt;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Builder;
import lombok.Getter;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

import static java.util.Collections.emptyMap;

@Getter
public class JwtFactory {

    private String subject = "test@email.com"; // 토큰의 주제(subject)로 사용할 기본 이메일 주소입니다.

    private Date issuedAt = new Date(); // 토큰 발행 시간입니다.

    private Date expiration = new Date(new Date().getTime() + Duration.ofDays(14).toMillis()); // 토큰 만료 시간으로, 현재 시간에서 14일 후로 설정됩니다.

    private Map<String, Object> claims = emptyMap(); // 토큰에 포함될 클레임(추가 정보)으로 기본적으로 빈 맵을 사용합니다.

    @Builder
    public JwtFactory(String subject, Date issuedAt, Date expiration,
                      Map<String, Object> claims) {
        this.subject = subject != null ? subject : this.subject; // 주어진 subject가 null이 아니면 해당 값을 사용하고, 그렇지 않으면 기본값을 사용합니다.
        this.issuedAt = issuedAt != null ? issuedAt : this.issuedAt; // 주어진 issuedAt이 null이 아니면 해당 값을 사용하고, 그렇지 않으면 기본값을 사용합니다.
        this.expiration = expiration != null ? expiration : this.expiration; // 주어진 expiration이 null이 아니면 해당 값을 사용하고, 그렇지 않으면 기본값을 사용합니다.
        this.claims = claims != null ? claims : this.claims; // 주어진 claims가 null이 아니면 해당 값을 사용하고, 그렇지 않으면 기본값을 사용합니다.
    }

    public static JwtFactory withDefaultValues() {
        return JwtFactory.builder().build(); // 기본값을 사용하여 JwtFactory 인스턴스를 생성합니다.
    }

    public String createToken(JwtProperties jwtProperties) {
        return Jwts.builder()
                .setSubject(subject)
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // JWT의 헤더 타입을 설정합니다.
                .setIssuer(jwtProperties.getIssuer()) // 토큰 발행자를 설정합니다.
                .setIssuedAt(issuedAt) // 토큰 발행 시간을 설정합니다.
                .setExpiration(expiration) // 토큰 만료 시간을 설정합니다.
                .addClaims(claims) // 클레임(추가 정보)을 설정합니다.
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey()) // HS256 알고리즘을 사용하여 토큰을 서명합니다.
                .compact(); // 최종적으로 JWT 문자열을 생성하여 반환합니다.
    }
}
