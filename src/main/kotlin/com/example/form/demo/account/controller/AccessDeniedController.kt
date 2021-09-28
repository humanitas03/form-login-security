package com.example.form.demo.account.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import java.security.Principal

@Controller
class AccessDeniedController {
    @GetMapping("/access-denied")
    fun accessDenied(principal: Principal, model: Model): String = model.addAttribute("name", principal.name).run { "access-denied" }

    @GetMapping("/user-not-found")
    fun userNotFound(principal: Principal, model: Model): String = model.addAttribute("name", principal.name).run { "user-not-found" }
}
