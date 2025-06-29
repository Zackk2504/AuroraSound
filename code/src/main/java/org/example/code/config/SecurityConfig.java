package org.example.code.config;


import org.example.code.model.KhachHang;

import org.example.code.repo.KhachHangRepository;
import org.example.code.service.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Optional;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    CustomOAuth2UserService customOAuth2UserService;
    @Autowired
    KhachHangRepository khachHangRepository;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").permitAll()
//                        .anyRequest().authenticated()
                                .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/index", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL để logout
                        .logoutSuccessUrl("/index") // Sau khi logout sẽ chuyển về đây
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/login")
                                .successHandler((request, response, authentication) -> {
                                    response.sendRedirect("/oauth2/success");
                                }
                        )
                );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(KhachHangRepository accountRepository) {
        return username -> {
            Optional<KhachHang> user = accountRepository.findByTenDangNhap(username);
            if (user.isEmpty()) {
                throw new UsernameNotFoundException("User not found with username: " + username);
            }
            return User.withUsername(user.get().getTenDangNhap())
                    .password(user.get().getMatKhau())
                    .build();
        };
    }


}
