package org.nar.jwtproyect.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Getter
@Setter
@Entity
@NoArgsConstructor
@EqualsAndHashCode(of={"id","name"})
@Table(name = "roles")

public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    @JsonIgnoreProperties({"users", "handler","hibernateLazyInitializer"})
    @ManyToMany(mappedBy = "roles")
    private List<User> users;



}
