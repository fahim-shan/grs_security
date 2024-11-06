package gov.bd.grs_security.auth.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "grs_users")
public class SuperAdmin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(length = 50, name = "username")
    private String username;

    @Column(length = 50, name = "password")
    private String password;

    @Column(name = "mobile_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "office_id")
    private long officeId;

    @NotFound(action = NotFoundAction.IGNORE)
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_role_id", referencedColumnName = "ID")
    private GrsRole role;
}
