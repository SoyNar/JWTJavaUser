package org.nar.jwtproyect.Service;


import org.nar.jwtproyect.Entities.Role;
import org.nar.jwtproyect.Entities.User;
import org.nar.jwtproyect.Repository.RoleRepository;
import org.nar.jwtproyect.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.nar.jwtproyect.Service.IService.IUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl  implements IUserService {

    @Autowired
    private UserRepository userRepository;
    // para buscar el role por el nombre
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    @Override
    public List<User> findAll() {
        return (List<User>)userRepository.findAll();
    }

    @Override
    @Transactional
    public User save(User user) {
        //todo usuario tiene role user
        Optional<Role> optionaRoleUser = roleRepository.findByName("ROLE_USER");
        // lista de roles para pasarla al user
        List<Role> roles  = new ArrayList<>();
        optionaRoleUser.ifPresent(roles::add);

        if(user.isAdmin()){
            Optional<Role> optionalRoleAdmin = roleRepository.findByName("ROLE_ADMIN");
            optionalRoleAdmin.ifPresent(roles::add);

        }

        user.setRoles(roles);
        //obtenemos el passoword que viene de user y lo encriptamos
        user.setPassword(passwordEncoder.encode(user.getPassword())); //passamo password codificado
        return userRepository.save(user);
    }




    @Override
    public boolean existsByUserName(String username) {
        return userRepository.existsByUsername(username);
    }
}
