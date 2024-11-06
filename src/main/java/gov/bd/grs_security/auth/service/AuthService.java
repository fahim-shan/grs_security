package gov.bd.grs_security.auth.service;

import gov.bd.grs_security.auth.payload.LoginRequest;
import gov.bd.grs_security.auth.payload.LoginResponse;
import gov.bd.grs_security.config.GrantedAuthorityImpl;
import gov.bd.grs_security.config.JwtService;
import gov.bd.grs_security.config.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;
    private final GRSUserDetailsServiceImpl grsUserDetailsService;

    public LoginResponse login(LoginRequest request) {
        try {
            LoginResponse response = new LoginResponse();
            UserDetailsImpl userDetails1 = userDetailsService.loadUserbyUsernameAndPassword(request.getUsername(), request.getPassword());
            if (userDetails1 == null || userDetails1.getUserInformation() == null) {
                UserDetailsImpl userDetails2 = grsUserDetailsService.loadUserByUsernameAndPassword(request.getUsername(), request.getPassword());
                if (userDetails2 != null && userDetails2.getUserInformation() != null) {
                    return constructLoginResponse(userDetails2);
                } else {
                    return new LoginResponse(false, "Bad Credentials", null, null, null, null);
                }
            } else {
                return constructLoginResponse(userDetails1);
            }
        } catch (Exception e) {
            log.error("Error handling login{}", e.toString());
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
}
