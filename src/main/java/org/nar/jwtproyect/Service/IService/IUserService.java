package org.nar.jwtproyect.Service.IService;

import org.nar.jwtproyect.Entities.User;

import java.util.List;

public interface IUserService {


    List<User> findAll();

    User save(User user);

    boolean existsByUserName(String username);
    

}
