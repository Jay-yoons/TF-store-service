package com.example.store.service.service;

import com.example.store.service.entity.Store;
import com.example.store.service.repository.StoreRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class StoreImageService {

    private final StoreRepository storeRepository;

    public StoreImageService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    /**
     * 매장 단일 이미지 URL 조회
     */
    public String getImageUrl(String storeId) {
        return storeRepository.findById(storeId)
                .map(Store::getImageUrl)
                .orElse(null);
    }

    /**
     * 호환성을 위해 리스트 리턴
     */
    public List<String> listImageUrls(String storeId, int limit) {
        String url = getImageUrl(storeId);
        if (url == null || url.isBlank()) {
            return Collections.emptyList();
        }
        return Collections.singletonList(url);
    }
}
