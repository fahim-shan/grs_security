package gov.bd.grs_security.auth.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import gov.bd.grs_security.common.enums.GRSUserType;
import gov.bd.grs_security.common.enums.OISFUserType;
import gov.bd.grs_security.common.enums.UserType;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class UserInformation {
    private Long userId;
    private String username;
    private UserType userType;
    private OISFUserType oisfUserType;
    private GRSUserType grsUserType;
    private OfficeInformation officeInformation;
    private Boolean isAppealOfficer;
    private Boolean isOfficeAdmin;
    private Boolean isCentralDashboardUser;
    private Boolean isCellGRO;
    private Boolean isMobileLogin;
    private Boolean isMyGovLogin;
    private String token = "";
}
