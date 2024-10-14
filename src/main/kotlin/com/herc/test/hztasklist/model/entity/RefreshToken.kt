package com.herc.test.hztasklist.model.entity

import jakarta.persistence.*

@Entity(name = "refresh_token")
class RefreshToken(
    @Column(nullable = false, unique = true)
    var token: String? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
)
