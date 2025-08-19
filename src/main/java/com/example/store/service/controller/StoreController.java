package com.example.store.service.controller;

import com.example.store.service.dto.StoreResponseWithLL;
import com.example.store.service.entity.StoreSeat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import com.example.store.service.entity.Store;
import com.example.store.service.service.StoreService;
import com.example.store.service.service.StoreImageService;
import com.example.store.service.dto.StoreResponse;
import com.example.store.service.dto.ReviewDto;
import com.example.store.service.dto.ReviewRequestDto;
import com.example.store.service.service.ReviewService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.Map;

/**
 * 스토어 관련 REST API 엔드포인트 집합.
 * - 목록/상세/여유 좌석 조회
 * - 좌석 직접 세팅 및 예약 연동을 위한 증감 API 제공
 */
@RestController
@RequestMapping("/api/stores")
@Slf4j
public class StoreController {

    private final StoreService service;
    private final StoreImageService imageService;
    private final ReviewService reviewService;

    public StoreController(StoreService service, StoreImageService imageService, ReviewService reviewService) {
        this.service = service;
        this.imageService = imageService;
        this.reviewService = reviewService;
    }

    /** 가게 목록 API (옵션: categoryCode로 필터링) */
    @GetMapping
    public List<StoreResponseWithLL> listStores(@RequestParam(value = "categoryCode", required = false) Integer categoryCode) {
        log.info("가게 목록 컨트롤러");
        return service.getStoresByCategoryCode(categoryCode).stream()
                .map(store -> {
                    StoreResponseWithLL r = StoreResponseWithLL.fromEntity(store);
                    r.setImageUrl(imageService.getImageUrl(r.getStoreId()));
                    r.setImageUrls(imageService.listImageUrls(r.getStoreId(), 10));
                    // 영업 상태 세팅
                    r.setOpenNow(service.isOpenNow(store));
                    r.setOpenStatus(service.openStatus(store));
                    return r;
                })
                .toList();
    }

    /** 가게 상세 API */
    @GetMapping("/{storeId}")
    public StoreResponse storeDetail(@PathVariable String storeId) {
        log.info("가게 상세 컨트롤러");
        Store store = service.getStore(storeId);
        int availableSeats = service.getAvailableSeats(storeId);
        StoreResponse response = StoreResponse.fromEntityWithSeats(store, availableSeats);
        response.setImageUrl(imageService.getImageUrl(response.getStoreId()));
        response.setImageUrls(imageService.listImageUrls(response.getStoreId(), 10));
        // 영업 상태 세팅
        response.setOpenNow(service.isOpenNow(store));
        response.setOpenStatus(service.openStatus(store));
        return response;
    }

    /** 가게 위치(위경도) 전용 API */
    @GetMapping("/{storeId}/location")
    public Map<String, String> getStoreLocation(@PathVariable String storeId) {
        log.info("위경도 컨트롤러");
        Store store = service.getStore(storeId);
        return Map.of(
                "latitude", store.getLatitude(),
                "longitude", store.getLongitude()
        );
    }

    /** Booking Service에서 호출할 REST API (좌석 정보 업데이트) */
    @PutMapping("/{storeId}/seats")
    public String updateSeatInfo(@PathVariable String storeId, @RequestParam int inUsingSeat) {
        StoreSeat storeSeat = service.getStoreSeatInfo(storeId);
        storeSeat.setInUsingSeat(inUsingSeat);
        service.saveStoreSeat(storeSeat);
        return "좌석 정보가 업데이트되었습니다.";
    }

    /** Booking Service에서 호출할 REST API (여유 좌석 조회) */
    @GetMapping("/{storeId}/available-seats")
    public int getAvailableSeats(@PathVariable String storeId) {
        return service.getAvailableSeats(storeId);
    }

    /** 가게 목록을 카테고리명(한식/일식/양식/중식/카페)으로 그룹핑하여 반환 */
    @GetMapping("/group-by-category")
    public Map<String, List<StoreResponse>> groupByCategory() {
        return service.groupStoresByKoreanCategoryName().entrySet().stream()
                .collect(java.util.stream.Collectors.toMap(
                        java.util.Map.Entry::getKey,
                        e -> e.getValue().stream().map(store -> {
                                    StoreResponse r = StoreResponse.fromEntity(store);
                                    r.setImageUrl(imageService.getImageUrl(r.getStoreId()));
                                    r.setImageUrls(imageService.listImageUrls(r.getStoreId(), 10));
                                    r.setOpenNow(service.isOpenNow(store));
                                    r.setOpenStatus(service.openStatus(store));
                                    return r;
                                })
                                .toList()
                ));
    }

    /** 예약 확정 시: 사용중 좌석 증가 */
    @PostMapping("/{storeId}/seats/increment")
    public int incrementInUsingSeat(@PathVariable String storeId, @RequestParam(defaultValue = "1") int count) {
        return service.incrementInUsingSeat(storeId, count);
    }

    /** 예약 취소 시: 사용중 좌석 감소 */
    @PostMapping("/{storeId}/seats/decrement")
    public int decrementInUsingSeat(@PathVariable String storeId, @RequestParam(defaultValue = "1") int count) {
        return service.decrementInUsingSeat(storeId, count);
    }

    /** [별칭] 가게 리뷰 목록 (설계안 호환: GET /stores/{storeId}/reviews) */
    @GetMapping("/{storeId}/reviews")
    public List<ReviewDto> storeReviewsAlias(@PathVariable String storeId) {
        return reviewService.getStoreReviews(storeId);
    }

    /** [별칭] 가게 리뷰 작성 (설계안 호환: POST /stores/{storeId}/reviews) */
    @PostMapping("/{storeId}/reviews")
    public ReviewDto createStoreReviewAlias(@PathVariable String storeId,
                                            @AuthenticationPrincipal Jwt jwt,
                                            @RequestBody ReviewRequestDto dto) {
        String userId = jwt.getClaimAsString("sub");
        // 경로에서 받은 storeId를 DTO에 세팅하여 재사용
        dto.setStoreId(storeId);
        return reviewService.createReview(userId, dto);
    }
}
