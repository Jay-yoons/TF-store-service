package com.example.store.service.dto;

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
