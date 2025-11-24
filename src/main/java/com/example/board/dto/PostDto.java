package com.example.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data  // get ,set 자동 생성
@NoArgsConstructor // 기본 생성자 만들어줌
@AllArgsConstructor // 모든 생성자 만들어줌
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
}
