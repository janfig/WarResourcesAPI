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
        if (roleRepository.findByName("ADMIN") == null) {
            Role adminRole = new Role("ADMIN");
            roleRepository.save(adminRole);
        }
        if (roleRepository.findByName("STANDARD") == null) {
            Role userRole = new Role("STANDARD");
            roleRepository.save(userRole);
        }
        if (roleRepository.findByName("CUSTOM") == null) {
            Role userRole = new Role("CUSTOM");
            roleRepository.save(userRole);
        }
//        roleRepository.flush();
    }
}
