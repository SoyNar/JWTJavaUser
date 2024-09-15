package org.nar.jwtproyect.Validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.nar.jwtproyect.Service.IService.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ExistsByUsernameValidation implements ConstraintValidator<ExistsByUsername,String> {
    @Autowired
    private IUserService userService;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext Context) {
        if (username == null) {
            return true; // Si el nombre de usuario es null, no es válido
        }
      return !userService.existsByUserName(username);
    }
}
