package com.herc.test.hztasklist.model.entity

import jakarta.persistence.*

@Entity(name = "refreshtoken")
class RefreshToken(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(nullable = false, unique = true)
    var token: String? = null
)
