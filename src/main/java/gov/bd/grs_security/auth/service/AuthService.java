package gov.bd.grs_security.auth.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.bd.grs_security.auth.dao.GrsRoleDao;
import gov.bd.grs_security.auth.gateway.OISFUserDetailsServiceGateway;
import gov.bd.grs_security.auth.model.GrsRole;
import gov.bd.grs_security.auth.payload.LoginRequest;
import gov.bd.grs_security.auth.payload.LoginResponse;
import gov.bd.grs_security.auth.payload.PasswordChange;
import gov.bd.grs_security.auth.payload.UserInformation;
import gov.bd.grs_security.auth.payload.doptor.*;
import gov.bd.grs_security.common.util.Utility;
import gov.bd.grs_security.config.GrantedAuthorityImpl;
import gov.bd.grs_security.config.JwtService;
import gov.bd.grs_security.config.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;
    private final GRSUserDetailsServiceImpl grsUserDetailsService;
    private final OISFUserDetailsServiceImpl oisfUserDetailsService;
    private final GrsRoleDao grsRoleDAO;
    private final OISFUserDetailsServiceGateway oisfUserDetailsServiceGateway;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public LoginResponse login(LoginRequest request) {
        try {
            // 1) Try to Login for Complainant
            UserDetailsImpl userDetails1 = userDetailsService.loadUserbyUsernameAndPassword(request.getUsername(), request.getPassword());

            // 2) If that failed, try to Login for SuperAdmin
            if (userDetails1 == null || userDetails1.getUserInformation() == null) {
                UserDetailsImpl userDetails2 = grsUserDetailsService.loadUserByUsernameAndPassword(request.getUsername(), request.getPassword());

                // 3) If that also failed, try to login for Admin Officers Login Bypass
                if (userDetails2 == null || userDetails2.getUserInformation() == null) {
                    UserDetailsImpl userDetails3 = oisfUserDetailsService.loadUserByUsernameAndPassword(request.getUsername(), request.getPassword());

                    // 4) If Admin Login Bypass also failed, return bad credentials
                    if (userDetails3 == null || userDetails3.getUserInformation() == null) {
                        return new LoginResponse(
                                false,
                                "Bad Credentials",
                                null,
                                null,
                                null,
                                null
                        );
                    } else {
                        return constructLoginResponse(userDetails3);
                    }
                } else {
                    return constructLoginResponse(userDetails2);
                }
            } else {
                return constructLoginResponse(userDetails1);
            }

        } catch (Exception e) {
            log.error("Error handling login {}", e.toString());
            return new LoginResponse(false, "Internal Server Error", null, null, null, null);
        }
    }

    private LoginResponse constructLoginResponse(UserDetailsImpl userDetails) {
        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);
        LoginResponse response = new LoginResponse();
        response.setOpResult(true);
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setUserInformation(userDetails.getUserInformation());
        response.setAuthorities(userDetails.getGrantedAuthorities()
                .stream()
                .map(GrantedAuthorityImpl::getAuthority)
                .collect(Collectors.toList())
        );

        return response;
    }

    public LoginResponse refresh(String refreshToken) {
        try {
            boolean isValid = jwtService.isTokenValid(refreshToken);
            if (isValid) {
                String username = jwtService.extractUsername(refreshToken);
                UserDetailsImpl userDetails1 = userDetailsService.loadUserByUsername(username);
                if (userDetails1 == null || userDetails1.getUserInformation() == null) {
                    UserDetailsImpl userDetails2 = grsUserDetailsService.loadUserByUsername(username);
                    if (userDetails2 != null && userDetails2.getUserInformation() != null) {
                        return constructLoginResponse(userDetails2);
                    } else {
                        return new LoginResponse(false, "Could not find user by this username", null, null, null, null);
                    }
                } else {
                    return constructLoginResponse(userDetails1);
                }
            } else {
                return new LoginResponse(false, "Invalid Refresh Token", null, null, null, null);
            }
        } catch (Exception e) {
            log.error("Exception while processing refresh token\n{}", e.toString());
            return null;
        }
    }

    public LoginResponse adminLogin(String data, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String jsonData = Utility.decompress(data);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        DoptorLoginResponse loginResponse = mapper.readValue(jsonData, DoptorLoginResponse.class);
        return afterLoginDoptorResponse(loginResponse, request, response);

    }


    public LoginResponse afterLoginDoptorResponse(DoptorLoginResponse loginResponse, HttpServletRequest request, HttpServletResponse response) throws Exception {

        User user1 = loginResponse.getUser_info().getUser();
        UserInfo userInfo = loginResponse.getUser_info();

        UserInformation userInformation = oisfUserDetailsServiceGateway.getUserInformation(userInfo);

        String roleName = null;
        if (userInformation.getGrsUserType() != null) {
            roleName = userInformation.getGrsUserType().name();
        } else {
            roleName = userInformation.getOisfUserType().name();
        }
        GrsRole grsRole = this.grsRoleDAO.findByRole(roleName);
        List<GrantedAuthorityImpl> grantedAuthorities = grsRole
                .getPermissions()
                .stream()
                .map(permission -> GrantedAuthorityImpl.builder()
                        .authority(permission.getName())
                        .build()).collect(Collectors.toList());

        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .username(user1.getUsername())
                .isAccountAuthenticated(true)
                .grantedAuthorities(grantedAuthorities).userInformation(userInformation).build();

        return constructLoginResponse(userDetails);
    }

    public String superAdminPasswordChange(PasswordChange passwordChange) {
        UserDetailsImpl userDetails = grsUserDetailsService.loadUserByUsernameAndPassword(passwordChange.getUsername(), passwordChange.getOldPassword());

        if (userDetails == null) {
            return "Could not find user by this username";
        }

        if (passwordChange.getNewPassword().equals(passwordChange.getConfirmPassword())) {
            grsUserDetailsService.updatePassword(passwordChange.getUsername(), passwordChange.getNewPassword());
            return "Password changed successfully";
        } else {
            return "New password and confirm password does not match";
        }
    }
}
