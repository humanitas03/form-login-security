package com.example.form.demo.account.controller

import com.example.form.demo.account.Account
import com.example.form.demo.account.AccountService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping

@Controller
class SignUpController @Autowired constructor(
    val accountService: AccountService
) {

    @GetMapping("/signup")
    fun signUpForm(model: Model): String {
        return model.addAttribute("account", Account(null, null, null, null)).run { "signup" }
    }

    @PostMapping("/signup")
    fun processSignUp(@ModelAttribute account: Account): String = Account.registerAdminAccount(account).let {
        accountService.createNew(it)
        "redirect:/admin"
    }
}
