package com.example.store.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 리뷰 수정 요청 DTO.
 *
 * 규칙
 * - 경로 변수의 reviewId로 대상을 지정합니다(요청 바디에 reviewId 없음).
 * - userId는 JWT sub에서 서버가 추출합니다.
 * - storeId/storeName은 수정 시 변경하지 않습니다.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateReviewRequestDto {

    /** 리뷰 본문 (REVIEW.COMENT). 최대 50자, null 허용(빈 내용 가능). */
    @Size(max = 50)
    private String comment;

    /** 평점 (REVIEW.SCORE). 1~5 범위 필수 입력. */
    @NotNull
    @Min(1) @Max(5)
    private Integer score;
}
