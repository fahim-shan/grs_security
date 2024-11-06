package gov.bd.grs_security.auth.service;

import gov.bd.grs_security.audit.service.LoginTraceService;
import gov.bd.grs_security.auth.dao.GrsRoleDao;
import gov.bd.grs_security.auth.model.Complainant;
import gov.bd.grs_security.auth.model.GrsRole;
import gov.bd.grs_security.auth.payload.UserInformation;
import gov.bd.grs_security.auth.repository.ComplainantRepository;
import gov.bd.grs_security.common.enums.UserType;
import gov.bd.grs_security.config.GrantedAuthorityImpl;
import gov.bd.grs_security.config.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final ComplainantRepository complainantRepo;
    private final GrsRoleDao roleDao;
    private final LoginTraceService loginTraceService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        Complainant user = complainantRepo.findByUsername(username);
        if (user == null) {
            return null;
        }

        GrsRole role = roleDao.findByRole("COMPLAINANT");

        List<GrantedAuthorityImpl> grantedAuthorities = role.getPermissions().stream()
                .map(permission -> {
                    return GrantedAuthorityImpl.builder()
                            .authority(permission.getName())
                            .build();
                }).toList();

        UserInformation userInformation = UserInformation
                .builder()
                .userId(user.getId())
                .username(user.getName())
                .userType(UserType.COMPLAINANT)
                .officeInformation(null)
                .oisfUserType(null)
                .isAppealOfficer(false)
                .build();

        loginTraceService.saveGRSLogin(userInformation);

        return UserDetailsImpl.builder()
                .username(user.getUsername())
//                .password(user.getPassword())
                .isAccountAuthenticated(user.isAuthenticated())
                .grantedAuthorities(grantedAuthorities)
                .userInformation(userInformation)
                .build();
    }

    public UserDetailsImpl loadUserbyUsernameAndPassword(String username, String password) {
        Complainant complainant = complainantRepo.findByUsername(username);
        if (complainant == null) {
            return null;
        }
        if (bCryptPasswordEncoder.matches(password, complainant.getPassword())) {
            return loadUserByUsername(username);
        } else {
            return null;
        }
    }
}
