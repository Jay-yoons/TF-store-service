package com.example.store.service.security;

import org.springframework.stereotype.Component;

/**
 * 현재 인증된 사용자의 식별자(userId)를 제공하는 컴포넌트.
 * - 임시로 하드코딩된 userId 반환 (JWT 인증 구현 전까지)
 */
@Component
public class CurrentUserProvider {

    /**
     * 임시로 하드코딩된 userId를 반환한다.
     * TODO: JWT 인증 구현 시 실제 인증 로직으로 교체
     */
    public String getCurrentUserId() {
        // TODO: JWT 인증 구현 시 실제 userId 추출 로직으로 교체
        return "temp-user-id-123";
    }
}


