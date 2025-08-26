package com.example.store.service.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * STORES_LOCATION 테이블 매핑 엔티티.
 * - PK/FK: STORE_ID (STORES 참조)
 * - 주소: STORE_LOCATION
 * - 위경도: LONGITUDE, LATITUDE
 */
@Entity
@Table(name = "STORES_LOCATION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreLocation {

    @Id
    @Column(name = "STORE_ID", length = 20, nullable = false)
    private String storeId;

    @Column(name = "STORE_LOCATION")
    private String address;   // ✅ DB: STORE_LOCATION → address 필드에 매핑

    @Column(name = "LONGITUDE", length = 50)
    private String longitude;

    @Column(name = "LATITUDE", length = 50)
    private String latitude;

    @OneToOne(mappedBy = "storeLocationEntity")
    private Store store;
}
