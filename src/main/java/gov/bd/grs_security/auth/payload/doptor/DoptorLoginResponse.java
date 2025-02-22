package gov.bd.grs_security.auth.payload.doptor;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DoptorLoginResponse implements Serializable {
    private String status;
    private UserInfo user_info;
}
