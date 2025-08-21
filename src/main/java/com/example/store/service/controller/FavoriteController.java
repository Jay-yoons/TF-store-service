// Java
// 파일: src/main/java/com/example/store/service/controller/FavoriteController.java
package com.example.store.service.controller;

import com.example.store.service.entity.FavStore;
import com.example.store.service.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 즐겨찾기 API.
 * - POST /api/favorites?storeId=S1   : 추가 (userId는 임시로 하드코딩)
 * - DELETE /api/favorites?storeId=S1 : 제거
 * - GET /api/favorites/status?storeId=S1 : 즐겨찾기 여부 확인
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorites")
@Slf4j
public class FavoriteController {

    private final FavoriteService favoriteService;

    // [생성] 즐겨찾기 추가 - userId는 임시로 하드코딩
    @PostMapping
    public FavStore add(@RequestParam String storeId) {
        // TODO: JWT 인증 구현 시 실제 userId 추출 로직으로 교체
        String userId = "temp-user-id-123";
        return favoriteService.addFavorite(userId, storeId);
    }

    // [삭제] 즐겨찾기 제거
    @DeleteMapping
    public void remove(@RequestParam String storeId) {
        // TODO: JWT 인증 구현 시 실제 userId 추출 로직으로 교체
        String userId = "temp-user-id-123";
        favoriteService.removeFavorite(userId, storeId);
    }

    // [조회] 즐겨찾기 여부(단건)
    @GetMapping("/status")
    public java.util.Map<String, Boolean> isFavorite(@RequestParam String storeId) {
        log.info("즐겨찾기 여부 컨트롤러");
        // TODO: JWT 인증 구현 시 실제 userId 추출 로직으로 교체
        String userId = "temp-user-id-123";
        boolean val = favoriteService.isFavorite(userId, storeId);
        return java.util.Collections.singletonMap("isFavorite", val);
    }

    // [조회] 내 즐겨찾기 목록
    @GetMapping("/me")
    public List<FavStore> myFavorites() {
        // TODO: JWT 인증 구현 시 실제 userId 추출 로직으로 교체
        String userId = "temp-user-id-123";
        return favoriteService.listFavorites(userId);
    }

    // [조회 - 별칭] 내 즐겨찾기 목록 (설계안: GET /favorites)
    @GetMapping
    public List<FavStore> myFavoritesAlias() {
        // TODO: JWT 인증 구현 시 실제 userId 추출 로직으로 교체
        String userId = "temp-user-id-123";
        return favoriteService.listFavorites(userId);
    }
}
