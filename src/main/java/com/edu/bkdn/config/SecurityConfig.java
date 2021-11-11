package com.edu.bkdn.config;

import com.edu.bkdn.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    BCryptPasswordEncoder getBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private UserService userService;
    @Autowired
    private CustomizeAuthenticationSuccessHandler customizeAuthenticationSuccessHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(getBCryptPasswordEncoder());
    }

    @Override
    public void configure(WebSecurity web){
        web
                .ignoring()
                .antMatchers("/resources/**", "/static/**",
                        "/css/**", "/js/**", "/images/**","/template/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //Cross Site Request Forgery
        http.csrf().disable();
        //permit all
        http
                .authorizeRequests()
                .antMatchers("/resources/**").permitAll()
                .antMatchers("/**").access("hasRole('ROLE_CLIENT')")
                .anyRequest().authenticated();

        http
                .formLogin()
                .loginProcessingUrl("/j_spring_security_check")
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/home")
                .usernameParameter("phoneNumber")
                .passwordParameter("password")
                .failureUrl("/login?error=true");

        http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/");
    }

}
