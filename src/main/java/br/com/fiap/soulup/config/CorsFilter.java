package br.com.fiap.soulup.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Libera o acesso do front-end (localhost do Vite e URL da Vercel em FRONT_URL)
 * e responde às requisições de preflight (OPTIONS).
 * FRONT_URL aceita mais de uma URL separada por vírgula.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter extends OncePerRequestFilter {

    private static final String METODOS = "GET, POST, PUT, PATCH, DELETE, OPTIONS";
    private static final String HEADERS = "Content-Type, Accept, Authorization";

    private final Set<String> origensPermitidas = new HashSet<>();

    public CorsFilter(@Value("${app.front-url:}") String frontUrl) {
        origensPermitidas.add("http://localhost:5173");
        origensPermitidas.add("http://localhost:4173");
        Arrays.stream(frontUrl.split(","))
                .map(String::trim)
                .map(url -> url.endsWith("/") ? url.substring(0, url.length() - 1) : url)
                .filter(url -> !url.isEmpty())
                .forEach(origensPermitidas::add);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String origem = request.getHeader("Origin");
        if (origem != null && origensPermitidas.contains(origem)) {
            response.setHeader("Access-Control-Allow-Origin", origem);
            response.setHeader("Access-Control-Allow-Methods", METODOS);
            response.setHeader("Access-Control-Allow-Headers", HEADERS);
            response.setHeader("Access-Control-Expose-Headers", "Location");
            response.setHeader("Access-Control-Max-Age", "3600");
        }
        response.addHeader("Vary", "Origin");

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        chain.doFilter(request, response);
    }
}
