package com.example.store.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 리뷰 생성 요청 DTO.
 *
 * 규칙
 * - userId는 클라이언트에서 받지 않습니다(JWT sub에서 서버가 추출).
 * - storeName은 받지 않습니다(서버가 Store에서 조회하여 비정규화 저장).
 * - reviewId는 생성 시점에 필요하지 않습니다.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewRequestDto {

    /** 대상 점포 식별자 (STORES.STORE_ID). 비어 있을 수 없습니다. */
    @NotBlank
    private String storeId;

    /** 리뷰 본문 (REVIEW.COMENT). 최대 50자, null 허용(빈 내용 가능). */
    @Size(max = 50)
    private String comment;

    /** 평점 (REVIEW.SCORE). 1~5 범위 필수 입력. */
    @NotNull
    @Min(1) @Max(5)
    private Integer score;
}
