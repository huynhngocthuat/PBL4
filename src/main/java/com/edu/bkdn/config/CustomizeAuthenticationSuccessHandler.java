package com.edu.bkdn.config;

import com.edu.bkdn.utils.httpResponse.okResponse;
import com.google.gson.Gson;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class CustomizeAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException {
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        String json = "";
        json = new Gson().toJson(new okResponse("/"));
        httpServletResponse.getWriter().write(json);
    }
}
