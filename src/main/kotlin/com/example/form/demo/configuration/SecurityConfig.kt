package com.example.form.demo.configuration

import com.example.form.demo.account.AccountService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDecisionVoter
import org.springframework.security.access.ConfigAttribute
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl
import org.springframework.security.access.vote.RoleHierarchyVoter
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager
import org.springframework.security.authorization.AuthorizationDecision
import org.springframework.security.authorization.ReactiveAuthorizationManager
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler
import org.springframework.security.web.server.authorization.HttpStatusServerAccessDeniedHandler
import reactor.core.publisher.Mono
import java.net.URI
import java.util.* // ktlint-disable no-wildcard-imports

@Configuration
class CustomPasswordEncoder {
    @Bean
    fun passwordEncoder(): PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
}

@Configuration
@EnableWebFluxSecurity
class SecurityConfig @Autowired constructor(
    val loginFailureHandler: LoginFailureHandler,
    val accountService: AccountService,
    @Qualifier("passwordEncoder")
    val passwordEncoder: PasswordEncoder
) {

    @Bean
    fun springSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain? {
        return http
            .csrf { it.disable() }
            .authorizeExchange()
            .pathMatchers("/", "/signup", "/login", "/h2-console/**").permitAll()
            .pathMatchers("/admin").hasRole("ADMIN")
            .pathMatchers("/user").hasAnyRole("USER", "ADMIN")
            .anyExchange().authenticated()
            .and()
            .formLogin()
            .loginPage("/login")
            .authenticationSuccessHandler(RedirectServerAuthenticationSuccessHandler("/user"))
            .authenticationFailureHandler(loginFailureHandler)
            .and()
            .logout()
            .logoutUrl("/logout")
            .logoutSuccessHandler(logoutSuccessHandler("/login"))
            .and()
            .authenticationManager(authenticationManager())
            .exceptionHandling()
            .accessDeniedHandler(HttpStatusServerAccessDeniedHandler(HttpStatus.BAD_REQUEST))
            .and().build()
    }

    @Bean
    fun authenticationManager(): ReactiveAuthenticationManager {
        val authManager = UserDetailsRepositoryReactiveAuthenticationManager(accountService)
        authManager.setPasswordEncoder(passwordEncoder)
        return authManager
    }

    @Bean
    fun roleHierarchy(): RoleHierarchy {
        val roleHierarchy = RoleHierarchyImpl()
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER")
        return roleHierarchy
    }

    private fun logoutSuccessHandler(url: String): ServerLogoutSuccessHandler {
        val successHandler = RedirectServerLogoutSuccessHandler()
        successHandler.setLogoutSuccessUrl(URI.create(url))
        return successHandler
    }

//    fun expressionHandler(): SecurityExpressionHandler<FilterInvocation> {
//        val roleHierarchy = RoleHierarchyImpl()
//        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER")
//
//        val defaultHandler = DefaultWebSecurityExpressionHandler()
//        defaultHandler.setRoleHierarchy(roleHierarchy)
//
//        return defaultHandler
//    }
}

class CustomReactiveAuthorizationManager<T> constructor(
    val authority: String,
    private val roleHierarchyVoter: RoleHierarchyVoter,
) : ReactiveAuthorizationManager<T> {
    override fun check(authentication: Mono<Authentication>?, unit: T): Mono<AuthorizationDecision> {
        return authentication!!.map<AuthorizationDecision?> {
            val ca = ConfigAttribute { authority }
            val voteResult = roleHierarchyVoter.vote(it, unit, Collections.singletonList(ca))
            val isAuthorized = voteResult == AccessDecisionVoter.ACCESS_GRANTED
            AuthorizationDecision(isAuthorized)
        }.defaultIfEmpty(AuthorizationDecision(false))
            .doOnError { println("An error occured voting decision") }
    }
}
