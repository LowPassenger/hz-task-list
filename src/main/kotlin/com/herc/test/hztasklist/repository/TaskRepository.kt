package com.herc.test.hztasklist.repository

import com.herc.test.hztasklist.model.entity.Task
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface TaskRepository : JpaRepository<Task, Long> {
    override fun findById(id: Long) : Optional<Task?>
}