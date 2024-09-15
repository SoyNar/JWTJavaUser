package org.nar.jwtproyect.Controller;

import jakarta.validation.Valid;
import org.nar.jwtproyect.Entities.User;
import org.nar.jwtproyect.Service.IService.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping
    public List<User> readAll(){
        return this.userService.findAll();

    }

    @PostMapping
    ResponseEntity<?> save( @Valid  @RequestBody User user, BindingResult result){
        if(result.hasFieldErrors()){
            return validation(result);
        }

        return  ResponseEntity.status(HttpStatus.CREATED).body(this.userService.save(user));
    }


    @PostMapping("/register")
    ResponseEntity<?> register( @Valid  @RequestBody User user, BindingResult result){
 // antes de guardar validamos que no sea admin
        if(result.hasFieldErrors()){
            return validation(result);
        }
        user.setAdmin(false);
        return  ResponseEntity.status(HttpStatus.CREATED).body(this.userService.save(user));
    }
// para validar


    private ResponseEntity<?> validation (BindingResult result){
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err ->{
            errors.put(err.getField(),"el campo" + err.getField() + "" + err.getDefaultMessage());
        });
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        // tambien se puede hacer
        //ResponseEntity.BadRequest.body(errors)
    }

}
