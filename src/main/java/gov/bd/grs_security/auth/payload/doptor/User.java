package gov.bd.grs_security.auth.payload.doptor;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class User {
    private Integer id;
    private String username;
    private String user_alias;
    private String hash_change_password;
    private Integer user_role_id;
    private Integer is_admin;
    private boolean active;
    private String user_status;
    private Integer is_email_verified;
    private String email_verify_code;
    private Date verification_date;
    private String ssn;
    private boolean force_password_change;
    private Date last_password_change;
    private Date created;
    private Date modified;
    private String created_by;
    private String modified_by;
    private String photo;
    private Integer employee_record_id;
}
