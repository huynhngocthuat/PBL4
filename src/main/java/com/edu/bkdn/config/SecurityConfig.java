package com.edu.bkdn.config;

import com.edu.bkdn.models.ApplicationUser;
import com.edu.bkdn.services.ContactService;
import com.edu.bkdn.services.UserService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;

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
    private ContactService contactService;
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
                .antMatchers("/resources/**", "/signup", "/logout", "/login").permitAll()
                .antMatchers("/**").access("hasRole('ROLE_CLIENT')")
                .anyRequest().authenticated();

        http
                .formLogin()
                .loginProcessingUrl("/j_spring_security_check")
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/home")
                .usernameParameter("phoneNumber")
                .passwordParameter("password")
                .failureUrl("/login?error=true")
                .and().logout().logoutSuccessHandler(new SimpleUrlLogoutSuccessHandler() {
            @Override
            @SneakyThrows
            public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                Long id =  ((ApplicationUser)authentication.getPrincipal()).getUser().getId();
                userService.updateActive(id, false);
                contactService.updateActive(id, false);
                super.onLogoutSuccess(httpServletRequest, httpServletResponse, authentication);
            }
        });

        http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/");
    }

}
