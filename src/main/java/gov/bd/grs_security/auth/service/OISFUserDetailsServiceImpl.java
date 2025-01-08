package gov.bd.grs_security.auth.service;

import gov.bd.grs_security.auth.dao.GrsRoleDao;
import gov.bd.grs_security.auth.gateway.OISFUserDetailsServiceGateway;
import gov.bd.grs_security.auth.model.GrsRole;
import gov.bd.grs_security.auth.model.User;
import gov.bd.grs_security.auth.payload.UserInformation;
import gov.bd.grs_security.auth.repository.UserRepository;
import gov.bd.grs_security.common.enums.UserType;
import gov.bd.grs_security.config.GrantedAuthorityImpl;
import gov.bd.grs_security.config.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OISFUserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final GrsRoleDao grsRoleDao;
    private final OISFUserDetailsServiceGateway oisfUserDetailsServiceGateway;

    @Override
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = this.userRepository.findByUsername(username);
        if (user == null) {
            return null;
        }
        UserInformation userInfo = oisfUserDetailsServiceGateway.getUserInformationFromUser(user);
        if (userInfo == null) {
            return null;
        }

        String roleName;
        if (userInfo.getGrsUserType() != null) {
            roleName = userInfo.getGrsUserType().name();
        } else {
            roleName = userInfo.getOisfUserType().name();
        }
        GrsRole grsRole = this.grsRoleDao.findByRole(roleName);

        List<GrantedAuthorityImpl> grantedAuthorities = grsRole.getPermissions().stream()
                .map(permission -> GrantedAuthorityImpl.builder()
                        .authority(permission.getName())
                        .build()
                ).collect(Collectors.toList());

        return UserDetailsImpl.builder()
                .username(userInfo.getUsername())
                .isAccountAuthenticated(true)
                .userInformation(userInfo)
                .grantedAuthorities(grantedAuthorities)
                .build();
    }

    public UserDetailsImpl loadUserByUsernameAndPassword(String username, String password) {
        User user = this.userRepository.findByUsername(username);
        if (user == null) {
            return null;
        }
        return this.loadUserByUsername(username);

//        Password is not checked. Any password will work
//        if (passwordService.checkPassword(password, user.getPassword())) {
//            return this.loadUserByUsername(username);
//        } else {
//            return null;
//        }
    }

    private UserInformation getUserInfo(User user) {
        return UserInformation.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .userType(UserType.OISF_USER)
                .build();
    }
}
