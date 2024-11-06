package gov.bd.grs_security.auth.model;

import gov.bd.grs_security.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "grs_roles")
public class GrsRole extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(length = 50, name = "role")
    private String role;

    @NotFound(action = NotFoundAction.IGNORE)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "permissions_to_roles",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "ID"))
    private List<Permission> permissions;
}
