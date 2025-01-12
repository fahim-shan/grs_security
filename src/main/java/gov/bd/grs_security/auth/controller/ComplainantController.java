package gov.bd.grs_security.auth.controller;

import gov.bd.grs_security.auth.model.Complainant;
import gov.bd.grs_security.auth.repository.ComplainantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/auth/complainant")
@RequiredArgsConstructor
public class ComplainantController {

    private final ComplainantRepository complainantRepo;

    @PostMapping("/findByUsername")
    public Complainant findByUsername(@RequestParam String username){
        return complainantRepo.findByUsername(username);
    };

    @PostMapping("/findByUsernameAndPassword")
    public Complainant findByUsernameAndPassword(@RequestParam String Username,
                                                 @RequestParam String password) {
        return complainantRepo.findByUsernameAndPassword(Username, password);
    };

    @PostMapping("/findByPhoneNumber")
    public Complainant findByPhoneNumber(@RequestParam String phoneNumber) {
        return complainantRepo.findByPhoneNumber(phoneNumber);
    };

    @PostMapping("/findByPhoneNumberIsContaining")
    public List<Complainant> findByPhoneNumberIsContaining(@RequestParam String phoneNumber) {
        return complainantRepo.findByPhoneNumberIsContaining(phoneNumber);
    };

    @GetMapping("/count")
    public long count() {
        return complainantRepo.count();
    }

    @GetMapping("/findOne/{id}")
    public Complainant findOne(@PathVariable Long id) {
        return complainantRepo.findById(id).orElse(null);
    }

    @GetMapping("/findAll")
    public List<Complainant> findAll() {
        return complainantRepo.findAll();
    }

    @PostMapping("/save")
    public Complainant save(@RequestBody Complainant complainant) {
        return complainantRepo.save(complainant);
    }
}
