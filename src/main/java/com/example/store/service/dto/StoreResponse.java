package com.example.store.service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalTime;

/**
 * 가게 조회 응답 DTO.
 * - storeId: 가게 식별자(STORES.STORE_ID)
 * - storeName: 가게 이름
 * - categoryCode: 카테고리 코드(Integer)
 * - storeLocation: 가게 주소
 * - seatNum: 전체 좌석 수
 * - openTime/closeTime: 영업 시간(우선)
 * - availableSeats: 여유 좌석 수(= 전체 좌석 - 사용중 좌석)
 * - openNow/openStatus: 현재 영업 상태
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreResponse {

    /** STORES.STORE_ID: 가게 식별자 */
    private String storeId;

    /** STORES.STORE_NAME: 가게 이름 */
    private String storeName;

    /** STORES.CATEGORY_CODE: 카테고리 코드 */
    private Integer categoryCode;

    /** 파생: 카테고리 한글명(코드 -> 라벨 매핑) */
    private String categoryName;

    /** STORES.STORE_LOCATION: 주소 문자열(표시용) */
    private String storeLocation;

    /** STORES.SEAT_NUM: 전체 좌석 수(여유 좌석의 모수) */
    private int seatNum;

    /** STORES.OPEN_TIME: 영업 시작 시간 */
    private LocalTime openTime;

    /** STORES.CLOSE_TIME: 영업 종료 시간 */
    private LocalTime closeTime;

    /** 파생: 여유 좌석 수 (= seatNum - inUsingSeat). 선택 필드 */
    private Integer availableSeats;

    /** 첨부 이미지 URL(S3/프록시 등). 선택 필드 */
    private String imageUrl;

    /** 파생: 현재 영업 여부(true/false/null) */
    private Boolean openNow;

    /** 파생: 현재 영업 상태 라벨("영업중"/"영업종료") */
    private String openStatus;

    public static StoreResponse fromEntity(com.example.store.service.entity.Store store) {
        com.example.store.service.entity.Category category = com.example.store.service.entity.Category.fromCode(store.getCategoryCode());
        return StoreResponse.builder()
                .storeId(store.getStoreId())
                .storeName(store.getStoreName())
                .categoryCode(store.getCategoryCode())
                .categoryName(category != null ? category.getKoreanName() : null)
                .seatNum(store.getSeatNum())
                .openTime(store.getOpenTime())
                .closeTime(store.getCloseTime())
                .build();
    }

    // (선택) 좌석 정보까지 포함하는 변환 메서드
    public static StoreResponse fromEntityWithSeats(com.example.store.service.entity.Store store, int availableSeats) {
        StoreResponse response = fromEntity(store);
        response.setAvailableSeats(availableSeats);
        return response;
    }
}
