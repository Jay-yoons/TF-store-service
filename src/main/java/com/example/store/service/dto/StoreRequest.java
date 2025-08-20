package com.example.store.service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * 가게 등록/수정 요청 DTO.
 *
 * 사용/매핑
 * - 대상 테이블: STORES
 * - 컬럼 매핑 및 필요 이유
 *   - storeName  -> STORES.STORE_NAME        : 목록/상세 표기용 필수 텍스트
 *   - categoryCode -> STORES.CATEGORY_CODE   : 목록 필터/그룹핑 기준
 *   - storeLocation -> STORES.STORE_LOCATION : 화면 표기용 주소 문자열
 *   - seatNum    -> STORES.SEAT_NUM          : 여유 좌석 계산의 모수(전체 좌석 수)
 * - 영업 시간은 OPEN_TIME/CLOSE_TIME 컬럼을 사용합니다(등록 화면에서도 해당 값 사용 권장).
 *
 * 비고
 * - serviceTime 필드는 과거 호환을 위해 남아있을 수 있으나, 현재 백엔드는 사용하지 않습니다(Deprecated).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreRequest {
    /** STORES.STORE_NAME: 목록/상세 표기용 */
    private String storeName;
    /** STORES.CATEGORY_CODE: 목록 필터/그룹핑 기준 */
    private Integer categoryCode;
    /** STORES.STORE_LOCATION: 화면 표기용 주소 */
    private String storeLocation;
    /** STORES.SEAT_NUM: 여유 좌석 계산(= seatNum - inUsingSeat) */
    private int seatNum;

    /**
     * (Deprecated) 과거 문자열 기반 영업시간.
     * 현재 백엔드는 OPEN_TIME/CLOSE_TIME만 사용하며, 이 필드는 무시됩니다.
     */
    @Deprecated
    private String serviceTime;
}
