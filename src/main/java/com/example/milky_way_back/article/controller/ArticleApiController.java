package com.example.milky_way_back.article.controller;

import com.example.milky_way_back.article.DTO.response.LikeResponse;
import com.example.milky_way_back.article.exception.DibsNotFoundException;
import com.example.milky_way_back.article.exception.DuplicateLikeException;
import com.example.milky_way_back.article.exception.MemberNotFoundException;
import com.example.milky_way_back.article.repository.DibsRepository;
import com.example.milky_way_back.member.Repository.MemberRepository;
import com.example.milky_way_back.article.DTO.request.AddArticle;
import com.example.milky_way_back.article.DTO.response.ArticleListView;
import com.example.milky_way_back.article.DTO.response.ArticleViewResponse;
import com.example.milky_way_back.article.entity.Article;
import com.example.milky_way_back.article.exception.UnauthorizedException;
import com.example.milky_way_back.article.service.ArticleService;
import com.example.milky_way_back.member.Repository.MemberRepository;
import com.example.milky_way_back.member.Service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController //http 응답으로 객체 데이터를 json 형태로 변환
public class ArticleApiController {
    private final ArticleService articleService;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

        // 게시글 추가
    @PostMapping("/posts/write")
    public ResponseEntity<Article> addBoard(HttpServletRequest request, @RequestBody AddArticle addArticle) {
        Article savedArticle = articleService.save(request, addArticle);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
    }
    //http://localhost:8080/posts/list?page=1&size=30
    //http://localhost:8080/posts/list?page={page-id}&size={size-id}
    @GetMapping("/posts/list")
    public ResponseEntity<Page<ArticleListView>> getArticles(
            HttpServletRequest request,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ArticleListView> articleListViewPage = articleService.findAll(request, pageable);

        return ResponseEntity.ok(articleListViewPage);
    }
    @GetMapping("/posts/{id}")
    public ResponseEntity<ArticleViewResponse> getArticleById(HttpServletRequest request,@PathVariable long id) {
        try {
            ArticleViewResponse articleDTO = articleService.findById(request,id);
            return ResponseEntity.ok(articleDTO);
        } catch (MemberNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    //DELETE
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable long id) {
        System.out.println(id);
        articleService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/posts/done/{id}")
    public ResponseEntity<Article> updateRecruit(
            HttpServletRequest request,
            @PathVariable long id) {
        Article updatedArticle = articleService.updateRecruit(request, id);
        return ResponseEntity.ok(updatedArticle);
    }
    // HttpServletRequest에서 JWT 토큰 추출하는 메서드
    private String extractTokenFromRequest(HttpServletRequest request) {
        final String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // "Bearer " 이후의 토큰 부분 추출
        }
        return null;
    }


    @PostMapping("/posts/likes/{id}")
    public ResponseEntity<LikeResponse> likeArticle(HttpServletRequest request,@PathVariable("id") Long id) {
        try {
            LikeResponse response = articleService.likeArticle(request, id);
            return ResponseEntity.ok(response);
        } catch (MemberNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (DuplicateLikeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/posts/likes/{id}")
    public ResponseEntity<?> removeDibs (HttpServletRequest request,@PathVariable("id") Long id) {
        try {
            memberService.removeDibs(request, id);
            return ResponseEntity.ok("Like removed successfully");
        } catch (MemberNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found");
        } catch (DibsNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Like not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while removing like");
        }
    }
}
