package gov.bd.grs_security.auth.controller;

import gov.bd.grs_security.auth.payload.LoginRequest;
import gov.bd.grs_security.auth.payload.LoginResponse;
import gov.bd.grs_security.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @ResponseBody
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/refresh")
    @ResponseBody
    public LoginResponse refresh(@RequestParam String refreshToken) {
        return authService.refresh(refreshToken);
    }

    @PostMapping("/admin-login")
    @ResponseBody
    public LoginResponse adminLogin(@RequestParam String data,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {
        return authService.adminLogin(data, request, response);
    }
}
