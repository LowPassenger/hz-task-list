package com.herc.test.hztasklist.model.db

import com.herc.test.hztasklist.model.EPriority
import jakarta.persistence.*

@Entity
@Table(name = "tasks", indexes = [Index(columnList = "timestamp")])
class Task(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var title: String? = null,
    var timeStamp: Long = 0,
    var expiredTime: Long = 0,
    var isComplete: Boolean = false,

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    val taskPriority: EPriority = EPriority.NORMAL,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User? = null
)