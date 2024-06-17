package me.shinsunyoung.springbootdeveloper.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name = "users") // 데이터베이스 테이블 이름을 설정하는 애노테이션
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 인자 없는 생성자를 자동으로 생성하지만, 외부에서 호출할 수 없도록 제한하는 애노테이션
@Getter // Lombok을 사용하여 getter 메서드를 자동으로 생성하는 애노테이션
@Entity // JPA 엔티티임을 나타내는 애노테이션
public class User implements UserDetails { // Spring Security의 UserDetails 인터페이스를 구현하여 사용자 정보를 제공하는 클래스
    @Id // 기본 키(PK) 필드를 나타내는 애노테이션
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 생성되는 PK 값을 데이터베이스에서 관리하는 방식을 설정하는 애노테이션
    @Column(name = "id", updatable = false) // 데이터베이스 테이블의 컬럼을 나타내는 애노테이션
    private Long id;

    @Column(name = "email", nullable = false, unique = true) // 데이터베이스 테이블의 컬럼을 나타내는 애노테이션. NOT NULL 제약 조건이 설정된 필드, 유니크 제약 조건이 설정된 필드
    private String email;

    @Column(name = "password") // 데이터베이스 테이블의 컬럼을 나타내는 애노테이션
    private String password;

    @Column(name = "nickname", unique = true)
    private String nickname;

    @Builder // 빌더 패턴을 사용하여 객체를 생성할 수 있는 빌더 메서드를 자동으로 생성하는 애노테이션
    public User(String email, String password, String nickname) { // 생성자 메서드
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    // 사용자의 권한을 반환하는 메서드
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    // 사용자의 이메일을 반환하는 메서드
    @Override
    public String getUsername() {
        return email;
    }

    // 사용자의 비밀번호를 반환하는 메서드
    @Override
    public String getPassword() {
        return password;
    }

    // 계정이 만료되지 않았는지를 나타내는 메서드
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정이 잠기지 않았는지를 나타내는 메서드
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 자격 증명이 만료되지 않았는지를 나타내는 메서드
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정이 활성화되어 있는지를 나타내는 메서드
    @Override
    public boolean isEnabled() {
        return true;
    }

    // 사용자 이름 변경
    public User update(String nickname) {
        this.nickname = nickname;
        return this;
    }
}
