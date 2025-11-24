package com.example.board.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "post")
@Getter
@Setter
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // <- AUTO INCREAMENT
    private Long id;  // generagtevalue 자동생성 됨

    @Column(nullable = false, length = 100) // NOT NULL, VARCHAR(100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT") // length 를 써도됨
    private String content;

    @Column(name = "created_at", updatable =false) // <- 자동으로 컨버팅 sql은 스네이크 방식으로 생성
    private LocalDateTime createdAt;

    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @PrePersist // 생성 시점에 자동으로 현재 시간을 설정
    public void prePersist()
    {
        this.createdAt = LocalDateTime.now();
    }



}
