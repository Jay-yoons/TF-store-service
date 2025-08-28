// Java
// 파일: src/main/java/com/example/store/service/repository/ReviewRepository.java
package com.example.store.service.repository;

import com.example.store.service.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * 리뷰 엔티티용 JPA 레포지토리.
 *
 * 설계 메모
 * - 엔티티: Review (PK: Long reviewId)
 * - 유니크 제약(서비스/DDL): 동일 사용자(USER_ID)가 동일 매장(STORE_ID)에 하나의 리뷰만 작성 가능
 * - 인덱스 권장: STORE_ID, USER_ID 각각 인덱싱하여 목록/내 리뷰 조회 최적화
 *
 * 트랜잭션/성능 가이드
 * - 목록은 페이징(Pageable)·정렬(Sort)을 붙여 확장하는 것을 권장 (예: 최신 작성순 등)
 * - 평균 평점 계산은 서비스/쿼리에서 집계 함수로 처리하거나 캐시/물리화 고려
 */
public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * 특정 매장 리뷰 전체 조회.
     * - 이 메서드는 storeId만으로 필터링하며, DB 담당자가 인덱스를 권장한 용도와 일치합니다.
     *
     * @param storeId 매장 식별자
     * @return 리뷰 목록
     */
    List<Review> findByStoreId(String storeId);

    /**
     * 특정 사용자가 작성한 리뷰 전체 조회.
     *
     * @param userId 사용자 식별자
     * @return 리뷰 목록
     */
    List<Review> findByUserId(String userId);

    /**
     * 특정 매장에 대한 사용자 본인의 단일 리뷰 조회.
     * - 리뷰 중복 작성 방지 체크에 사용됩니다.
     *
     * @param storeId 매장 식별자
     * @param userId  사용자 식별자
     * @return 존재 시 Optional<Review>, 없으면 Optional.empty()
     */
    Optional<Review> findByStoreIdAndUserId(String storeId, String userId);

    /**
     * 특정 사용자-매장 조합의 리뷰 존재 여부 확인 (성능 최적화).
     * - DB에 해당 레코드가 존재하는지만 효율적으로 확인합니다.
     *
     * @param storeId 매장 식별자
     * @param userId  사용자 식별자
     * @return 존재하면 true, 없으면 false
     */
    boolean existsByStoreIdAndUserId(String storeId, String userId);

    /**
     * 리뷰 내용/점수 수정 (작성자 본인 조건 포함).
     * - 영향받은 행 수를 반환(0이면 권한 없음 또는 대상 없음)
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Review r set r.comment = :comment, r.score = :score " +
            "where r.reviewId = :reviewId and r.userId = :userId")
    int updateContentAndScoreByReviewIdAndUserId(@Param("reviewId") Long reviewId,
                                                 @Param("userId") String userId,
                                                 @Param("comment") String comment,
                                                 @Param("score") Integer score);

    /**
     * 리뷰 삭제 (작성자 본인 조건 포함).
     * - 영향받은 행 수를 반환(0이면 권한 없음 또는 대상 없음)
     */
    @Modifying
    @Query("delete from Review r where r.reviewId = :reviewId and r.userId = :userId")
    int deleteByReviewIdAndUserId(@Param("reviewId") Long reviewId, @Param("userId") String userId);
}