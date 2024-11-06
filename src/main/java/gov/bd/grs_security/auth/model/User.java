package gov.bd.grs_security.auth.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(length = 50, name = "username")
    private String username;

    @Column(length = 50, name = "password")
    private String password;

    @NotNull
    @Column(name = "is_email_verified", columnDefinition = "TINYINT(4)")
    private Boolean authenticated;

    @Column(length = 255, name = "email_verify_code")
    private String confirmationCode;

    @Column(name = "employee_record_id")
    private Long employeeRecordId;

    @Column(name = "is_admin")
    private Boolean isAdmin = false;

}
