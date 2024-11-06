package gov.bd.grs_security.auth.repository;

import gov.bd.grs_security.auth.model.Complainant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplainantRepository extends JpaRepository<Complainant, Long> {
    Complainant findByUsername(String username);
}
