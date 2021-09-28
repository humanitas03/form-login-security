package com.example.form.demo.account.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class LoginOutController {

    @GetMapping("/login")
    fun loginForm(): String = "login"

    @GetMapping("/logout")
    fun logout(): String = "logout"
}
