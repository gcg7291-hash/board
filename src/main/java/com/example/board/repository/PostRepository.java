package com.example.board.repository;


import com.example.board.entity.Post;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostRepository {

    @PersistenceContext
    private EntityManager em;

     // CRUD 구현
    public Post save(Post post) {
        em.persist(post); //persist 영구적인 em => 관리자역할
        return post;
    }

    public Post findById(Long id) {
        return em.find(Post.class, id); //2개의 정보를 넣어야함
    }

    public List<Post> findAll() {
        // Entity Manager => 단일 엔티티 조작만 기본 제공
        String jpql = "SELECT p FROM Post p"; // Post 객체 접근  jpql 약어를 반드시 작성해야함 Post의 전체데이터
       return em.createQuery(jpql, Post.class).getResultList();
    }

    public Post update(Post post) {
        return em.merge(post); // persist, find , merge, remove
    }

    public void delete(Post post) {
        em.remove(post);
    }

    public List<Post> findByTitleContaining(String keyword) {
        String jpql = "SELECT p FROM Post p WHERE p.title LIKE :keyword ";
        return em.createQuery(jpql, Post.class).setParameter("keyword","%"+ keyword + "%").getResultList();
    }

    // 1. 비영속 (id가 부여되지 않음)
    // new Post('title', 'content');

    // 2. 영속 (id가 부여됨)
    // em.persist(post);

    // => detach(), clear()

    // 3. 준영속 (detached 수정하는중)
    // em.detach(post);

    // => merger() => 영속으로 돌아감

    // 4. 삭제
    // em.remove(post)



}