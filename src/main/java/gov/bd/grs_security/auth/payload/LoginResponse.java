package gov.bd.grs_security.auth.payload;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private Boolean opResult;
    private String opMessage;
    private String accessToken;
    private String refreshToken;
    private UserInformation userInformation;
    private List<String> authorities;
}
