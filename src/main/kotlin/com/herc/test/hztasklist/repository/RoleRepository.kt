package com.herc.test.hztasklist.repository

import com.herc.test.hztasklist.model.ERole
import com.herc.test.hztasklist.model.entity.Role
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface RoleRepository : JpaRepository<Role, Long> {
    fun findByName(name: ERole): Optional<Role>
}
