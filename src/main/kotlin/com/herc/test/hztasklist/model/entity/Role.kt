package com.herc.test.hztasklist.model.entity

import com.herc.test.hztasklist.model.ERole
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "roles")
class Role(
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    val name: ERole = ERole.ROLE_USER,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
)
