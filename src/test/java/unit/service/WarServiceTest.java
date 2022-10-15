package unit.service;

import com.example.warresourcesapi.model.War;
import com.example.warresourcesapi.repository.WarRepository;
import com.example.warresourcesapi.service.WarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class WarServiceTest {
    private WarRepository warRepository = null;

    private WarService warService = null;

    @BeforeEach
    void setup() {
        this.warRepository = Mockito.mock(WarRepository.class);

        this.warService = new WarService(
                this.warRepository
        );
    }

    @Test
    void getWars_thereAreWars_returnsWars() {
        // arrange
        War war = new War();
        war.setId(1L);
        war.setName("War 1");
        war.setStartDate(LocalDate.now().minusDays(2));
        war.setEndDate(LocalDate.now());

        ArrayList<War> wars = new ArrayList<>();
        wars.add(war);

        when(this.warRepository.count()).thenReturn(1L);
        when(this.warRepository.findAll()).thenReturn(wars);

        // act
        List<War> result;
        try {
            result = this.warService.getWars();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        // assert
        assertEquals(wars, result);
    }
}
