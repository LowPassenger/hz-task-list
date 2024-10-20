package com.herc.test.hztasklist.model.entity

import com.herc.test.hztasklist.model.EPriority
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Entity
@Table(name = "tasks", indexes = [Index(columnList = "expiredTime")])
class Task(
    @field:NotBlank
    @field:Size(max = 80)
    var title: String? = null,

    @field:Size(max = 300)
    var description: String? = null,

    @Column(name = "timestamp", nullable = false)
    var timeStamp: Long = 0,

    @Column(name = "expired", nullable = false)
    var expiredTime: Long = 0,

    @Column(name = "iscomplete", nullable = false)
    var isComplete: Boolean = false,

    @Enumerated(EnumType.STRING)
    @Column(name = "taskpriority", length = 10)
    var taskPriority: EPriority = EPriority.NORMAL,

    @OneToOne
    @JoinColumn(name = "user_id")
    var user: User? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
)