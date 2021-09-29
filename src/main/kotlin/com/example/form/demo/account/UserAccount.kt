package com.example.form.demo.account

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User

class UserAccount(val account: Account) :
    User(
        account.userName,
        account.password,
        listOf(SimpleGrantedAuthority("ROLE_" + account.role))
    )
