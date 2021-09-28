package com.example.form.demo.account.controller

import com.example.form.demo.account.Account
import com.example.form.demo.account.CurrentUser
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import java.security.Principal

@Controller
class AdminController {

    @GetMapping("/")
    fun index(model: Model, @CurrentUser account: Account?): String? {
        if (account == null) {
            model.addAttribute("message", "Hello Spring Security")
        } else {
            model.addAttribute("message", "Hello " + account.userName)
        }
        return "index"
    }
    @GetMapping("/admin")
    fun admin(model: Model, principal: Principal): String? {
        model.addAttribute("message", "Hello admin, " + principal.name)
        return "admin"
    }

    @GetMapping("/user")
    fun user(model: Model, principal: Principal): String? {
        model.addAttribute("message", "Hello" + principal.name)
        return "user"
    }
}
