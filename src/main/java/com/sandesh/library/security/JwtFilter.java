package com.sandesh.library.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtFilter implements Filter {

    @Autowired
    private JwtUtil jwtUtil;

@Override
public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse res = (HttpServletResponse) response;

    String path = req.getRequestURI();

    // 🔥 1. Allow preflight (VERY IMPORTANT for frontend)
    if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
        res.setStatus(HttpServletResponse.SC_OK);
        chain.doFilter(request, response);
        return;
    }

    // 🔓 2. Allow auth APIs (login/register)
    if (path.startsWith("/api/auth")) {
        chain.doFilter(request, response);
        return;
    }

    // 🔓 3. (OPTIONAL) allow public GET APIs if needed
    // if (path.equals("/api/books/get_books")) {
    //     chain.doFilter(request, response);
    //     return;
    // }

    // 🔐 4. Validate JWT
    String authHeader = req.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        res.setStatus(401);
        res.getWriter().write("Missing Authorization header");
        return;
    }

    String token = authHeader.substring(7);

    if (!jwtUtil.validateToken(token)) {
        res.setStatus(401);
        res.getWriter().write("Invalid token");
        return;
    }

    // ✅ 5. Continue
    chain.doFilter(request, response);
}

}