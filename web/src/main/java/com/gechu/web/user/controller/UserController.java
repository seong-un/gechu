package com.gechu.web.user.controller;

import com.gechu.web.article.dto.ArticleMyPageDto;
import com.gechu.web.game.dto.LikeGameItemDto;
import com.gechu.web.review.dto.ReviewMyPageDto;
import com.gechu.web.user.entity.KakaoUserInfo;
import com.gechu.web.user.service.UserService;
import com.gechu.web.user.util.JwtToken;
import com.gechu.web.user.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @PostMapping("/login")
    public ResponseEntity<JwtToken> loginSuccess(@RequestBody Map<String, String> loginForm) {
        JwtToken token = userService.login(loginForm.get("username"), loginForm.get("password"));
        return ResponseEntity.ok(token);
    }

    @PostMapping("/auth")
    public Mono<ResponseEntity<?>> authenticateWithProvider(@RequestParam String code, HttpServletRequest request) {


        Enumeration<String> headerNames = request.getHeaderNames();

        if (code == null || code.isEmpty()) {
            return Mono.just(ResponseEntity.badRequest().body("인가 코드가 비어있습니다"));
        }

        try {
            return authenticateWithKakao(code);
        } catch (Exception e) {
            log.info("에러 터짐");
        }

        return Mono.just(ResponseEntity.badRequest().body("카카오 로그인만 가능합니다."));
    }
    @PostMapping("/api/web/auth")
    public Mono<ResponseEntity<?>> authenticateWithProvider2(@RequestParam String code, HttpServletRequest request) {
//        String code = authData.get("code");

        Enumeration<String> headerNames = request.getHeaderNames();

        log.info("게이트웨이를 거치지 않은 요청의 헤더 목록 시작");
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            log.info("{}: {}", headerName, request.getHeader(headerName));
        }
        log.info("게이트웨이를 거치지 않은 요청의 헤더 목록 종료");

        if (code == null || code.isEmpty()) {
            return Mono.just(ResponseEntity.badRequest().body("인가 코드가 비어있습니다"));
        }

        return authenticateWithKakao(code);
    }

    private Mono<ResponseEntity<?>> authenticateWithKakao(String code) {
        return userService.getTokenFromKakao(code)
                .flatMap(tokens -> {
                    String accessToken = tokens.get("accessToken");
                    String refreshToken = tokens.get("refreshToken");

                    if (accessToken == null) {
                        return Mono.just(ResponseEntity.badRequest().body("카카오로부터 액세스 토큰을 얻는데 실패했습니다."));
                    }

                    log.info("accessToken: {}",accessToken);

                    // 액세스 토큰을 이용하여 사용자 정보 가져오기
                    return userService.getUserInfoFromKakao(accessToken)
                            .flatMap(userInfo -> {
                                if (userInfo == null) {
                                    return Mono.just(ResponseEntity.badRequest().body("카카오로부터 사용자 정보를 불러오는데 실패했습니다."));
                                }

                                // DTO or Map을 사용해서 응답 데이터를 구성
                                Map<String, Object> responseData = new HashMap<>();
                                responseData.put("accessToken", accessToken);
                                responseData.put("refreshToken", refreshToken);
                                responseData.put("userInfo", userInfo);

                                return Mono.just(ResponseEntity.ok(responseData));
                            });
                });
    }

    @GetMapping("/users/{userSeq}/estimates")
    public ResponseEntity<?> findLikeGames(@PathVariable Long userSeq, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status;
        List<LikeGameItemDto> likeList;

        try {
            likeList = new ArrayList<>();
//            likeList = userService.findLikeGames(userSeq);
            LikeGameItemDto data1 = new LikeGameItemDto((long)1, "젤다의 전설: 왕국의 눈물", "data:image/jpeg;base64,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");
            LikeGameItemDto data2 = new LikeGameItemDto((long)2, "별의 커비 디스커버리", "https://www.techm.kr/news/photo/202205/97171_111300_2358.jpeg");

            likeList.add(data1);
            likeList.add(data2);

            resultMap.put("likeList", likeList);

            resultMap.put("success", true);
            status = HttpStatus.OK;
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status);
    }

    @GetMapping("/users/{userSeq}/reviews")
    public ResponseEntity<?> findMyReviews(@PathVariable Long userSeq, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status;
        List<ReviewMyPageDto> reviewList;

        try {
            reviewList = new ArrayList<>();
//            reviewList = userService.findMyReviews(userSeq);
            ReviewMyPageDto data1 = new ReviewMyPageDto((long)1, "젤다의 전설: 왕국의 눈물", "data:image/jpeg;base64,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"
            ,  (long)1, "리뷰 제목", "리뷰 내용",  LocalDateTime.now(), "like");
            ReviewMyPageDto data2 = new ReviewMyPageDto((long)2, "별의 커비 디스커버리", "https://www.techm.kr/news/photo/202205/97171_111300_2358.jpeg"
                    , (long)1, "리뷰 제목", "리뷰 내용", LocalDateTime.now(), "like");

            reviewList.add(data1);
            reviewList.add(data2);

            resultMap.put("reviewList", reviewList);
            resultMap.put("success", true);
            status = HttpStatus.OK;
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status);
    }

    @GetMapping("/users/{userSeq}/articles")
    public ResponseEntity<?> findMyArticles(@PathVariable Long userSeq, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status;
        List<ArticleMyPageDto> articleList;

        try {
            articleList = new ArrayList<>();
//            articleList = userService.findMyArticles(userSeq);
            ArticleMyPageDto data1 = new ArticleMyPageDto((long)1, "", "",  (long)1, "리뷰 제목", "리뷰 내용", LocalDateTime.now());
            ArticleMyPageDto data2 = new ArticleMyPageDto((long)2, "", "", (long)2, "리뷰 제목", "리뷰 내용",  LocalDateTime.now());

            articleList.add(data1);
            articleList.add(data2);

            resultMap.put("articleList", articleList);
            resultMap.put("success", true);
            status = HttpStatus.OK;
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status);
    }
}
