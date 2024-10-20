package com.herc.test.hztasklist.security.services

import com.fasterxml.jackson.annotation.JsonIgnore
import com.herc.test.hztasklist.model.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserDetailsImpl(user: User) : UserDetails{
    var id: Long? = null
    val email: String
    val user: User

    @JsonIgnore
    private val password: String
    private var authorities: Collection<GrantedAuthority> = emptyList()

    init {
        this.id = user.id
        this.email = user.email
        this.user = user
        this.password = user.password
        this.authorities = user.roles.map { SimpleGrantedAuthority(it.name.name) }
    }

    override fun getAuthorities(): Collection<GrantedAuthority> = authorities

    override fun getPassword(): String = password

    override fun getUsername(): String = email

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true

    fun getUserFromDetails(): User = user
}
