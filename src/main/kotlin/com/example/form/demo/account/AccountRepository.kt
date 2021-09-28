package com.example.form.demo.account

import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepository : JpaRepository<Account, Long> {
    fun findByUserName(userName: String): Account?
}
