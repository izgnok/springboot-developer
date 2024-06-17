package me.shinsunyoung.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.shinsunyoung.springbootdeveloper.domain.User;
import me.shinsunyoung.springbootdeveloper.dto.AddUserRequest;
import me.shinsunyoung.springbootdeveloper.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 회원가입 및 사용자 조회를 처리하는 서비스 클래스입니다.
 * 사용자 정보를 데이터베이스에 저장하고, 사용자의 비밀번호를 암호화하여 관리합니다.
 */
@RequiredArgsConstructor
@Service
public class UserService {

    // UserRepository 객체를 주입받습니다.
    private final UserRepository userRepository;

    // 비밀번호 암호화를 위한 BCryptPasswordEncoder 객체
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * 주어진 회원가입 요청 데이터를 바탕으로 새로운 사용자를 저장합니다.
     * 비밀번호는 BCrypt 해시 알고리즘을 사용하여 암호화됩니다.
     *
     * @param dto 회원가입 요청 데이터
     * @return 생성된 사용자의 ID
     */
    public Long save(AddUserRequest dto) {
        // User 객체를 생성하고 저장합니다.
        return userRepository.save(User.builder()
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .build()).getId();
    }

    /**
     * 주어진 사용자 ID로 사용자를 조회합니다.
     * 사용자가 존재하지 않으면 예외를 발생시킵니다.
     *
     * @param userId 조회할 사용자 ID
     * @return 조회된 User 객체
     * @throws IllegalArgumentException 주어진 ID로 사용자를 찾을 수 없을 때 발생합니다.
     */
    public User findById(Long userId) {
        // 주어진 사용자 ID로 사용자를 조회하고, 없을 경우 예외를 발생시킵니다.
        return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 ID의 사용자를 찾을 수 없습니다."));
    }

    /**
     * 주어진 이메일로 사용자를 조회합니다.
     * 사용자가 존재하지 않으면 예외를 발생시킵니다.
     *
     * @param email 조회할 사용자의 이메일
     * @return 조회된 User 객체
     * @throws IllegalArgumentException 주어진 이메일로 사용자를 찾을 수 없을 때 발생합니다.
     */
    public User findByEmail(String email) {
        // 주어진 이메일로 사용자를 조회하고, 없을 경우 예외를 발생시킵니다.
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 사용자를 찾을 수 없습니다."));
    }
}
