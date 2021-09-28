package com.example.form.demo.account

import org.springframework.security.crypto.password.PasswordEncoder
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Account(

    @Id @GeneratedValue
    val id: Long?,

    @Column(unique = true)
    val userName: String? = null,

    val password: String? = null,

    val role: String? = null,
) {
    companion object {
        fun encodedPasswordAccount(
            account: Account,
            passwordEncoder: PasswordEncoder
        ): Account {
            return Account(
                account.id,
                account.userName,
                passwordEncoder.encode(account.password),
                account.role
            )
        }

        fun registerAdminAccount(
            account: Account
        ): Account {
            return Account(
                account.id,
                account.userName,
                account.password,
                "ADMIN"
            )
        }
    }
}
