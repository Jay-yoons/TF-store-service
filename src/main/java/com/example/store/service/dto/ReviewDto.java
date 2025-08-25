package com.example.store.service.dto;

import com.example.store.service.entity.Review;
import lombok.*;

/**
 * 리뷰 조회용 응답 DTO.
 *
 * 사용/매핑 (REVIEW)
 * - reviewId -> REVIEW.REVIEW_ID           : 리뷰 PK
 * - storeId  -> REVIEW.STORE_ID            : 점포 식별자
 * - storeName -> REVIEW.STORE_NAME(선택)   : 비정규화된 점포명(있을 때만 사용)
 * - userId   -> REVIEW.USER_ID             : 작성자 식별자
 * - comment  -> REVIEW.COMENT              : 리뷰 본문(컬럼명 COMENT 주의)
 * - score    -> REVIEW.SCORE               : 평점(1~5)
 */
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDto {

    /** REVIEW.REVIEW_ID: 리뷰 PK */
    private Long reviewId;

    /** REVIEW.STORE_ID: 대상 점포 식별자 */
    private String storeId;

    /** REVIEW.STORE_NAME: 비정규화된 점포명(표시 최적화용, 없을 수 있음) */
    private String storeName;

    /** REVIEW.USER_ID: 작성자 식별자(보안상 노출 범위 주의) */
    private String userId;

    /** REVIEW.COMENT: 리뷰 본문(컬럼명 오타 주의: COMENT) */
    private String comment;

    /** REVIEW.SCORE: 평점(1~5) */
    private Integer score;

    public static ReviewDto fromEntity(Review review) {
        return ReviewDto.builder()
                .reviewId(review.getReviewId())
                .storeId(review.getStoreId())
                //.storeName(review.getStoreName())
                .userId(review.getUserId())
                .comment(review.getComment())
                .score(review.getScore())
                .build();
    }
}
