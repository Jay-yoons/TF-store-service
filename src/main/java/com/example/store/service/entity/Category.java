package com.example.store.service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "CATEGORY")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @Column(name = "CATEGORY_CODE")
    private Integer code;   // DB: CATEGORY_CODE

    @Column(name = "CATEGORY_NAME")
    private String koreanName; // DB: CATEGORY_NAME
}
