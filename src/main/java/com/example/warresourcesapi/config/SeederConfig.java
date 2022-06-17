package com.example.warresourcesapi.config;

import com.example.warresourcesapi.model.Role;
import com.example.warresourcesapi.repository.RoleRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class SeederConfig implements ApplicationRunner {

    private final RoleRepository roleRepository;

    public SeederConfig(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        seedRoles();
    }

    private void seedRoles() {
        if (roleRepository.findByName("BASIC") == null) {
            Role adminRole = new Role("BASIC");
            roleRepository.save(adminRole);
        }
        if (roleRepository.findByName("PREMIUM") == null) {
            Role userRole = new Role("PREMIUM");
            roleRepository.save(userRole);
        }
    }
}
