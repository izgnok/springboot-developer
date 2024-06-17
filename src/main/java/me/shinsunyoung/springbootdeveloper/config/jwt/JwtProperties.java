package me.shinsunyoung.springbootdeveloper.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties("jwt") // 자바 클래스에 프로퍼티값을 가져와서 사용하는 애노테이션
public class JwtProperties {
    // JWT 발급자(issuer) 설정
    private String issuer;

    // JWT 비밀키(secret key) 설정
    private String secretKey;
}
