package com.example.preordering.repository;

import com.example.preordering.entity.SessionToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SessionTokenRepository extends JpaRepository<SessionToken, Long> {
    @Query(
            "SELECT s FROM SessionToken s WHERE s.sessionToken = ?1"
    )
    SessionToken getEmailOrUsernamebySessionToken(String sessionToken);
}
