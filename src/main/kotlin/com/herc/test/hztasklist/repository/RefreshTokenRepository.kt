package com.herc.test.hztasklist.repository

import com.herc.test.hztasklist.model.entity.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface RefreshTokenRepository : JpaRepository<RefreshToken, Long> {
}
