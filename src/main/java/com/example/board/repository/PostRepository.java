package com.example.board.repository;

import com.example.board.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> { //인터페이스 정의만 해주는거
    // JpaReopository에서  6가지를 자동으로 구현해줌
    // 기본 CRUD 메서드

    // 저장
    // save() => Post save(Post entity), 저장 (INSERT or UPDATE)  JpaRepositrory<Post, Long>
    // 아이디가 없으면 INSERT, 아이디가 있으면 수정

    // 조회
    // findById() => Optional<Post> findById(Long id);
    // findAll() => List<Post> findAll();
    // List<Post> findAll(Sort sort); 오버로딩

    // 삭제
    // deleteById() => void deleteById(Long id);  void delete(Post entity); 삭제라 리턴값이 필요없음

    // 갯수 조회
    // count() => long count(); 갯수 조회,

    // 존재 여부 확인
    // existsById() => boolean existsById(Long id); 아이디 기준으로 존재여부 판단

    // findBy + 필드명 + 조건

    // LIKE %keyword% 단점 코드가 길어지면 가독성이 떨어짐
    List<Post> findByTitleContaining(String keyword);

    // @Query 방식  직접 쿼리문 작성으로 searchBytitle로 함 % : %  위에 방식이랑 같음 @Param("") 을 설정해줘야함
    @Query("SELECT p FROM Post p WHERE p.title LIKE %:keyword%")
    List<Post> searchByTitle(@Param("keyword") String keyword);


    // LIKE keyword% 키워드로 시작하는 단어찾기
    List<Post> findByTitleStartingWith(String keyword);

    // > 초과
    List<Post> findByIdGreaterThan(Long id);

    // ORDER BY id DESC
    List<Post> findAllByOrderByIdDesc();

    // 제목 or 내용 으로 검색 쿼리메서드 방식
    List<Post> findByTitleContainingOrContentContaining(String titleKeyword, String contentKeyword);

    // 제목 내용 쿼리 방식
    @Query("""
            SELECT p FROM Post p 
            WHERE p.title LIKE %:keyword% OR p.content LIKE %:keyword% 
            ORDER BY p.createdAt DESC
    """)
    List<Post> searchByKeyword(@Param("keyword") String keyword);

    @Query(value="""
        SELECT * FROM post 
        WHERE title LIKE %:keyword% 
        ORDER BY id DESC
     """, nativeQuery=true)
    List<Post> searchByTitleNative(@Param("keyword")  String keyword);

    // 1.query method
    List<Post> findTop3ByOrderByCreatedAtDesc();

    // 2.jpql    createdAt 객체
    @Query("""
        SELECT p FROM Post p      
        ORDER BY p.createdAt DESC
""")
    List<Post> findRecentPosts(Pageable pageable);


    // 3.native sql  created_at table
    // List<Post> findAll() => JpaRepository 가 구현 해준 메서드
    // 오버로딩 (동일한 이름이지만 매개변수가 다름)

    @Query (value="""
            SELECT * FROM post
            ORDER BY created_at DESC
            LIMIT 3
     """, nativeQuery=true)
    List<Post> findRecentPostsNative();
   Page<Post> findAll(Pageable pageable);

   Page<Post> findByTitleContaining(String keyword, Pageable pageable);


   Slice<Post> findAllBy(Pageable pageable);

   // findAll() => Jpa 자동생성
    @Query("SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.comments")
    List<Post> findAllWithComments();

    @EntityGraph(attributePaths ={"comments"} )
    @Query("SELECT p FROM Post p")
    List<Post> findAllWithCommentsEntityGraph();
}
