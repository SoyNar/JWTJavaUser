package org.nar.jwtproyect.Service;

import org.nar.jwtproyect.Entities.User;
import org.nar.jwtproyect.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// como traemos nuestro usuario por username
// inyectamos componente que nos permite traer los username
@Service
public class JpaUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       Optional<User> userOptional = this.userRepository.findByUsername(username);
       // si no esta presente lanzamos una excepcion
        if(!userOptional.isPresent()){
            throw new UsernameNotFoundException(String.format("User %s not found", username));
            // pasamos dos parametros, mensaje con format %S y el username
        }
// si esta presente lo obtenemos
        User user = userOptional.orElseThrow();
        List<GrantedAuthority> roles = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
        // con el stream convertimos a una lista de granteauthority
        //por cada role creamos una instancia de simplegranteAuthority
        // ahora con collections lo convertimos en una lista
        return new org.springframework.security
                .core.userdetails.
                User(user.getUsername(),
                user.getPassword(),user.isEnabled(),
                true, // la cuenta no expira
                true,        //los credenciales no expiran
                true,   // la cuenta no se bloquea pero si se puede deshabilitar
            roles);
    }
}
