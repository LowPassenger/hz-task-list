package com.herc.test.hztasklist.model.db

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Entity
@Table(
    name = "users",
    uniqueConstraints = [UniqueConstraint(columnNames = ["email"])])
class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @field:NotBlank
    @field:Size(max = 50)
    @field:Email
    val email: String = "",

    @field:NotBlank
    var password: String = "",

    var fcmToken: String? = null,

    @OneToOne(cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
    @JoinColumn(name = "refresh_token_id")
    var refreshToken: RefreshToken? = null,

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_roles",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    var roles: Set<Role> = HashSet(),

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_tasks",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "task_id")]
    )
    var tasks: List<Task> = ArrayList()
)

{
    fun requiredId(): Long {
        return id ?: throw IllegalStateException("Id is null")
    }
}
