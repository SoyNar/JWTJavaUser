package org.nar.jwtproyect.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(); //referencia de bcryp
    }

    // configurar reglas de configuracion
    // springsecurity
    // dar permisos, denegar
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // se inyecto objeto de spring
        return http.authorizeHttpRequests(authz -> authz.requestMatchers("/users")
                .permitAll().anyRequest().authenticated())
                .csrf(config -> config.disable())//token para evitar vulnerabildiad
                .sessionManagement(manag -> manag // para que la sesion no tenga estado y todo se maneje desde el token
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)).build();
        //crear user y listar de acceso publico

    }

}
