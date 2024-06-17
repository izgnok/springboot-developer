package me.shinsunyoung.springbootdeveloper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication // 스프링 부트 애플리케이션의 주요 애노테이션
@EnableJpaAuditing // 엔티티의 createdAt, updatedAt을 자동으로 업데이트 하기 위한 애노테이션
public class SpringBootDeveloperApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootDeveloperApplication.class, args); // 애플리케이션 진입점
    }
}
