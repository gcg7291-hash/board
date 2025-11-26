package com.example.board.service;

import com.example.board.entity.Post;
import com.example.board.repository.PostRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 상위에 호출해서 하위에 하나씩 호출 안해도 됨
public class PostService {

    private final PostRepository postRepository;

    // 저장
   @Transactional(readOnly = false) // 한번 더 덮어씌우기
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    // Read (조회)
//    @Transactional(readOnly = true)
    public Post getPostById(Long id) {
       return postRepository.findById(id)
               .orElseThrow(()-> new RuntimeException("post not found"));
        // readOnly = false
        //1. 엔티티 조회
        //2. 스냅샷 저장
        //3. 트렌젝션이 끝날 때 비교
        //4. 변경이 있으면 update

        //readOnly = true
        //1. entity 조회
        //2. 읽기 끝나면 끝
    }

    // 전체 데이터 가져오기 정렬
    public List<Post> getAllPosts() {
//       return postRepository.findAll(
//               Sort.by(Sort.Direction.DESC, "id")
//       );
        return postRepository.findAllByOrderByIdDesc();

   }

    // 수정
    @Transactional
    public Post updatePost(Long id, Post updatedPost) {
        Post post = getPostById(id);
        post.setTitle(updatedPost.getTitle());
        post.setContent(updatedPost.getContent());
        return post;
    }

    // 삭제
    @Transactional
    public void deletePost(Long id) {
        Post post = getPostById(id);
        postRepository.delete(post);
    }

    @Transactional(readOnly = true)
    public void testFirstLevelCache() {
       Post post1 = postRepository.findById(1L).orElseThrow();
        System.out.println(post1.getTitle());

       Post post2 = postRepository.findById(1L).orElseThrow();
        System.out.println(post2.getTitle());

        System.out.println(post1 == post2);
    }

    @Transactional
    public void testWriteBehind(){
       Post post = postRepository.findById(1L).orElseThrow();
       post.setTitle("hello!!!!");
       System.out.println("update1");

       post.setTitle("hi!!!!!!");
       System.out.println("update2");

       post.setTitle("bye!!!!!!");
       System.out.println("update3");

        System.out.println("method end");
    }

    @Transactional
    public void testDirtyChecking() {
       Post post = postRepository.findById(1L).orElseThrow();
        System.out.println("SELECT!!!");

        post.setTitle("hello!!!!");
        System.out.println("change title");

    }

    public List<Post> searchPosts(String keyword) {
       return postRepository.findByTitleContaining(keyword);
    }

    public List<Post> searchPostsByTitleOrContent(String keyword) {
//       return postRepository.findByTitleContainingOrContentContaining(keyword, keyword);
//        return postRepository.searchByKeyword(keyword);
        return postRepository.searchByTitleNative(keyword);
    }

    public List<Post> getRecentPosts() {
//    return postRepository.findTop3ByOrderByCreatedAtDesc();
        return postRepository.findRecentPosts(PageRequest.of(0, 3));
//        return postRepository.findRecentPostsNative();

   }

    public Page<Post> getPostsPage(Pageable pageable) {
       return postRepository.findAll(pageable);
    }

    @Transactional
    public void createDummyPosts(int count) {
        for (int i = 0; i <= count; i++) {
            Post post = new Post(i +"번 제목", "게시물 내용");
            postRepository.save(post);
        }
    }

    public Page<Post> searchPostsPage(String keyword, Pageable pageable) {
       return postRepository.findByTitleContaining(keyword, pageable);
    }

    public Slice<Post> getPostsSlice(Pageable pageable) {
       return postRepository.findAllBy(pageable);
    }

    public List<Post> getAllPostWithFetchJoin() {
//        return postRepository.findAll();
        return postRepository.findAllWithComments();
    }

    public List<Post> getAllPostsWithEntityGraph() {
       return postRepository.findAllWithCommentsEntityGraph();
    }


}
