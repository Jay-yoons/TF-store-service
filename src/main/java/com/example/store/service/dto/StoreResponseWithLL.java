package com.example.store.service.dto;

import jakarta.persistence.Column;
import lombok.*;
import java.time.LocalTime;

/**
 * 가게 조회 응답 DTO(위경도 포함).
 *
 * 사용/매핑
 * - STORES
 *   - storeId        -> STORES.STORE_ID
 *   - storeName      -> STORES.STORE_NAME
 *   - categoryCode   -> STORES.CATEGORY_CODE
 *   - storeLocation  -> STORES.STORE_LOCATION
 *   - seatNum        -> STORES.SEAT_NUM
 *   - openTime       -> STORES.OPEN_TIME
 *   - closeTime      -> STORES.CLOSE_TIME
 * - STORE_LOCATION
 *   - latitude/longitude -> STORE_LOCATION.LATITUDE/LONGITUDE (서비스에서 조인하여 주입)
 * - 파생/계산
 *   - openNow/openStatus : OPEN/CLOSE 기반 현재 시간 계산
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreResponseWithLL {

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

    /** STORES.SEAT_NUM: 전체 좌석 수 */
    private int seatNum;

    /** STORES.OPEN_TIME: 영업 시작 시간 */
    private LocalTime openTime;

    /** STORES.CLOSE_TIME: 영업 종료 시간 */
    private LocalTime closeTime;

    /** 첨부 이미지 URL(S3/프록시 등). 선택 필드 */
    private String imageUrl;

    /** STORE_LOCATION.LONGITUDE: 경도 */
    private String longitude;

    /** STORE_LOCATION.LATITUDE: 위도 */
    private String latitude;

    /** 파생: 현재 영업 여부(true/false/null) */
    private Boolean openNow;

    /** 파생: 현재 영업 상태 라벨("영업중"/"영업종료") */
    private String openStatus;

    public static StoreResponseWithLL fromEntity(com.example.store.service.entity.Store store) {
        com.example.store.service.entity.Category category = com.example.store.service.entity.Category.fromCode(store.getCategoryCode());
        return StoreResponseWithLL.builder()
                .storeId(store.getStoreId())
                .storeName(store.getStoreName())
                .categoryCode(store.getCategoryCode())
                .categoryName(category != null ? category.getKoreanName() : null)
                .seatNum(store.getSeatNum())
                .openTime(store.getOpenTime())
                .closeTime(store.getCloseTime())
                .build();
    }
}
