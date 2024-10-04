package com.herc.test.hztasklist.model.db

import com.herc.test.hztasklist.model.ERole
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "roles")
class Role(
    @Id
    var id: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    val name: ERole = ERole.ROLE_USER
)
