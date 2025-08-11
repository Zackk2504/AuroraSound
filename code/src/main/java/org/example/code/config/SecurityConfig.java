package org.example.code.config;


import org.example.code.model.KhachHang;

import org.example.code.model.NhanVien;
import org.example.code.repo.KhachHangRepository;
import org.example.code.repo.NhanVienRepository;
import org.example.code.service.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    CustomOAuth2UserService customOAuth2UserService;
    @Autowired
    KhachHangRepository khachHangRepository;
    @Autowired
    NhanVienRepository nhanVienRepository;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    @Order(1)
    public SecurityFilterChain nhanVienSecurity(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .securityMatcher("/admin/**", "/ban-hang-tai-quay/**", "/nhan-vien/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/nhan-vien/login").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/ban-hang-tai-quay/**","nhan-vien/**").hasAnyRole("ADMIN", "EMPLOYEE")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/nhan-vien/login")
                        .defaultSuccessUrl("/nhan-vien/home", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/nhan-vien/logout")
                        .logoutSuccessUrl("/nhan-vien/login")
                );

        return http.build();
    }
    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
//                .securityMatcher("/khach-hang/**") // hoặc "/khach-hang/**", "/index", "/san-pham/**"...
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/api/**","/verify/**","/register/**","/", "/index", "/login", "/oauth2/**","/error","/login/oauth2/**","/images/**","/shipping/**").permitAll()
                                .requestMatchers("/khach-hang/**").hasRole("USER")
//                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/khach-hang/login")
                        .defaultSuccessUrl("/index", true)
                        .permitAll()
                ).oauth2Login(oauth -> oauth
                        .loginPage("/login")
                       .userInfoEndpoint(userInfo -> userInfo
                        .userService(customOAuth2UserService) // Bắt buộc dùng cái custom để tạo user mới
                        .userAuthoritiesMapper(authorities -> {
                            Set<GrantedAuthority> mapped = new HashSet<>();
                            mapped.add(new SimpleGrantedAuthority("ROLE_USER")); // Gán tay
                            return mapped;
                        })
                    )
                        .successHandler((request, response, authentication) -> {
                            response.sendRedirect("/oauth2/success");
                        })
                )
                .logout(logout -> logout
                        .logoutUrl("/khach-hang/logout") // URL để logout
                        .logoutSuccessUrl("/index") // Sau khi logout sẽ chuyển về đây
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                )
               ;

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            // Ưu tiên tìm nhân viên trước
            Optional<NhanVien> nvOpt = nhanVienRepository.findByTenDangNhap(username);
            if (nvOpt.isPresent()) {
                NhanVien nv = nvOpt.get();
                String role = nv.getVaiTro().getTenVaiTro().equalsIgnoreCase("admin") ? "ADMIN" : "EMPLOYEE";

                return User.withUsername(nv.getTenDangNhap())
                        .password(nv.getMatKhau())
                        .roles(role)
                        .build();
            }

            // Nếu không phải nhân viên thì tìm khách hàng
            Optional<KhachHang> khOpt = khachHangRepository.findByTenDangNhap(username);
            if (khOpt.isPresent()) {
                KhachHang kh = khOpt.get();
                return User.withUsername(kh.getTenDangNhap())
                        .password(kh.getMatKhau())
                        .roles("USER")
                        .build();
            }

            throw new UsernameNotFoundException("Không tìm thấy người dùng");
        };
    }


}



