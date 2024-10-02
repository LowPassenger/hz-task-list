package com.herc.test.hztasklist.model.db

import jakarta.persistence.*

@Entity(name = "refreshtoken")
class RefreshToken(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(nullable = false, unique = true)
    val token: String
)
