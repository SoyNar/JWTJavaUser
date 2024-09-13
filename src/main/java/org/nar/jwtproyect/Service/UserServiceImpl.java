package org.nar.jwtproyect.Service;

import groovy.transform.PackageScope;
import org.nar.jwtproyect.Entities.Role;
import org.nar.jwtproyect.Entities.User;
import org.nar.jwtproyect.Repository.RoleRepository;
import org.nar.jwtproyect.Repository.UserRepository;
import org.nar.jwtproyect.Service.IService.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
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
        return (List<User>)this.userRepository.findAll();
    }

    @Override
    @Transactional
    public User save(User user) {
        //todo usuario tiene role user
        Optional<Role> optionaRoleUser = this.roleRepository.findByName("ROLE_USER");
        // lista de roles para pasarla al user
        List<Role> roles  = new ArrayList<>();
        optionaRoleUser.ifPresent(roles::add);

        user.setRoles(roles);
        //obtenemos el passoword que viene de user y lo encriptamos
        user.setPassword(passwordEncoder.encode(user.getPassword())); //passamo password codificado

        if(user.isAdmin()){
            Optional<Role> optionalRoleAdmin = this.roleRepository.findByName("ROLE_ADMIN");
            optionalRoleAdmin.ifPresent(roles::add);

        }
        return this.userRepository.save(user);
    }
}
