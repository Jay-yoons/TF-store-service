package com.example.store.service.repository;

import com.example.store.service.entity.FavStore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 즐겨찾기 리포지토리(JPA).
 * 설계 메모
 * - 엔티티: FavStore (PK: Long favStoreId)
 * - 유니크 제약: (STORE_ID, USER_ID) 한 사용자 당 하나의 매장에 대해 1개의 즐겨찾기만 허용
 * - 인덱스 권장: USER_ID, STORE_ID 각각에 인덱스를 두면 조회 성능 향상
 * - 비정규화: STORE_NAME 컬럼을 함께 저장하여 목록 표시 시 조인 최소화(정책에 따라 갱신/스냅샷)
 *
 * 트랜잭션/성능 가이드
 * - 다량 조회는 페이징(Pageable) 사용을 권장 (findByUserId(Pageable) 시그니처로 확장 가능)
 * - 중복 여부 체크는 existsBy... 메소드 사용으로 성능 최적화
 *
 * 사용 예시
 * - 생성 전 중복 검사: existsByStoreIdAndUserId(storeId, userId)
 * - 목록 화면: findByUserId(userId)
 */
public interface FavStoreRepository extends JpaRepository<FavStore, Long> {

    /**
     * 동일 사용자-가게 조합 존재 여부 조회 (성능 최적화).
     * - DB에 해당 레코드가 존재하는지만 효율적으로 확인합니다.
     *
     * @param storeId 매장 식별자
     * @param userId  사용자 식별자
     * @return 존재하면 true, 없으면 false
     */
    boolean existsByStoreIdAndUserId(String storeId, String userId);

    /**
     * 사용자별 즐겨찾기 목록 조회.
     * - JWT의 sub 값을 userId로 사용
     */
    List<FavStore> findByUserId(String userId);

    /**
     * 동일 사용자-가게 조합으로 삭제.
     * @return 삭제된 행 수
     */
    long deleteByStoreIdAndUserId(String storeId, String userId);
}