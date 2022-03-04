package Simulations.Services;

import Simulations.Entity.Simulation;
import Simulations.Entity.SimulationsValues;
import Simulations.Repositories.SimulationRepository;
import Simulations.Repositories.SimulationsValuesRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
@Aspect
public class EachRemainingDayService {


    private final SimulationRepository simulationRepository;
    private final SimulationsValuesRepository simulationsValuesRepository;


    @Pointcut("@annotation(Simulations.Annotations.MyAnnotation)")
    public void MyAnnotationMethod() {
    }


    public List<Simulation> getSimulations() {
        List<Simulation> listOdIds = new ArrayList<>(simulationRepository.findAll());
        listOdIds.sort(Comparator.comparing(Simulation::getId));
        return listOdIds;
    }

    public List<SimulationsValues> getSimulationsValues() {
        List<SimulationsValues> listOdIdsOfValues = new ArrayList<>(simulationsValuesRepository.findAll());
        listOdIdsOfValues.sort(Comparator.comparing(SimulationsValues::getId));
        return listOdIdsOfValues;
    }


    //3 zapis wynikow do bazy
    @After("MyAnnotationMethod()")// zmienilem z Before
    public void SimulationParametersForRemainingDays() {


        var sim = getSimulations();
        var newestSimulation = simulationRepository.getById(sim.get(sim.size() - 1).getId());

        var simVal = getSimulationsValues();
        var newestSimulationsVal = simulationsValuesRepository
                .getById(simVal.get(simVal.size() - 1).getId());


        //zapisanie do bazy TO LOGIKA!!!
        for (double i = 2; i <= newestSimulation.getSimulation_Time().doubleValue(); i++) {
            if (newestSimulationsVal.getHealthy_Prone_To_Infection().doubleValue() <= 0) {
                break;
            }

            if (i >= newestSimulation.getNumber_Of_Days_To_Death().doubleValue()
                    && i < newestSimulation.getNumber_Of_Days_To_Recovery().doubleValue()) {

                sim = getSimulations();
                newestSimulation = simulationRepository.getById(sim.get(sim.size() - 1).getId());

                simVal = getSimulationsValues();
                newestSimulationsVal = simulationsValuesRepository
                        .getById(simVal.get(simVal.size() - 1).getId());

                SimulationsValues simulation_values;
                Simulation finalNewestSimulation1 = newestSimulation;
                double finalI = i;


                simulation_values = SimulationsValues.builder()
                        .day(BigDecimal.valueOf(i))
                        .healthy_Prone_To_Infection(newestSimulation.getPopulation_Size()
                                .subtract((((newestSimulation.getHow_Many_One_Infects()).multiply(newestSimulationsVal
                                        .getNumber_Of_Infected()
                                        .add(newestSimulationsVal.getNumber_Of_Infected())))))
                        )


                        .number_Of_Infected((((newestSimulation.getHow_Many_One_Infects()).multiply(newestSimulationsVal
                                .getNumber_Of_Infected()).add(newestSimulationsVal.getNumber_Of_Infected())).subtract
                                (newestSimulation.getMortality_Rate().multiply(BigDecimal.valueOf((simulationsValuesRepository
                                        .findAll().stream().filter(e -> e.getDay().doubleValue() == finalI + 1 - finalNewestSimulation1
                                                .getNumber_Of_Days_To_Death().doubleValue()))
                                        .collect(Collectors.toList()).get((int) (simulationsValuesRepository.findAll().stream()
                                                .filter(e -> e.getDay().doubleValue() == (finalI + 1 - finalNewestSimulation1
                                                        .getNumber_Of_Days_To_Death().doubleValue()))).count() - 1).getNumber_Of_Infected().doubleValue())))))//V24


                        .regained_Health_And_Immunity(BigDecimal.valueOf(0))// Z24


                        .dead((newestSimulation.getMortality_Rate().multiply(BigDecimal.valueOf((simulationsValuesRepository
                                .findAll().stream().filter(e -> e.getDay().doubleValue() == finalI + 1 - finalNewestSimulation1
                                        .getNumber_Of_Days_To_Death().doubleValue()))
                                .collect(Collectors.toList()).get((int) (simulationsValuesRepository.findAll().stream()
                                        .filter(e -> e.getDay().doubleValue() == (finalI + 1 - finalNewestSimulation1
                                                .getNumber_Of_Days_To_Death().doubleValue()))).count() - 1)
                                .getNumber_Of_Infected().doubleValue()))).subtract(newestSimulationsVal.getDead())
                        )//V24

                        .build();


                var simId = simulationsValuesRepository.save(simulation_values);
                simId.setSimulation(newestSimulation);//dodane
                var simVal2 = newestSimulation.getSimulationsValues();
                simVal2.add(simId);
                newestSimulation.setSimulationsValues(simVal2);
                simulationRepository.save(newestSimulation);

            } else if (i >= newestSimulation.getNumber_Of_Days_To_Recovery().doubleValue()) {

                sim = getSimulations();
                newestSimulation = simulationRepository.getById(sim.get(sim.size() - 1).getId());

                simVal = getSimulationsValues();
                newestSimulationsVal = simulationsValuesRepository
                        .getById(simVal.get(simVal.size() - 1).getId());


                Simulation finalNewestSimulation1 = newestSimulation;
                double finalI = i;
                SimulationsValues simulation_values;


                simulation_values = SimulationsValues.builder()
                        .day(BigDecimal.valueOf(i))
                        .healthy_Prone_To_Infection(newestSimulation.getPopulation_Size().subtract
                                ((newestSimulation.getHow_Many_One_Infects()).multiply(newestSimulationsVal
                                        .getNumber_Of_Infected())))


                        .number_Of_Infected(((newestSimulation.getHow_Many_One_Infects()).multiply(newestSimulationsVal
                                .getNumber_Of_Infected()).add(newestSimulationsVal.getNumber_Of_Infected())).subtract
                                (newestSimulation.getMortality_Rate().multiply(BigDecimal.valueOf((simulationsValuesRepository
                                        .findAll().stream().filter(e -> e.getDay().doubleValue() == finalI + 1 - finalNewestSimulation1
                                                .getNumber_Of_Days_To_Death().doubleValue()))
                                        .collect(Collectors.toList()).get((int) (simulationsValuesRepository.findAll().stream()
                                                .filter(e -> e.getDay().doubleValue() == (finalI + 1 - finalNewestSimulation1
                                                        .getNumber_Of_Days_To_Death().doubleValue()))).count() - 1).getNumber_Of_Infected().doubleValue())).subtract

                                        (((BigDecimal.valueOf(1).subtract(newestSimulation.getMortality_Rate())).multiply(simulationsValuesRepository
                                                .findAll().stream().filter(e -> e.getDay().doubleValue() == finalI + 1 - finalNewestSimulation1
                                                        .getNumber_Of_Days_To_Recovery().doubleValue())
                                                .collect(Collectors.toList()).get((int) (simulationsValuesRepository.findAll().stream()
                                                        .filter(e -> e.getDay().doubleValue() == (finalI + 1 - finalNewestSimulation1
                                                                .getNumber_Of_Days_To_Recovery().doubleValue())).count() - 1)
                                                ).getNumber_Of_Infected())))))


                        .regained_Health_And_Immunity((BigDecimal.valueOf(1).subtract(newestSimulation.getMortality_Rate())).multiply
                                (simulationsValuesRepository.findAll()
                                        .stream().filter(e -> e.getDay().doubleValue() == finalI + 1 - finalNewestSimulation1
                                                .getNumber_Of_Days_To_Recovery().doubleValue())
                                        .collect(Collectors.toList()).get((int) (simulationsValuesRepository.findAll()
                                                .stream().filter(e -> e.getDay().doubleValue() == finalI + 1 - finalNewestSimulation1
                                                        .getNumber_Of_Days_To_Recovery().doubleValue())).count() - 1)
                                        .getNumber_Of_Infected()))// Z24


                        .dead((newestSimulation.getMortality_Rate().multiply(BigDecimal.valueOf((
                                simulationsValuesRepository.findAll()
                                        .stream().filter(e -> e.getDay().doubleValue() == finalI + 1 - finalNewestSimulation1
                                        .getNumber_Of_Days_To_Death().doubleValue()))
                                .collect(Collectors.toList()).get((int) (simulationsValuesRepository.findAll().stream()
                                        .filter(e -> e.getDay().doubleValue() == (finalI + 1 - finalNewestSimulation1
                                                .getNumber_Of_Days_To_Death().doubleValue()))).count() - 1)
                                .getNumber_Of_Infected().doubleValue()))).subtract(newestSimulationsVal.getDead())
                        )//V24

                        .build();


                var simId = simulationsValuesRepository.save(simulation_values);
                simId.setSimulation(newestSimulation);//dodane
                var simVal2 = newestSimulation.getSimulationsValues();
                simVal2.add(simId);
                newestSimulation.setSimulationsValues(simVal2);
                simulationRepository.save(newestSimulation);

            } else if (i == 2) {
                sim = getSimulations();
                newestSimulation = simulationRepository.getById(sim.get(sim.size() - 1).getId());

                simVal = getSimulationsValues();
                newestSimulationsVal = simulationsValuesRepository
                        .getById(simVal.get(simVal.size() - 1).getId());


                SimulationsValues simulation_values;
                simulation_values = SimulationsValues.builder()
                        .day(BigDecimal.valueOf(i))
                        .healthy_Prone_To_Infection(newestSimulationsVal.getHealthy_Prone_To_Infection().subtract
                                ((newestSimulation.getHow_Many_One_Infects()).multiply(newestSimulationsVal
                                        .getNumber_Of_Infected())))
                        .number_Of_Infected((newestSimulation.getHow_Many_One_Infects()).multiply(newestSimulationsVal
                                .getNumber_Of_Infected()).add(newestSimulationsVal.getNumber_Of_Infected()))
                        .regained_Health_And_Immunity(BigDecimal.valueOf(0))
                        .dead(BigDecimal.valueOf(0))
                        .build();

                var simId2 = simulationsValuesRepository.save(simulation_values);
                simId2.setSimulation(newestSimulation);//dodane
                var simVal2 = newestSimulation.getSimulationsValues();
                simVal2.add(simId2);
                newestSimulation.setSimulationsValues(simVal2);
                simulationRepository.save(newestSimulation);

            } else {
                sim = getSimulations();
                newestSimulation = simulationRepository.getById(sim.get(sim.size() - 1).getId());

                simVal = getSimulationsValues();
                newestSimulationsVal = simulationsValuesRepository
                        .getById(simVal.get(simVal.size() - 1).getId());


                SimulationsValues simulation_values;
                simulation_values = SimulationsValues.builder()
                        .day(BigDecimal.valueOf(i))
                        .healthy_Prone_To_Infection(newestSimulationsVal.getHealthy_Prone_To_Infection().subtract
                                ((newestSimulation.getHow_Many_One_Infects()).multiply(newestSimulationsVal
                                        .getNumber_Of_Infected())))
                        .number_Of_Infected((newestSimulation.getHow_Many_One_Infects()).multiply(newestSimulationsVal
                                .getNumber_Of_Infected())
                                .add(newestSimulationsVal.getNumber_Of_Infected()))
                        .regained_Health_And_Immunity(BigDecimal.valueOf(0))
                        .dead(BigDecimal.valueOf(0))
                        .build();

                var simId2 = simulationsValuesRepository.save(simulation_values);
                simId2.setSimulation(newestSimulation);//dodane
                var simVal2 = newestSimulation.getSimulationsValues();
                simVal2.add(simId2);
                newestSimulation.setSimulationsValues(simVal2);
                simulationRepository.save(newestSimulation);

            }
        }

    }


}
