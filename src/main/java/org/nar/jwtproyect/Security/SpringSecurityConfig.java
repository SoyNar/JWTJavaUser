package org.nar.jwtproyect.Security;

import org.nar.jwtproyect.Security.Filter.JwtAuthenticationFilter;
import org.nar.jwtproyect.Security.Filter.JwtValidationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;


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
                        .requestMatchers(HttpMethod.POST,"/api/users").hasRole("ADMIN")

                        .anyRequest().authenticated())
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilter(new JwtValidationFilter(authenticationManager()))

                .csrf(config -> config.disable())//token para evitar vulnerabildiad
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(manag ->
                        manag // para que la sesion no tenga estado y todo se maneje desde el token
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)).build();
        //crear user y listar de acceso publico

    }

    //configurar cors

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization","Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> corsBean =
                new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));
        corsBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return corsBean;
    }

}
