package com.example.form.demo.account

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class AccountService @Autowired constructor(
    val accountRepository: AccountRepository,
    @Qualifier("passwordEncoder")
    val passwordEncoder: PasswordEncoder
) : ReactiveUserDetailsService {

    override fun findByUsername(username: String?): Mono<UserDetails> {
        val account = accountRepository.findByUserName(username ?: throw NoSuchElementException())
        return Mono.just(UserAccount(account ?: throw UsernameNotFoundException(username)))
    }

    fun createNew(account: Account): Account = accountRepository.save(Account.encodedPasswordAccount(account, passwordEncoder))
}
