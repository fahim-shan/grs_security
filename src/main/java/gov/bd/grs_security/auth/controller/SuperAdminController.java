package gov.bd.grs_security.auth.controller;

import gov.bd.grs_security.auth.model.SuperAdmin;
import gov.bd.grs_security.auth.payload.PasswordChange;
import gov.bd.grs_security.auth.repository.SuperAdminRepository;
import gov.bd.grs_security.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/auth/superAdmin")
@RequiredArgsConstructor
public class SuperAdminController {

    private final SuperAdminRepository superAdminRepo;
    private final AuthService authService;

    @PostMapping("/findByUsername")
    public SuperAdmin findByUsername(@RequestParam String username){
        return superAdminRepo.findByUsername(username);
    }

    @PostMapping("/findByUsernameAndPassword")
    public SuperAdmin findByUsernameAndPassword(@RequestParam String username,
                                                @RequestParam String password){
        return superAdminRepo.findByUsernameAndPassword(username, password);
    }

    @PostMapping("/findByUserRoleId")
    public List<SuperAdmin> findByUserRoleId(@RequestParam Long role) {
        return superAdminRepo.findByUserRoleId(role);
    }

    @PostMapping("/findByPhoneNumber")
    public SuperAdmin findByPhoneNumber(@RequestParam String phoneNumber) {
        return superAdminRepo.findByPhoneNumber(phoneNumber);
    }

    @PostMapping("/countByUsername")
    public Integer countByUsername(@RequestParam String username) {
        return superAdminRepo.countByUsername(username);
    }

    @PostMapping("/save")
    public SuperAdmin save(@RequestBody SuperAdmin superAdmin) {
        return superAdminRepo.save(superAdmin);
    }

    @GetMapping("/findOne/{id}")
    public SuperAdmin findOne(@PathVariable Long id) {
        return superAdminRepo.findById(id).orElse(null);
    }

    @GetMapping("/findAll")
    public List<SuperAdmin> findAll() {
        return superAdminRepo.findAll();
    }

    @PostMapping("/passwordChange")
    public Map<String, Object> superAdminPasswordChange(@RequestBody PasswordChange passwordChange) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", authService.superAdminPasswordChange(passwordChange));
        return response;
    }

}
