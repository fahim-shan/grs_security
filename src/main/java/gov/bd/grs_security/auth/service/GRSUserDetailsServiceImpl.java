package gov.bd.grs_security.auth.service;

import gov.bd.grs_security.audit.service.LoginTraceService;
import gov.bd.grs_security.auth.model.GrsRole;
import gov.bd.grs_security.auth.model.SuperAdmin;
import gov.bd.grs_security.auth.payload.UserInformation;
import gov.bd.grs_security.auth.repository.SuperAdminRepository;
import gov.bd.grs_security.common.enums.GRSUserType;
import gov.bd.grs_security.common.enums.UserType;
import gov.bd.grs_security.config.GrantedAuthorityImpl;
import gov.bd.grs_security.config.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GRSUserDetailsServiceImpl implements UserDetailsService {
    private final SuperAdminRepository superAdminRepo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final LoginTraceService loginTraceService;

    @Override
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        SuperAdmin user = superAdminRepo.findByUsername(username);
        if (user == null) {
            return null;
        }

        GrsRole role = user.getRole();
        List<GrantedAuthorityImpl> grantedAuthorities = role.getPermissions().stream()
                .map(permission -> {
                    return GrantedAuthorityImpl.builder()
                            .authority(permission.getName())
                            .build();
                }).collect(Collectors.toList());

        UserInformation userInformation = this.getUserInfo(user);

        loginTraceService.saveGRSLogin(userInformation);

        return UserDetailsImpl.builder()
                .username(user.getUsername())
//                .password(user.getPassword())
                .grantedAuthorities(grantedAuthorities)
                .userInformation(userInformation)
                .isAccountAuthenticated(true)
                .build();
    }

    public UserDetailsImpl loadUserByUsernameAndPassword(String username, String password) {
        SuperAdmin user = superAdminRepo.findByUsername(username);
        if (user == null) {
            return null;
        }
        if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
            return loadUserByUsername(username);
        } else {
            return null;
        }
    }

    public UserInformation getUserInfo(SuperAdmin user) {
        return UserInformation
                .builder()
                .userId(user.getId())
                .username(user.getUsername())
                .userType(UserType.SYSTEM_USER)
                .oisfUserType(null)
                .isOfficeAdmin(false)
                .isAppealOfficer(false)
                .isCellGRO(false)
                .grsUserType(getGRSUserTypeFromRole(user))
                .build();
    }

    public GRSUserType getGRSUserTypeFromRole(SuperAdmin user) {
        return GRSUserType.valueOf(user.getRole().getRole());
    }
}
