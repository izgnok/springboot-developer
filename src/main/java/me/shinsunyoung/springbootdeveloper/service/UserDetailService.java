package me.shinsunyoung.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.shinsunyoung.springbootdeveloper.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 로그인
@RequiredArgsConstructor // Lombok을 사용하여 필수 생성자를 자동으로 생성하는 애노테이션
@Service // 이 클래스가 서비스 레이어의 컴포넌트임을 나타내는 애노테이션
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository; // 사용자 정보에 접근하기 위한 JPA 레포지토리

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 이메일을 기준으로 사용자 정보를 로드하며, 사용자 정보를 찾지 못하면 예외를 던짐
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(email));
    }
}
