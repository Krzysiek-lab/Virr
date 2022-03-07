package Simulations;

import Simulations.Entity.Simulation;
import Simulations.Entity.SimulationsValues;
import Simulations.Repositories.SimulationRepository;
import Simulations.Repositories.SimulationsValuesRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DemoApplicationTests {


    @Autowired
    SimulationRepository simulationRepository;

    @Autowired
    SimulationsValuesRepository simulationsValuesRepository;

    @Autowired
    TestEntityManager testEntityManager;

    SimulationsValues l1 = new SimulationsValues();

    Simulation simulation1 = new Simulation();


    @Test
    public void it_should_save_Entity() {
        Simulation simulationEntity = new Simulation();
        simulationEntity.setSimulation_Name("TEST");
        simulationEntity.setPopulation_Size(BigDecimal.valueOf(1000000000));
        simulationEntity.setInitial_Infected_Number(BigDecimal.valueOf(1));
        simulationEntity.setHow_Many_One_Infects(BigDecimal.valueOf(2));
        simulationEntity.setMortality_Rate(BigDecimal.valueOf(0.9));
        simulationEntity.setNumber_Of_Days_To_Recovery(BigDecimal.valueOf(14));
        simulationEntity.setNumber_Of_Days_To_Death(BigDecimal.valueOf(12));
        simulationEntity.setSimulation_Time(BigDecimal.valueOf(17));
        Simulation simulation = simulationRepository.save(simulationEntity);

        Assertions.assertThat(simulation).hasFieldOrPropertyWithValue("simulation_Name", "TEST");
        Assertions.assertThat(simulation).hasFieldOrPropertyWithValue("population_Size", BigDecimal
                .valueOf(1000000000));
        Assertions.assertThat(simulation).hasFieldOrPropertyWithValue("initial_Infected_Number", BigDecimal
                .valueOf(1));
        Assertions.assertThat(simulation).hasFieldOrPropertyWithValue("how_Many_One_Infects", BigDecimal
                .valueOf(2));
        Assertions.assertThat(simulation).hasFieldOrPropertyWithValue("mortality_Rate", BigDecimal.valueOf(0.9));
        Assertions.assertThat(simulation).hasFieldOrPropertyWithValue("number_Of_Days_To_Recovery", BigDecimal
                .valueOf(14));
        Assertions.assertThat(simulation).hasFieldOrPropertyWithValue("number_Of_Days_To_Death", BigDecimal
                .valueOf(12));
        Assertions.assertThat(simulation).hasFieldOrPropertyWithValue("simulation_Time", BigDecimal.valueOf(17));
    }


    @Test
    public void should_find_all_Entities() {
        Simulation simulationEntity = new Simulation();
        simulationEntity.setSimulation_Name("TEST");
        simulationEntity.setPopulation_Size(BigDecimal.valueOf(1000000000));
        simulationEntity.setInitial_Infected_Number(BigDecimal.valueOf(1));
        simulationEntity.setHow_Many_One_Infects(BigDecimal.valueOf(2));
        simulationEntity.setMortality_Rate(BigDecimal.valueOf(0.9));
        simulationEntity.setNumber_Of_Days_To_Recovery(BigDecimal.valueOf(14));
        simulationEntity.setNumber_Of_Days_To_Death(BigDecimal.valueOf(12));
        simulationEntity.setSimulation_Time(BigDecimal.valueOf(17));
        testEntityManager.persist(simulationEntity);
        Iterable<Simulation> simulationEntities = simulationRepository.findAll();
        Assertions.assertThat(simulationEntities).hasSize(1).contains(simulationEntity);
    }


    @Test
    public void should_find_Entity_By_Name() {
        Simulation simulationEntity = new Simulation();
        simulationEntity.setSimulation_Name("TEST");
        simulationEntity.setPopulation_Size(BigDecimal.valueOf(1000000000));
        simulationEntity.setInitial_Infected_Number(BigDecimal.valueOf(1));
        simulationEntity.setHow_Many_One_Infects(BigDecimal.valueOf(2));
        simulationEntity.setMortality_Rate(BigDecimal.valueOf(0.9));
        simulationEntity.setNumber_Of_Days_To_Recovery(BigDecimal.valueOf(14));
        simulationEntity.setNumber_Of_Days_To_Death(BigDecimal.valueOf(12));
        simulationEntity.setSimulation_Time(BigDecimal.valueOf(17));
        testEntityManager.persist(simulationEntity);
        Iterable<Simulation> simulation = simulationRepository.findByName(simulationEntity.getSimulation_Name());
        Assertions.assertThat(simulation).hasSize(1).contains(simulationEntity);
    }


//    @Test
//    public void should_calculate_remaining_days() {
//        //given
//        EachRemainingDayService eachRemainingDayService =
//                new EachRemainingDayService(simulationRepository, simulationsValuesRepository);
//        DayOneService dayOneService = new DayOneService(simulationsValuesRepository, eachRemainingDayService
//                , simulationRepository);
//
//        //when
//
//        dayOneService.generateSimulationForDayOne(simulation1);
//
//        //then
//        Assertions.assertThat(!simulationRepository.findAll().isEmpty());
//        Assertions.assertThat(!simulationsValuesRepository.findAll().isEmpty());
//    }
//
//    @BeforeEach
//    void setUp() {
//
//        simulation1 = Simulation.builder()
//                .simulation_Name("TEST")
//                .population_Size(BigDecimal.valueOf(1000000000))
//                .initial_Infected_Number(BigDecimal.valueOf(1))
//                .how_Many_One_Infects(BigDecimal.valueOf(2))
//                .mortality_Rate(BigDecimal.valueOf(0.9))
//                .number_Of_Days_To_Recovery(BigDecimal.valueOf(14))
//                .number_Of_Days_To_Death(BigDecimal.valueOf(12))
//                .simulation_Time(BigDecimal.valueOf(17))
//                .simulationsValues(List.of(l1))
//                .build();
//        simulationRepository.save(simulation1);
//
//        l1 = SimulationsValues.builder()
//                .id(1L)
//                .day(BigDecimal.valueOf(10))
//                .number_Of_Infected(BigDecimal.valueOf(10))
//                .healthy_Prone_To_Infection(BigDecimal.valueOf(10))
//                .regained_Health_And_Immunity(BigDecimal.valueOf(10))
//                .simulation(simulation1)
//                .build();
//        simulationsValuesRepository.save(l1);
//    }

}
