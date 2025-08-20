package com.example.store.service.repository;

import com.example.store.service.entity.StoreLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * STORE_LOCATION 엔티티용 JPA 레포지토리.
 *
 * 목적
 * - 매장별 위경도(좌표)를 조회하기 위한 전용 레포지토리.
 * - 설계상 STORES와 1:1 관계(각 store_id 당 1행)를 가정한다.
 *
 * 키/무결성
 * - PK/UK: STORE_ID (String). 무결성을 위해 STORE_LOCATION.STORE_ID는 유니크여야 한다.
 * - 데이터가 없을 수 있으므로 Optional로 반환하며, 서비스 레이어에서 null 처리/예외 처리한다.
 *
 * 주요 사용처
 * - StoreService.getStoreLocation(storeId): 단건 좌표 조회(목록/상세 응답에 좌표 주입).
 * - StoreController:
 *   - GET /api/stores/{id}/location: 좌표 전용 API 응답 구성.
 *   - 목록/상세 응답에서 위경도 필드 채움(서비스를 통해 조회).
 *
 * 성능 가이드
 * - 단건 조회(findByStoreId)는 PK/UK 인덱스 기반으로 O(1)에 가깝게 동작.
 * - 대량 목록에서 N+1을 피하려면 필요한 경우 배치 메서드(예: findAllByStoreIdIn(Collection<String> ids)) 추가를 검토.
 *
 * 확장 포인트(예시)
 * - findAllByStoreIdIn(Collection<String> ids): 여러 매장의 좌표를 한 번에 조회해 Map으로 변환하여 사용.
 *
 * 주의
 * - 좌표 누락 시(없음): 컨트롤러는 404/적절한 에러를 반환하거나 프런트에 “위치 정보 없음”을 표시하도록 처리한다.
 */
public interface StoreLocationRepository extends JpaRepository<StoreLocation, String> {

    Optional<StoreLocation> findByStoreId(String storeId);
}
