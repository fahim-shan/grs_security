package gov.bd.grs_security.auth.controller;

import gov.bd.grs_security.auth.model.SuperAdmin;
import gov.bd.grs_security.auth.repository.SuperAdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/auth/superAdmin")
@RequiredArgsConstructor
public class SuperAdminController {

    private final SuperAdminRepository superAdminRepo;

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
}
