package com.example.trainingsite.security;


import com.example.trainingsite.entity.User;
import com.example.trainingsite.repository.UserRepo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserRepo userRepo;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepo userRepo){
        return username -> {
            User user = userRepo.findByUsername(username);
            if(user != null){
                return user;
            }
            throw new UsernameNotFoundException("User - "+username+" not found");
        };
    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        return httpSecurity.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
//            authorizationManagerRequestMatcherRegistry.requestMatchers("/news/**").hasRole("ADMIN")
//                    .requestMatchers("/content/**").authenticated()
//                    .requestMatchers("/api/**").authenticated()
//                    .requestMatchers("/**").permitAll();
//        }).formLogin(httpSecurityFormLoginConfigurer -> {
//            httpSecurityFormLoginConfigurer.loginPage("/")
//                    .loginProcessingUrl("/authenticate").successHandler(new AuthenticationSuccessHandler() {
//                        @Override
//                        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//                            userRepo.updateStatusByLogin(((User) authentication.getPrincipal()).getUsername(), User.Status.ONLINE);
//                            response.setContentType("application/json");
//                            response.getWriter().write("{\"redirectUrl\": \"/\"}");
//                            response.getWriter().flush(); // Перенаправлення користувача на головну сторінку після успішної аутентифікації
//                        }
//                    }).failureHandler(new AuthenticationFailureHandler() {
//                        @Override
//                        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
//                                                            AuthenticationException exception) throws IOException, ServletException {
//                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Встановлюємо статус 401
//                            response.getWriter().write("Invalid credentials");
//                        }
//                    });
//        }).exceptionHandling(exceptionHandling ->
//                exceptionHandling
//                        .authenticationEntryPoint((request, response, authException) -> {
//                            response.sendRedirect("/?error=access-denied");
//                        })
//        ).logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer.logoutUrl("/logout").
//                logoutSuccessUrl("/").addLogoutHandler(new LogoutHandler() {
//                    @Override
//                    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
//                        userRepo.updateStatusByLogin(((User) authentication.getPrincipal()).getUsername(), User.Status.OFFLINE);
//                    }
//                })).build();
//
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
            authorizationManagerRequestMatcherRegistry.requestMatchers("/news/**").hasRole("ADMIN")
                    .requestMatchers("/content/**").authenticated()
                    .requestMatchers("/api/**").authenticated()
                    .requestMatchers("/**").permitAll();
        }).formLogin(httpSecurityFormLoginConfigurer -> {
            httpSecurityFormLoginConfigurer.loginPage("/")
                    .loginProcessingUrl("/authenticate").successHandler(new AuthenticationSuccessHandler() {
                        @Override
                        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                            response.setContentType("application/json");
                            response.getWriter().write("{\"redirectUrl\": \"/\"}");
                            response.getWriter().flush(); // Перенаправлення користувача на головну сторінку після успішної аутентифікації
                        }
                    }).failureHandler(new AuthenticationFailureHandler() {
                        @Override
                        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                                            AuthenticationException exception) throws IOException, ServletException {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Встановлюємо статус 401
                            response.getWriter().write("Invalid credentials");
                        }
                    });
        }).exceptionHandling(exceptionHandling ->
                exceptionHandling
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendRedirect("/?error=access-denied");
                        })
        ).logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer.logoutUrl("/logout").
                logoutSuccessUrl("/")).build();

    }
}
