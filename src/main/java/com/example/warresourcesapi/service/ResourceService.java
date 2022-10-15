package com.example.warresourcesapi.service;

import com.example.warresourcesapi.exception.ForbiddenRequestException;
import com.example.warresourcesapi.exception.NotFoundException;
import com.example.warresourcesapi.model.AppUser;
import com.example.warresourcesapi.model.Resource;
import com.example.warresourcesapi.model.Role;
import com.example.warresourcesapi.repository.ResourceRepository;
import com.example.warresourcesapi.repository.RoleRepository;
import com.example.warresourcesapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Service
public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public ResourceService(ResourceRepository resourceRepository, UserRepository userRepository, RoleRepository roleRepository) {
        this.resourceRepository = resourceRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public List<Resource> getResources(Long userId) {
        if (isUserPremium(userId))
            return resourceRepository.findAll();
        return new ArrayList<>(List.of(resourceRepository.getById(1L)));

    }

    public Resource getSingleResource(Long resourceId, Long userId) {
        if (!isUserPremium(userId) && resourceId > 1)
            throw new ForbiddenRequestException("Only premium users can access this resource");
        return resourceRepository.findById(resourceId)
                .orElseThrow(() -> new NotFoundException("There is no resource with given id"));
    }

    public Resource getResourceByName(String name) {
        return resourceRepository.getByName(name);
    }

    public Resource getResourcesFromDateRange(Long resourceId, String startDateStr, String endDateStr, Long userId) {
        if (!isUserPremium(userId) && resourceId > 1)
            throw new ForbiddenRequestException("Only premium users can access this resource");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        var startDate = LocalDate.parse(startDateStr, formatter);
        var endDate = LocalDate.parse(endDateStr, formatter);
        return new Resource(
                resourceId,
                resourceRepository.getById(resourceId).getName(),
                resourceRepository.getResourceByIdAndPricesBetween(resourceId, startDate, endDate)
        );
    }

    private boolean isUserPremium(Long id) {
        AppUser user = userRepository.getById(id);
        Role premiumRole = roleRepository.findByName("PREMIUM");
        return user.getRoles().contains(premiumRole);
    }


}
