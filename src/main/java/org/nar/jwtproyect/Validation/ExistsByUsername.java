package org.nar.jwtproyect.Validation;

import jakarta.validation.Constraint;
import org.springframework.messaging.handler.annotation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
// preparamos nuestra propia anotacion para validar
// constraint tenemos el nombre de la clase
// que implementa la interfaz constraint

@Constraint(validatedBy = ExistsByUsernameValidation.class )
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistsByUsername {

    java.lang.String message() default "{ Ya existe en la base de datos }";

    java.lang.Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
