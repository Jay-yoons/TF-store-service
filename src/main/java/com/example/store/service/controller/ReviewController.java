package com.example.store.service.controller;

import com.example.store.service.dto.ReviewDto;
import com.example.store.service.dto.CreateReviewRequestDto;
import com.example.store.service.dto.UpdateReviewRequestDto;
import com.example.store.service.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

/**
 * 리뷰 관련 REST API 엔드포인트.
 * - 사용자 식별자는 Cognito JWT의 sub 클레임을 사용한다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    // 특정 가게의 모든 리뷰
    @GetMapping("/stores/{storeId}")
    public List<ReviewDto> getStoreReviews(@PathVariable String storeId) {
        return reviewService.getStoreReviews(storeId);
    }

    // 내 모든 리뷰
    @GetMapping("/my")
    public List<ReviewDto> getMyReviews() {
        return reviewService.getMyReviews();
    }

    // 특정 매장에서의 내 리뷰
    @GetMapping("/my/stores/{storeId}")
    public List<ReviewDto> getMyReviewsByStore(@PathVariable String storeId) {
        return reviewService.getMyReviewsByStore(storeId);
    }

    // [별칭] 내 모든 리뷰 (설계안: GET /reviews)
    @GetMapping
    public List<ReviewDto> getMyReviewsAlias() {
        return reviewService.getMyReviews();
    }

    // 리뷰 단건
    @GetMapping("/{id}")
    public ReviewDto getReview(@PathVariable Long id) {
        log.info("리뷰 단건 컨트롤러");
        return reviewService.getReview(id);
    }

    // 리뷰 작성
    @PostMapping
    public ReviewDto createReview(@RequestBody @Valid CreateReviewRequestDto dto) {
        log.info("리뷰 작성 컨트롤러");
        return reviewService.createReview(dto);
    }

    // 리뷰 수정
    @PutMapping("/{id}")
    public ReviewDto updateReview(@PathVariable Long id, @RequestBody @Valid UpdateReviewRequestDto dto) {
        return reviewService.updateReview(id, dto);
    }

    // 리뷰 삭제
    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
    }
}
