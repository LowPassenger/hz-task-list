package com.herc.test.hztasklist.model.entity

import jakarta.persistence.*

@Entity(name = "refresh_token")
class RefreshToken(
    @Column(nullable = false, unique = true)
    var token: String? = null,

    @Column(name = "counter", nullable = false)
    var counter: Int = 0,

    @Column(name = "timestamp", nullable = false)
    var timeStamp: Long = 0,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
)
