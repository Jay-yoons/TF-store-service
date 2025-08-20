package com.example.store.service.security;

import com.example.store.service.exception.ForbiddenException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

/**
 * 현재 인증된 사용자의 식별자(userId)를 제공하는 컴포넌트.
 * - JWT 기반 인증에서 sub 클레임을 userId로 사용한다.
 */
@Component
public class CurrentUserProvider {

    /**
     * SecurityContext에서 JWT를 찾아 sub 클레임을 반환한다.
     * 인증 정보가 없거나 JWT가 없으면 403을 발생시킨다.
     */
    public String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ForbiddenException("인증되지 않았습니다.");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof Jwt jwt) {
            String sub = jwt.getClaimAsString("sub");
            if (sub == null || sub.isBlank()) {
                throw new ForbiddenException("유효하지 않은 토큰입니다.");
            }
            return sub;
        }

        // 일부 구현체에서 credentials에 Jwt가 들어오는 경우 대비
        Object credentials = authentication.getCredentials();
        if (credentials instanceof Jwt jwtCred) {
            String sub = jwtCred.getClaimAsString("sub");
            if (sub == null || sub.isBlank()) {
                throw new ForbiddenException("유효하지 않은 토큰입니다.");
            }
            return sub;
        }

        throw new ForbiddenException("JWT를 찾을 수 없습니다.");
    }
}


