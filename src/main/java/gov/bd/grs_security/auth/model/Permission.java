package gov.bd.grs_security.auth.model;

import lombok.*;

import jakarta.persistence.*;

/**
 * Created by Acer on 9/9/2017.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "permissions")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(length = 50, name = "PERMISSION_NAME")
    private String name;
}
