package com.project.configurations;

import com.project.filters.JwtTokenFilter;
import com.project.models.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;

import static org.springframework.http.HttpMethod.*;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableWebMvc
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenFilter jwtTokenFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()  // Thêm cấu hình CORS vào đây
                .csrf().disable()
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(POST, "/api/auth/login").permitAll()
                //Api Users
                .antMatchers(GET, "/api/users/**").permitAll()
                .antMatchers(POST, "/api/users/search").permitAll()
                .antMatchers(POST, "/api/users/").hasRole(Role.ADMIN)
                .antMatchers(POST, "/api/users/change-password").hasAnyRole(Role.ADMIN, Role.DOCTOR)
                .antMatchers(PUT, "/api/users/**").hasAnyRole(Role.ADMIN, Role.DOCTOR)
                .antMatchers(DELETE, "/api/users/**").hasRole(Role.ADMIN)
                //Api Majors
                .antMatchers(GET, "/api/majors/**").permitAll()
                .antMatchers(POST, "/api/majors/").hasRole(Role.ADMIN)
                .antMatchers(PUT, "/api/majors/**").hasRole(Role.ADMIN)
                .antMatchers(DELETE, "/api/majors/**").hasRole(Role.ADMIN)
                //Api Bookings
                .antMatchers(GET, "/api/bookings/confirm/{id}").permitAll()
                .antMatchers(GET, "/api/bookings/").hasAnyRole(Role.ADMIN, Role.DOCTOR)
                .antMatchers(POST, "/api/bookings/**").permitAll()
                .antMatchers(PUT, "/api/bookings/**").hasRole(Role.DOCTOR)
                //Api Time
                .antMatchers(POST, "/api/times/search").permitAll()
                .anyRequest().authenticated();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.setAllowedHeaders(Arrays.asList(
                HttpHeaders.AUTHORIZATION,
                HttpHeaders.CONTENT_TYPE,
                HttpHeaders.ACCEPT
        ));
        config.setAllowedMethods(Arrays.asList(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.DELETE.name(),
                HttpMethod.OPTIONS.name()
        ));
        config.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
