package com.example.store.service.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FavStoreDto {
    private String storeId;
    private String userId;
    private String storeName;
}
