package com.herc.test.hztasklist.repository

import com.herc.test.hztasklist.model.ERole
import com.herc.test.hztasklist.model.entity.Role
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository : JpaRepository<Role, Long> {
    fun findByRoleName(name: ERole): Role?
}
