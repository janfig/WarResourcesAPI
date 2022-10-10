package unit.service;

import com.example.warresourcesapi.model.AppUser;
import com.example.warresourcesapi.model.Role;
import com.example.warresourcesapi.repository.ResourceRepository;
import com.example.warresourcesapi.repository.RoleRepository;
import com.example.warresourcesapi.repository.UserRepository;
import com.example.warresourcesapi.service.ResourceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
}
