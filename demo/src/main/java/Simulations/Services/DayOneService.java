package Simulations.Services;

import Simulations.Annotations.MyAnnotation;
import Simulations.Entity.Simulation;
import Simulations.Entity.SimulationsValues;
import Simulations.Repositories.SimulationRepository;
import Simulations.Repositories.SimulationsValuesRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.List;

@Configuration
@AllArgsConstructor
public class DayOneService {


    private final SimulationsValuesRepository simulationsValuesRepository;
    private final EachRemainingDayService eachRemainingDayService;
    private final SimulationRepository simulationRepository;


    //1
    public void generateSimulationValues(String N, Double P, Long I, Double R, Double M, Long Ti, Long Tm, Long Ts,
                                         List<SimulationsValues> list) {
        var simulation = Simulation.builder()
                .simulation_Name(N)
                .population_Size(P)
                .initial_Infected_Number(I)
                .how_Many_One_Infects(R)
                .mortality_Rate(M)
                .number_Of_Days_To_Recovery(Ti)
                .number_Of_Days_To_Death(Tm)
                .simulation_Time(Ts)
                .simulationsValues(list)
                .build();
        generateSimulationForDayOne(simulation);
    }


    //2
    @Bean
    @Lazy()
    @MyAnnotation
    public String generateSimulationForDayOne(Simulation simulation) {
        var sim = eachRemainingDayService.getSimulations();
        var simulations = simulationRepository.getById(sim.get(sim.size() - 1).getId());

        var simulation_values = SimulationsValues.builder()
                .day(1L)
                .healthy_Prone_To_Infection(simulation.getHow_Many_One_Infects())
                .number_Of_Infected(simulation.getInitial_Infected_Number())
                .dead(0L)
                .regained_Health_And_Immunity(0L)
                .build();
        var s = simulationsValuesRepository.save(simulation_values);
        var s2 = simulationsValuesRepository.save(simulation_values);//szopka poczatek

        var simm = simulations.getSimulationsValues();
        simm.add(s);
        simm.add(s2);//szopka cd.
        simulations.setSimulationsValues(simm);
        simulationRepository.save(simulations);
        return "";
    }


    //@PostConstruct
    //@DependsOn("Simulation")
    //@Order(101)
    //@Scope(scopeName = "request")
    //@RequestScope
}
