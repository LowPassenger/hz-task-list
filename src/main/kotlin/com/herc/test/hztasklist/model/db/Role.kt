package com.herc.test.hztasklist.model.db

import com.herc.test.hztasklist.model.ERole
import jakarta.persistence.*
import jakarta.persistence.EnumType

@Entity
@Table(name = "roles")
data class Role(
    @Id
    var id: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    val name: ERole
)
