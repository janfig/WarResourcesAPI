package unit.service;

import com.example.warresourcesapi.exception.ForbiddenRequestException;
import com.example.warresourcesapi.exception.NotFoundException;
import com.example.warresourcesapi.model.AppUser;
import com.example.warresourcesapi.model.Price;
import com.example.warresourcesapi.model.Resource;
import com.example.warresourcesapi.model.Role;
import com.example.warresourcesapi.repository.ResourceRepository;
import com.example.warresourcesapi.repository.RoleRepository;
import com.example.warresourcesapi.repository.UserRepository;
import com.example.warresourcesapi.service.ResourceService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ResourceServiceTest {
    private ResourceRepository resourceRepository = null;
    private UserRepository userRepository = null;
    private RoleRepository roleRepository = null;
    private ResourceService resourceService = null;

    @BeforeEach
    void setup() {
        this.userRepository = Mockito.mock(UserRepository.class);
        this.resourceRepository = Mockito.mock(ResourceRepository.class);
        this.roleRepository = Mockito.mock(RoleRepository.class);

        this.resourceService = new ResourceService(
                this.resourceRepository,
                this.userRepository,
                this.roleRepository
        );
    }

    @Test
    void getResources_userExists_returnsResourcesListForUser() {
        // arrange
        Collection<Role> roles = new ArrayList<>();
        roles.add(new Role("PREMIUM"));
        AppUser user = new AppUser("zbych", "pass", "zbych@email.com", roles);
        user.setId(1L);

        when(this.userRepository.getById(user.getId())).thenReturn(user);
        when(this.roleRepository.findByName("PREMIUM")).thenReturn(new Role("PREMIUM"));

        // act
        var resources = this.resourceService.getResources(user.getId());

        // assert
        assertEquals(new ArrayList<>(), resources);
    }

    @Test
    void getNonPremiumResources_userNonPremium_returnsResourcesListForUser() {
        // arrange
        Collection<Role> roles = new ArrayList<>();
        roles.add(new Role("BASIC"));
        AppUser user = new AppUser("zbych", "pass", "zbych@email.com", roles);
        user.setId(1L);
        var prices = new HashSet<Price>();
        prices.add(new Price(22.22, LocalDate.now()));

        ArrayList<Resource> resourceList = new ArrayList<>();
        resourceList.add(new Resource(1L, "GOLD", prices));
        resourceList.add(new Resource(2L, "SILVER", prices));

        when(this.userRepository.getById(user.getId())).thenReturn(user);
        when(this.roleRepository.findByName("BASIC")).thenReturn(new Role("BASIC"));
        when(this.resourceRepository.getById(1L)).thenReturn(resourceList.get(0));
        when(this.resourceRepository.findAll()).thenReturn(resourceList);

        // act
        var resources = this.resourceService.getResources(user.getId());

        // assert
        assertEquals(resources, List.of(resourceList.get(0)));
    }

    @Test
    void getPremiumResources_userPremium_returnsResourcesListForUser() {
        // arrange
        Collection<Role> roles = new ArrayList<>();
        roles.add(new Role("PREMIUM"));
        AppUser user = new AppUser("zbych", "pass", "zbych@email.com", roles);
        user.setId(1L);
        var prices = new HashSet<Price>();
        prices.add(new Price(22.22, LocalDate.now()));

        ArrayList<Resource> resourceList = new ArrayList<>();
        resourceList.add(new Resource(1L, "GOLD", prices));
        resourceList.add(new Resource(2L, "SILVER", prices));

        when(this.userRepository.getById(user.getId())).thenReturn(user);
        when(this.roleRepository.findByName("PREMIUM")).thenReturn(new Role("PREMIUM"));
        when(this.resourceRepository.getById(1L)).thenReturn(resourceList.get(0));
        when(this.resourceRepository.findAll()).thenReturn(resourceList);

        // act
        var resources = this.resourceService.getResources(user.getId());

        // assert
        assertEquals(resources, resourceList);
    }

    @Test
    void getPremiumResourcesFromDataRange_resourcesAndUserExists_userNONPremium_throwsForbiddenRequestException() {
        // arrange
        Collection<Role> roles = new ArrayList<>();
        roles.add(new Role("BASIC"));
        AppUser user = new AppUser("zbych", "pass", "zbych@email.com", roles);
        user.setId(1L);
        var prices = new HashSet<Price>();
        prices.add(new Price(22.21, LocalDate.now().plusDays(1)));
        prices.add(new Price(22.22, LocalDate.now()));
        prices.add(new Price(22.23, LocalDate.now().minusDays(1)));
        Resource resource = new Resource(2L, "GOLD", prices);

        when(this.userRepository.getById(user.getId())).thenReturn(user);
        when(this.roleRepository.findByName("BASIC")).thenReturn(new Role("BASIC"));
        when(this.resourceRepository.getById(resource.getId())).thenReturn(resource);
        when(this.resourceRepository.getResourceByIdAndPricesBetween(resource.getId(),
                LocalDate.now().minusDays(1), LocalDate.now().plusDays(1)))
                .thenReturn(prices);

        // act and assert
        assertThrows(ForbiddenRequestException.class, () -> this.resourceService.getResourcesFromDateRange(resource.getId(),
                LocalDate.now().minusDays(1).toString(),
                LocalDate.now().plusDays(1).toString(), user.getId()));

    }

    @Test
    void getSingleResource_resourceAndUserExists_returnsResourceForUser() {
        // arrange
        Collection<Role> roles = new ArrayList<>();
        roles.add(new Role("PREMIUM"));
        AppUser user = new AppUser("zbych", "pass", "zbych@email.com", roles);
        user.setId(1L);
        var prices = new HashSet<Price>();
        prices.add(new Price(22.22, LocalDate.now()));
        Resource resource = new Resource(1L, "GOLD", prices);

        when(this.userRepository.getById(user.getId())).thenReturn(user);
        when(this.roleRepository.findByName("BASIC")).thenReturn(new Role("BASIC"));
        when(this.resourceRepository.findById(resource.getId())).thenReturn(Optional.of(resource));

        // act
        var result = this.resourceService.getSingleResource(resource.getId(), user.getId());

        // assert
        assertEquals(resource, result);
    }

    @Test
    void getSinglePremiumResource_resourceAndUserExists_userNONPremium_throwsForbiddenRequestException() {
        // arrange
        Collection<Role> roles = new ArrayList<>();
        roles.add(new Role("BASIC"));
        AppUser user = new AppUser("zbych", "pass", "zbych@email.com", roles);
        user.setId(1L);
        var prices = new HashSet<Price>();
        prices.add(new Price(22.22, LocalDate.now()));
        Resource resource = new Resource(2L, "GOLD", prices);

        when(this.userRepository.getById(user.getId())).thenReturn(user);
        when(this.roleRepository.findByName("BASIC")).thenReturn(new Role("BASIC"));
        when(this.resourceRepository.getById(resource.getId())).thenReturn(resource);

        // act and assert
        assertThrows(ForbiddenRequestException.class, () -> this.resourceService.getSingleResource(resource.getId(), user.getId()));

    }

    @Test
    void getSingleResource_userExists_resourceNOTExists_throwsNotFoundException() {
        // arrange
        Collection<Role> roles = new ArrayList<>();
        roles.add(new Role("BASIC"));
        AppUser user = new AppUser("zbych", "pass", "zbych@email.com", roles);
        user.setId(1L);

        when(this.userRepository.getById(user.getId())).thenReturn(user);
        when(this.roleRepository.findByName("BASIC")).thenReturn(new Role("BASIC"));
        when(this.resourceRepository.findById(any())).thenReturn(Optional.empty());

        // act and assert
        assertThrows(NotFoundException.class, () -> this.resourceService.getSingleResource(-1L, user.getId()));
    }

}
