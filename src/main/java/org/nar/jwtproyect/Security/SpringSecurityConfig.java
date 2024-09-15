package org.nar.jwtproyect.Security;

import org.nar.jwtproyect.Security.Filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.util.UrlPathHelper;

@Configuration
public class SpringSecurityConfig {

    // registrar filtro de autenticacion con autenticacion manager
@Autowired
    private AuthenticationConfiguration authenticationConfiguration;

@Bean
AuthenticationManager authenticationManager () throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
}
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(); //referencia de bcryp
    }

    // configurar reglas de configuracion
    // springsecurity
    // dar permisos, denegar
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        // se inyecto objeto de spring
        return http.authorizeHttpRequests((authz) -> authz
                        .requestMatchers(HttpMethod.GET,"/api/users").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/users/register").permitAll()
                        .anyRequest().authenticated())
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .csrf(config -> config.disable())//token para evitar vulnerabildiad
                .sessionManagement(manag ->
                        manag // para que la sesion no tenga estado y todo se maneje desde el token
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)).build();
        //crear user y listar de acceso publico

    }

}
