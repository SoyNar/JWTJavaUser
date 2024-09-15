package org.nar.jwtproyect.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.nar.jwtproyect.Validation.ExistsByUsername;
//import org.nar.jwtproyect.Validation.ExistsByUsername;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table (name = "user")
@EqualsAndHashCode(of={"id","username"})
public class User {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;
//    @ExistsByUsername
    @Column(unique = true)
    @NotBlank(message = "no vacio")

    private String username;


    @PrePersist
    public void prePersist() {
        enabled = true;
    }

    private boolean enabled;
// ignorar contraseña
    // enviamos y desaparece la contraseña
    //@Jsonignore puede ser pero no funciona con postmappin
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "no vacio")
    // numero de caracteres
    private  String password;

//campo no mapeado a la tabla// bandera
    // propio de la clase no de la BD
    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // solo se muestra para editar
    private boolean admin;


    @JsonIgnoreProperties({"users", "handler","hibernateLazyInitializer"})
    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns  = @JoinColumn(name = "user_id"),
            inverseForeignKey = @ForeignKey (name = "roles_id"),
            uniqueConstraints = {@UniqueConstraint(columnNames= {"user_id, roles_id"} )}
    )
    private List<Role> roles;




}
