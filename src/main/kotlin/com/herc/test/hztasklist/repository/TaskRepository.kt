package com.herc.test.hztasklist.repository

import com.herc.test.hztasklist.model.entity.Task
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.Optional

interface TaskRepository : JpaRepository<Task, Long> {
    override fun findById(id: Long) : Optional<Task>

    override fun existsById(id: Long) : Boolean

    @Query("SELECT t FROM Task t WHERE t.user.id = :userId")
    fun findAllByUserId(@Param("userId") userId: Long): List<Task>

    @Query("SELECT t FROM Task t WHERE t.user.id = :userId AND t.isComplete = true")
    fun findCompletedTasksByUserId(@Param("userId") userId: Long): List<Task>

    @Query("SELECT t FROM Task t WHERE t.user.id = :userId AND t.isComplete = false")
    fun findUncompletedTasksByUserId(@Param("userId") userId: Long): List<Task>

    fun countByIsCompleteTrue() : Long

    fun countByIsCompleteFalse() : Long

}