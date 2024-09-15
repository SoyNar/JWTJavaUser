package org.nar.jwtproyect.Security.Filter;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.GrantedAuthority;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.nar.jwtproyect.Entities.User;

import java.io.IOException;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
// importamos las variables de la clase tokenconfig
import  static org.nar.jwtproyect.Security.Filter.TokenJwtConfig.*;


// filtro de autenticacion

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    private AuthenticationManager authenticationManager;

    //llave secreta




    // enviaremos json con user y passwoord
    //capturamos el objeto y lo convertimos a un objeto de java
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {


            User userE = null;
            String userName = null;
            String passWord= null;
            try {
                userE = new
               ObjectMapper().readValue(request.getInputStream(), User.class);
                userName = userE.getUsername();
                passWord = userE.getPassword();
            } catch (StreamReadException e) {
                e.printStackTrace();
            }catch (DatabindException e){
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }
            // recibe dos argunementos el JSon que capturamos con RequestInputJsonStream
            // el otro parametro es el objet al cual lo vamos a convertir un objeto de la clase USer

            UsernamePasswordAuthenticationToken authenticationToken = new
                    UsernamePasswordAuthenticationToken(userName
                    ,passWord);
            return  authenticationManager.authenticate(authenticationToken);
        }

        // metodo si todo sale bien
    // antes de eso neceitamo generar la llave secreta
    // tipo key



    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
             Authentication authResult)
            throws IOException, ServletException {

        // obtenemos el username con autresult
        org.springframework.security.core.userdetails.User userS = (org.springframework.security.core.userdetails.User)authResult.getPrincipal(); // user de spring security
        String userName = userS.getUsername();
        // roles
         Collection <? extends GrantedAuthority> roles =
                 authResult.getAuthorities();
         // pasar a los claims. datos parte del payload

        Claims claims = Jwts.claims()
                .add("authorities", roles)
                .build();

        // lo casteamos al user de springsecurity
        String toke = Jwts.builder()
                .subject(userName)
                .claims(claims)
                .expiration(new Date(System.currentTimeMillis()+ 3600000) ) // fecha actual mas una hora
                .issuedAt(new Date()) // fechac actual
                .signWith(SECRET_KEY)
                .compact();
        // GENERAMOS token

        // devolvemos el token a la vista
        response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + toke);
        // pasamos con json
        Map<String, String> body = new HashMap<>();
        body.put("token",toke);
        body.put("username",userName);
        body.put("message",String.format("Hola %s has iniciacio sesion con exito", userName));

        // pasamos este json en la respuestas

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        // convertimos el map a un json
        response.setContentType(CONTENT_TYPE);
        response.setStatus(200);



    }

//     En caso de error
//     como buena practica no se deebe dar informacion sobre cual de los datos es el incorrecto por seguridad

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
           HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        Map<String, String> body = new HashMap<>();
        body.put("messege","Error en la autenticacion username o password incorrectos");
        body.put("error", failed.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(401);
        response.setContentType(CONTENT_TYPE);
    }
}

