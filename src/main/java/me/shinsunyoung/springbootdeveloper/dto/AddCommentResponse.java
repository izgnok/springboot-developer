package me.shinsunyoung.springbootdeveloper.dto;

import lombok.AllArgsConstructor; // Lombok 애너테이션: 모든 필드를 매개변수로 받는 생성자 생성
import lombok.Getter; // Lombok 애너테이션: 모든 필드에 대한 getter 메서드 생성
import lombok.NoArgsConstructor; // Lombok 애너테이션: 매개변수가 없는 기본 생성자 생성
import lombok.ToString;
import me.shinsunyoung.springbootdeveloper.domain.Comment; // Comment 엔티티 import

@NoArgsConstructor // 기본 생성자 생성
@AllArgsConstructor // 모든 필드를 매개변수로 받는 생성자 생성
@Getter // 모든 필드에 대한 getter 메서드 생성
@ToString
public class AddCommentResponse {
    private Long id; // 댓글의 ID
    private String content; // 댓글 내용

    // Comment 객체를 받아서 AddCommentResponse 객체로 변환하는 생성자
    public AddCommentResponse(Comment comment) {
        this.id = comment.getId(); // Comment 객체의 ID를 설정
        this.content = comment.getContent(); // Comment 객체의 내용을 설정
    }
}
