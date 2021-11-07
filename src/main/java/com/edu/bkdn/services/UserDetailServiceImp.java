package com.edu.bkdn.services;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailServiceImp implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.edu.bkdn.models.User user = userService.findByPhone(username);
        if (user == null) {
            System.out.println("User not found! " + username);
            throw new UsernameNotFoundException("User " + username + " was not found in the database");
        }
        //Client
        List<GrantedAuthority> grant = new ArrayList<>();
        GrantedAuthority authority = new SimpleGrantedAuthority("CLIENT");
        grant.add(authority);
        UserDetails userDetails = (UserDetails) new User(user.getPhone(), user.getPassword(), grant);

        return userDetails;
    }
}
