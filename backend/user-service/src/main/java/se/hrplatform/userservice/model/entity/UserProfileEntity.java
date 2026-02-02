package se.hrplatform.userservice.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_profile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id; // Keycloak userId (UUID som String)

    @Column(nullable = false, unique = true)
    private String username;

    private String name;

    private String email;

    @Column(nullable = false)
    private boolean active;

    @ManyToMany
    @JoinTable(
            name = "user_profile_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<WorkRoleEntity> roles = new HashSet<>();

    @Column(name = "department_id")
    private Long departmentId;
}

