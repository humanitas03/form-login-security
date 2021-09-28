package com.example.form.demo.account

import org.springframework.security.core.annotation.AuthenticationPrincipal
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

@Retention(RetentionPolicy.RUNTIME)
@kotlin.annotation.Target(AnnotationTarget.VALUE_PARAMETER)
@AuthenticationPrincipal(expression = "#this=='anonymousUser' ? null : account")
annotation class CurrentUser
