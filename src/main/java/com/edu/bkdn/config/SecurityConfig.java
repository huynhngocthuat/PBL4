package com.edu.bkdn.config;

import com.edu.bkdn.services.UserDetailServiceImp;
import com.edu.bkdn.services.UserService;
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
    UserDetailServiceImp userDetailServiceImp;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailServiceImp).passwordEncoder(getBCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //Cross Site Request Forgery
        http.csrf().disable();
        //permit all
        http.authorizeRequests().antMatchers("/login").permitAll();
        //permit client
        http.authorizeRequests().antMatchers("/**").access("hasRole('ROLE_CLIENT')");

        http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/");

        http.authorizeRequests().and().formLogin()
                .loginProcessingUrl("/j_spring_security_check")
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .failureUrl("/login?error=true")
                .usernameParameter("phoneNumber")
                .passwordParameter("password");
    }

    @Override
    public void configure(WebSecurity web){
        web
            .ignoring()
            .antMatchers("/resources/**", "/static/**",
                    "/css/**", "/js/**", "/images/**","/template/**");
    }
}