package se.hrplatform.userservice.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

/**
 * This represents user table created by keycloak - Should only be used to read data from
 */
@Entity
@Table(name = "user_entity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @Column(name = "id")
    private String id;

    private String email;

    @Column(name = "email_constraint")
    private String emailConstraint;

    @Column(name = "email_verified")
    private boolean emailVerified;

    private boolean enabled;

    @Column(name = "federation_link")
    private String federationLink;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "realm_id")
    private String realmId;

    private String username;

    @Column(name = "created_timestamp")
    private Long createdTimestamp;

    @Column(name = "service_account_client_link")
    private String serviceAccountClientLink;

    @Column(name = "not_before")
    private Integer notBefore;

}

