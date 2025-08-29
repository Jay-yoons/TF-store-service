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
}
