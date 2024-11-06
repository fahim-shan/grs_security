package gov.bd.grs_security.audit.service;

import gov.bd.grs_security.auth.payload.UserInformation;
import gov.bd.grs_security.common.enums.LoginType;
import gov.bd.grs_security.audit.model.LoginTrace;
import gov.bd.grs_security.audit.repository.LoginTraceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginTraceService {

    @Autowired
    LoginTraceRepo loginTraceRepo;

    public LoginTrace save(LoginTrace loginTrace) {
        return loginTraceRepo.save(loginTrace);
    }

    public LoginTrace saveSSOLogin(UserInformation userInformation) {
        return save(LoginTrace.builder()
                .loginType(LoginType.OISF.name())
                .userId(userInformation.getUserId())
                .username(userInformation.getUsername())
                .officeId(userInformation.getOfficeInformation().getOfficeId())
                .officeNameBangla(userInformation.getOfficeInformation().getOfficeNameBangla())
                .officeNameEnglish(userInformation.getOfficeInformation().getOfficeNameEnglish())
                .officeMinistryId(userInformation.getOfficeInformation().getOfficeMinistryId())
                .officeOriginId(userInformation.getOfficeInformation().getOfficeOriginId())
                .designation(userInformation.getOfficeInformation().getDesignation())
                .employeeRecordId(userInformation.getOfficeInformation().getEmployeeRecordId())
                .officeUnitOrganogramId(userInformation.getOfficeInformation().getOfficeUnitOrganogramId())
                .layerLevel(userInformation.getOfficeInformation().getLayerLevel())
                .geoDivisionId(userInformation.getOfficeInformation().getGeoDivisionId())
                .geoDistrictId(userInformation.getOfficeInformation().getGeoDistrictId())
                .build()
        );
    }

    public LoginTrace saveMyGovLogin(UserInformation userInformation) {
        return save(LoginTrace.builder()
                .loginType(LoginType.MYGOV.name())
                .userId(userInformation.getUserId())
                .username(userInformation.getUsername())
                .mobileNo(userInformation.getUsername())
                .build()
        );
    }

    public LoginTrace saveGRSLogin(UserInformation userInformation) {
        return save(LoginTrace.builder()
                .loginType(LoginType.GRS.name())
                .userId(userInformation.getUserId())
                .username(userInformation.getUsername())
                .mobileNo(userInformation.getUsername())
                .build()
        );
    }

}
