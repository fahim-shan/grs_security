package gov.bd.grs_security.audit.repository;

import gov.bd.grs_security.audit.model.LoginTrace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginTraceRepo extends JpaRepository<LoginTrace, Long> {
}
