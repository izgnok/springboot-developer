package me.shinsunyoung.springbootdeveloper.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import me.shinsunyoung.springbootdeveloper.dto.CreateAccessTokenRequest;
import me.shinsunyoung.springbootdeveloper.dto.CreateAccessTokenResponse;
import me.shinsunyoung.springbootdeveloper.service.RefreshTokenService;
import me.shinsunyoung.springbootdeveloper.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 토큰 관련 API 요청을 처리하는 컨트롤러 클래스입니다.
 */
@Tag(name = "Token", description = "Token 관련 API 입니다.") // Swagger 태그와 설명을 설정
@RequiredArgsConstructor
@RestController
@RequestMapping("/tokenapi")
public class TokenApiController {

    // 의존성 주입을 통해 TokenService 객체를 주입받습니다.
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;

    /**
     * 새로운 액세스 토큰을 생성합니다.
     *
     * @param request 클라이언트로부터 받은 액세스 토큰 생성 요청
     * @return 새로 생성된 액세스 토큰을 포함한 응답 객체
     */
    @Operation(
            summary = "토큰 발급", // 요약 설명
            description = "새로운 토큰을 발급 합니다." // 상세 설명
    )
    @PostMapping("/token")
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(@RequestBody CreateAccessTokenRequest request) {
        // TokenService를 사용하여 새로운 액세스 토큰을 생성합니다.
        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());
        // 생성된 액세스 토큰을 포함한 응답을 반환합니다.
        return ResponseEntity.ok(new CreateAccessTokenResponse(newAccessToken));
    }

    /**
     * 리프레시 토큰을 삭제하는 엔드포인트입니다.
     * 현재 인증된 사용자의 리프레시 토큰을 삭제합니다.
     *
     * @return 삭제 요청의 성공 여부를 응답합니다.
     */
    @Operation(
            summary = "토큰 제거", // 요약 설명
            description = "토큰을 제거 합니다." // 상세 설명
    )
    @DeleteMapping("/token")
    public ResponseEntity deleteRefreshToken() {
        refreshTokenService.delete();

        return ResponseEntity.ok()
                .build();
    }
}
