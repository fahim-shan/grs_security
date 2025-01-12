package gov.bd.grs_security.auth.repository;

import gov.bd.grs_security.auth.model.Complainant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplainantRepository extends JpaRepository<Complainant, Long> {
    Complainant findByUsername(String username);
    Complainant findByUsernameAndPassword(String Username, String password);
    Complainant findByPhoneNumber(String phoneNumber);
    List<Complainant> findByPhoneNumberIsContaining(String phoneNumber);
}
