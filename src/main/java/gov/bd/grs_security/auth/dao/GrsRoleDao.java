package gov.bd.grs_security.auth.dao;

import gov.bd.grs_security.auth.model.GrsRole;
import gov.bd.grs_security.auth.repository.GrsRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GrsRoleDao {
    @Autowired
    private GrsRoleRepository grsRoleRepo;

    public GrsRole findByRole(String role){
        return this.grsRoleRepo.findByRole(role);
    }

    public List<GrsRole> findAll() {
        return this.grsRoleRepo.findAll();
    }
}
