package com.example.form.demo.configuration

import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.server.DefaultServerRedirectStrategy
import org.springframework.security.web.server.ServerRedirectStrategy
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.net.URI

@Component
class LoginFailureHandler constructor(
    private val redirectStrategy: ServerRedirectStrategy = DefaultServerRedirectStrategy()
) : ServerAuthenticationFailureHandler {
    override fun onAuthenticationFailure(
        webFilterExchange: WebFilterExchange?,
        exception: AuthenticationException?
    ): Mono<Void> {
        when (exception) {
            is BadCredentialsException -> return redirectStrategy.sendRedirect(webFilterExchange!!.exchange, URI.create("/login?err=e001"))
            is UsernameNotFoundException -> return redirectStrategy.sendRedirect(webFilterExchange!!.exchange, URI.create("/login?err=e002"))
        }
        return webFilterExchange!!.chain.filter(webFilterExchange!!.exchange)
    }
}
