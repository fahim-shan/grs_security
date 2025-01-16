package gov.bd.grs_security.auth.payload;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordChange {
    private String username;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
