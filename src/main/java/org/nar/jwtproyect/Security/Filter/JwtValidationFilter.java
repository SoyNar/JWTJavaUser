package org.nar.jwtproyect.Security.Filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.nar.jwtproyect.Security.SimpleGrantedAuthorityJsonCreator;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.*;

import static org.nar.jwtproyect.Security.Filter.TokenJwtConfig.*;

public class JwtValidationFilter extends BasicAuthenticationFilter {

    public JwtValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        super.doFilterInternal(request, response, chain);

        // obtener token o cabecera

        String header = request.getHeader(HEADER_AUTHORIZATION);
        // validamos que sea distinto de nulo
        if(header == null || !header.startsWith(PREFIX_TOKEN)){
            chain.doFilter(request,response);
            return;
        }
        String token = header.replace(PREFIX_TOKEN, "");// quitamos el prefijo del token/
        // quitamos la palabra bearer y la reemplazamos por vacio
        try {
            Claims claims = Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();
            // validamos el token hacemos un parser, verificamos con la llave
            // si //todo sale bien obtenemos el payload//
            // obtener el username
            String username = claims.getSubject();
            // otra forma con :
//            String usernamee = (String) claims.get("username");
            Object authorityClaims = claims.get("authorities"); // roles

            // procesamos los roles
            Collection<? extends GrantedAuthority> authorities = Arrays.asList
                    ( new ObjectMapper()
                            .addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityJsonCreator.class)// acoplamos simple  a  nuestra clase
                            .readValue(authorityClaims.toString().getBytes(), SimpleGrantedAuthority[].class));

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,null,authorities);

        // autenticamos
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            chain.doFilter(request,response);
            
        } catch (JwtException e) {
          //en caso de que no se valide creamos un json con map
            Map<String, String > body = new HashMap<>();
                    body.put("error",e.getMessage());
                    body.put("message", "El token Jwt es invalid");

                    response.getWriter().write(new ObjectMapper().writeValueAsString(body));
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.setContentType(CONTENT_TYPE);
        }
        // comenzar a validar
    }
}
