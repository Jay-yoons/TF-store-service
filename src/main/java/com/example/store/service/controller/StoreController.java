package com.example.store.service.controller;

import com.example.store.service.dto.StoreResponseWithLL;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import com.example.store.service.entity.Store;
import com.example.store.service.service.StoreService;
import com.example.store.service.service.StoreImageService;
import com.example.store.service.dto.StoreResponse;
import com.example.store.service.dto.ReviewDto;
import com.example.store.service.dto.CreateReviewRequestDto;
import com.example.store.service.service.ReviewService;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

/**
 * 스토어 관련 REST API 엔드포인트 집합.
 * - 목록/상세 조회
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

    //가게 이름 가져오기 - 추가
    @GetMapping("/{storeId}/name")
    public String getStoreName(@PathVariable String storeId) {
        log.info("가게 이름 컨트롤러, 가게id={}", storeId);
        return service.getStoreName(storeId);
    }

    /** 가게 목록 API (옵션: categoryCode로 필터링) */
    @GetMapping
    public List<StoreResponseWithLL> listStores(@RequestParam(value = "categoryCode", required = false) Integer categoryCode) {
        log.info("가게 목록 컨트롤러");
        return service.getStoresByCategoryCode(categoryCode).stream()
                .map(store -> {
                    StoreResponseWithLL r = StoreResponseWithLL.fromEntity(store);
                    r.setImageUrl(imageService.getImageUrl(r.getStoreId()));
                    // 위경도: STORE_LOCATION에서 조회
                    com.example.store.service.entity.StoreLocation loc = service.getStoreLocation(r.getStoreId());
                    if (loc != null) {
                        r.setLongitude(loc.getLongitude());
                        r.setLatitude(loc.getLatitude());
                    }
                    // 영업 상태 세팅
                    r.setOpenNow(service.isOpenNow(store));
                    r.setOpenStatus(service.openStatus(store));
                    return r;
                })
                .toList();
    }

    /** 가게 상세 API */
    @GetMapping("/{storeId}")
    public StoreResponseWithLL storeDetail(@PathVariable String storeId) {
        log.info("가게 상세 컨트롤러");
        Store store = service.getStore(storeId);
        StoreResponseWithLL response = StoreResponseWithLL.fromEntity(store);
        response.setImageUrl(imageService.getImageUrl(response.getStoreId()));
        // 위경도: STORE_LOCATION에서 조회
        com.example.store.service.entity.StoreLocation loc = service.getStoreLocation(response.getStoreId());
        if (loc != null) {
            response.setLongitude(loc.getLongitude());
            response.setLatitude(loc.getLatitude());
        }
        // 영업 상태 세팅
        response.setOpenNow(service.isOpenNow(store));
        response.setOpenStatus(service.openStatus(store));
        return response;
    }

    /** 가게 위치(위경도) 전용 API */
    @GetMapping("/{storeId}/location")
    public Map<String, String> getStoreLocation(@PathVariable String storeId) {
        log.info("위경도 컨트롤러");
        com.example.store.service.entity.StoreLocation loc = service.getStoreLocation(storeId);
        if (loc == null) {
            throw new IllegalArgumentException("위치 정보가 없습니다.");
        }
        return Map.of(
                "latitude", loc.getLatitude(),
                "longitude", loc.getLongitude()
        );
    }

    /** 가게 목록을 카테고리명(한식/일식/양식/중식/카페)으로 그룹핑하여 반환 */
    @GetMapping("/group-by-category")
    public Map<String, List<StoreResponse>> groupByCategory() {
        List<Store> stores = service.getAllStores(); // 전체 스토어 조회

        return stores.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        store -> store.getCategory() != null ? store.getCategory().getKoreanName() : "기타", // 변경
                        java.util.stream.Collectors.mapping(
                                store -> {
                                    StoreResponse r = StoreResponse.fromEntity(store);
                                    r.setImageUrl(imageService.getImageUrl(r.getStoreId()));
                                    r.setOpenNow(service.isOpenNow(store));
                                    r.setOpenStatus(service.openStatus(store));
                                    return r;
                                },
                                java.util.stream.Collectors.toList()
                        )
                ));
    }

    /** [별칭] 가게 리뷰 목록 (설계안 호환: GET /stores/{storeId}/reviews) */
    @GetMapping("/{storeId}/reviews")
    public List<ReviewDto> storeReviewsAlias(@PathVariable String storeId) {
        return reviewService.getStoreReviews(storeId);
    }

    /** [별칭] 가게 리뷰 작성 (설계안 호환: POST /stores/{storeId}/reviews) */
    @PostMapping("/{storeId}/reviews")
    public ReviewDto createStoreReviewAlias(@PathVariable String storeId,
                                            @RequestBody CreateReviewRequestDto dto) {
        // 경로에서 받은 storeId를 DTO에 세팅하여 재사용
        dto.setStoreId(storeId);
        return reviewService.createReview(dto);
    }
}
