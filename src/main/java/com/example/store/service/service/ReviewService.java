// Java
// 파일: src/main/java/com/example/store/service/service/ReviewService.java
package com.example.store.service.service;

import com.example.store.service.dto.ReviewDto;
import com.example.store.service.dto.CreateReviewRequestDto;
import com.example.store.service.dto.UpdateReviewRequestDto;
import com.example.store.service.entity.Review;
import com.example.store.service.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import com.example.store.service.security.CurrentUserProvider;
import org.springframework.stereotype.Service;
import com.example.store.service.exception.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Objects;

/**
 * 리뷰 비즈니스 로직.
 * - 사용자 식별자는 클라이언트가 주지 않고(JWT sub 사용) 서비스 인자로 전달받는다.
 * - 평점 범위(1~5) 검증, 중복 작성 방지, 본인 권한 검증 포함.
 */
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    // storeId -> storeName 조회용
    private final com.example.store.service.repository.StoreRepository storeRepository;
    private final CurrentUserProvider currentUserProvider;

    // 특정 가게의 리뷰 목록
    public List<ReviewDto> getStoreReviews(String storeId) {
        return reviewRepository.findByStoreId(storeId).stream()
                .map(ReviewDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 내 모든 리뷰
    public List<ReviewDto> getMyReviews() {
        String userId = currentUserProvider.getCurrentUserId();
        return reviewRepository.findByUserId(userId).stream()
                .map(ReviewDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 특정 가게에서의 내 리뷰
    public List<ReviewDto> getMyReviewsByStore(String storeId) {
        String userId = currentUserProvider.getCurrentUserId();
        return reviewRepository.findByStoreIdAndUserId(storeId, userId)
                .map(review -> List.of(ReviewDto.fromEntity(review)))
                .orElseGet(List::of);
    }

    // 리뷰 단건
    public ReviewDto getReview(Long id) {
        return reviewRepository.findById(id)
                .map(ReviewDto::fromEntity)
                .orElseThrow(() -> new NotFoundException("리뷰가 없습니다."));
    }

    // 리뷰 작성
    public ReviewDto createReview(CreateReviewRequestDto dto) {
        String userId = currentUserProvider.getCurrentUserId();
        validateScore(dto.getScore());

        // [변경] 존재 여부만 확인하는 existsBy... 메소드 사용
        if (reviewRepository.existsByStoreIdAndUserId(dto.getStoreId(), userId)) {
            throw new BadRequestException("이미 작성한 리뷰입니다.");
        }

        // storeId 유효성 및 최신 매장명 확보
        com.example.store.service.entity.Store store = storeRepository.findById(dto.getStoreId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 매장입니다."));

        Review review = Review.builder()
                .storeId(dto.getStoreId())
                .userId(userId)
                .comment(dto.getComment())
                .score(dto.getScore())
                .build();
        // 비정규화 컬럼 세팅
        //review.setStoreName(store.getStoreName());
        return ReviewDto.fromEntity(reviewRepository.save(review));
    }

    // 리뷰 수정(작성자 본인만)
    @Transactional
    public ReviewDto updateReview(Long id, UpdateReviewRequestDto dto) {
        String userId = currentUserProvider.getCurrentUserId();
        validateScore(dto.getScore());
        boolean exists = reviewRepository.existsById(id);
        if (!exists) {
            throw new NotFoundException("리뷰 없음");
        }
        int updated = reviewRepository.updateContentAndScoreByReviewIdAndUserId(
                id, userId, dto.getComment(), dto.getScore());
        if (updated == 0) {
            throw new ForbiddenException("수정 권한이 없습니다.");
        }
        return reviewRepository.findById(id)
                .map(ReviewDto::fromEntity)
                .orElseThrow(() -> new NotFoundException("리뷰 없음"));
    }

    // 리뷰 삭제(작성자 본인만)
    @Transactional
    public void deleteReview(Long id) {
        String userId = currentUserProvider.getCurrentUserId();
        boolean exists = reviewRepository.existsById(id);
        if (!exists) {
            throw new NotFoundException("리뷰 없음");
        }
        int deleted = reviewRepository.deleteByReviewIdAndUserId(id, userId);
        if (deleted == 0) {
            throw new ForbiddenException("삭제 권한이 없습니다.");
        }
    }

    // 평점 범위 검증(1~5)
    private void validateScore(Integer score) {
        if (score == null || score < 1 || score > 5) {
            throw new RuntimeException("평점은 1에서 5 사이여야 합니다.");
        }
    }
}