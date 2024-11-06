package gov.bd.grs_security.auth.repository;

import gov.bd.grs_security.auth.model.GrsRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GrsRoleRepository extends JpaRepository<GrsRole, Long> {
    GrsRole findByRole(String role);
}
