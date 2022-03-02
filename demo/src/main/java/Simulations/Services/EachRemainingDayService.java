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


    public List<Simulation> getSimulations() { // usunac public
        List<Simulation> listOdIds = new ArrayList<>(simulationRepository.findAll());
        listOdIds.sort(Comparator.comparing(Simulation::getId));
        return listOdIds;
    }

    public List<SimulationsValues> getSimulationsValues() {// usunac public
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
        for (double i = 2; i <= newestSimulation.getSimulation_Time(); i++) {
            if (newestSimulationsVal.getHealthy_Prone_To_Infection() <= 0) {
                break;
            }

            if (i >= newestSimulation.getNumber_Of_Days_To_Death()
                    && i < newestSimulation.getNumber_Of_Days_To_Recovery()) {

                sim = getSimulations();
                newestSimulation = simulationRepository.getById(sim.get(sim.size() - 1).getId());

                simVal = getSimulationsValues();
                newestSimulationsVal = simulationsValuesRepository
                        .getById(simVal.get(simVal.size() - 1).getId());

                SimulationsValues simulation_values;
                Simulation finalNewestSimulation1 = newestSimulation;
                double finalI = i;

                //////////////

//                var tt = (newestSimulation.getPopulation_Size() -
//                        (((newestSimulation.getHow_Many_One_Infects())
//                                * newestSimulationsVal.getNumber_Of_Infected()
//                                + newestSimulationsVal.getNumber_Of_Infected())));
//
//
//                var w = (newestSimulation.getMortality_Rate() * (double) ((simulationsValuesRepository
//                        .findAll().stream().filter(e -> e.getDay() == finalI + 1 - finalNewestSimulation1
//                                .getNumber_Of_Days_To_Death()))
//                        .collect(Collectors.toList()).get((int) (simulationsValuesRepository.findAll().stream()
//                                .filter(e -> e.getDay() == (finalI + 1 - finalNewestSimulation1
//                                        .getNumber_Of_Days_To_Death()))).count() - 1).getNumber_Of_Infected()));
//
//                var dd = ((((newestSimulation.getHow_Many_One_Infects())
//                        * newestSimulationsVal.getNumber_Of_Infected()
//                        + newestSimulationsVal.getNumber_Of_Infected())//L24
//
//                        - (newestSimulation.getMortality_Rate() * (double) ((simulationsValuesRepository
//                        .findAll().stream().filter(e -> e.getDay() == finalI + 1 - finalNewestSimulation1
//                                .getNumber_Of_Days_To_Death()))
//                        .collect(Collectors.toList()).get((int) (simulationsValuesRepository.findAll().stream()
//                                .filter(e -> e.getDay() == (finalI + 1 - finalNewestSimulation1
//                                        .getNumber_Of_Days_To_Death()))).count() - 1).getNumber_Of_Infected()))));
                /////////////////


                simulation_values = SimulationsValues.builder()
                        .day(i)
                        .healthy_Prone_To_Infection(newestSimulation.getPopulation_Size() -
                                (((newestSimulation.getHow_Many_One_Infects())
                                        * newestSimulationsVal.getNumber_Of_Infected()
                                        + newestSimulationsVal.getNumber_Of_Infected())))


                        .number_Of_Infected((((newestSimulation.getHow_Many_One_Infects())
                                * newestSimulationsVal.getNumber_Of_Infected()
                                + newestSimulationsVal.getNumber_Of_Infected())//L24
                                - (newestSimulation.getMortality_Rate() * (double) ((simulationsValuesRepository
                                .findAll().stream().filter(e -> e.getDay() == finalI + 1 - finalNewestSimulation1
                                        .getNumber_Of_Days_To_Death()))
                                .collect(Collectors.toList()).get((int) (simulationsValuesRepository.findAll().stream()
                                        .filter(e -> e.getDay() == (finalI + 1 - finalNewestSimulation1
                                                .getNumber_Of_Days_To_Death()))).count() - 1).getNumber_Of_Infected()))))//V24


                        .regained_Health_And_Immunity(0d)// Z24


                        .dead(newestSimulation.getMortality_Rate() * (double) ((simulationsValuesRepository
                                .findAll().stream().filter(e -> e.getDay() == finalI + 1 - finalNewestSimulation1
                                        .getNumber_Of_Days_To_Death()))
                                .collect(Collectors.toList()).get((int) (simulationsValuesRepository.findAll().stream()
                                        .filter(e -> e.getDay() == (finalI + 1 - finalNewestSimulation1
                                                .getNumber_Of_Days_To_Death()))).count() - 1).getNumber_Of_Infected()))//V24

                        .build();


                var simId = simulationsValuesRepository.save(simulation_values);
                var simVal2 = newestSimulation.getSimulationsValues();
                simVal2.add(simId);
                newestSimulation.setSimulationsValues(simVal2);
                simulationRepository.save(newestSimulation);

            } else if (i >= newestSimulation.getNumber_Of_Days_To_Recovery()) {

                sim = getSimulations();
                newestSimulation = simulationRepository.getById(sim.get(sim.size() - 1).getId());

                simVal = getSimulationsValues();
                newestSimulationsVal = simulationsValuesRepository
                        .getById(simVal.get(simVal.size() - 1).getId());


                Simulation finalNewestSimulation1 = newestSimulation;
                double finalI = i;
                SimulationsValues simulation_values;


                simulation_values = SimulationsValues.builder()
                        .day(i)
                        .healthy_Prone_To_Infection(newestSimulation.getPopulation_Size() -
                                ((newestSimulation.getHow_Many_One_Infects())
                                        * newestSimulationsVal.getNumber_Of_Infected()))


                        .number_Of_Infected(((newestSimulation.getHow_Many_One_Infects())
                                * newestSimulationsVal.getNumber_Of_Infected()
                                + newestSimulationsVal.getNumber_Of_Infected())//L24

                                - (newestSimulation.getMortality_Rate() * (double) ((simulationsValuesRepository
                                .findAll().stream().filter(e -> e.getDay() == finalI + 1 - finalNewestSimulation1
                                        .getNumber_Of_Days_To_Death()))
                                .collect(Collectors.toList()).get((int) (simulationsValuesRepository.findAll().stream()
                                        .filter(e -> e.getDay() == (finalI + 1 - finalNewestSimulation1
                                                .getNumber_Of_Days_To_Death()))).count() - 1).getNumber_Of_Infected())//V24

                                - (((1 - newestSimulation.getMortality_Rate()) * (simulationsValuesRepository
                                .findAll().stream().filter(e -> e.getDay() == finalI + 1 - finalNewestSimulation1
                                        .getNumber_Of_Days_To_Recovery()))
                                .collect(Collectors.toList()).get((int) (simulationsValuesRepository.findAll().stream()
                                        .filter(e -> e.getDay() == finalI + 1 - finalNewestSimulation1
                                                .getNumber_Of_Days_To_Recovery())).count() - 1)
                                .getNumber_Of_Infected()))//Z24
                        ))


                        .regained_Health_And_Immunity((1 - newestSimulation.getMortality_Rate()) *
                                (simulationsValuesRepository.findAll()
                                        .stream().filter(e -> e.getDay() == finalI + 1 - finalNewestSimulation1
                                                .getNumber_Of_Days_To_Recovery()))
                                        .collect(Collectors.toList()).get((int) (simulationsValuesRepository.findAll()
                                        .stream()
                                        .filter(e -> e.getDay() == finalI + 1 - finalNewestSimulation1
                                                .getNumber_Of_Days_To_Recovery())).count() - 1)
                                        .getNumber_Of_Infected())// Z24


                        .dead(newestSimulation.getMortality_Rate() * (double) ((simulationsValuesRepository.findAll()
                                .stream().filter(e -> e.getDay() == finalI + 1 - finalNewestSimulation1
                                        .getNumber_Of_Days_To_Death()))
                                .collect(Collectors.toList()).get((int) (simulationsValuesRepository.findAll().stream()
                                        .filter(e -> e.getDay() == (finalI + 1 - finalNewestSimulation1
                                                .getNumber_Of_Days_To_Death()))).count() - 1).getNumber_Of_Infected()))//V24

                        .build();

                //////////////
//                var doinfekcji = (newestSimulation.getPopulation_Size() -
//                        ((newestSimulation.getHow_Many_One_Infects())
//                                * newestSimulationsVal.getNumber_Of_Infected()));
//
//                var zarazeni = (((newestSimulation.getHow_Many_One_Infects())
//                        * newestSimulationsVal.getNumber_Of_Infected()
//                        + newestSimulationsVal.getNumber_Of_Infected())//L24
//
//                        - (newestSimulation.getMortality_Rate() * (double) ((simulationsValuesRepository
//                        .findAll().stream().filter(e -> e.getDay() == finalI + 1 - finalNewestSimulation1
//                                .getNumber_Of_Days_To_Death()))
//                        .collect(Collectors.toList()).get((int) (simulationsValuesRepository.findAll().stream()
//                                .filter(e -> e.getDay() == (finalI + 1 - finalNewestSimulation1
//                                        .getNumber_Of_Days_To_Death()))).count() - 1).getNumber_Of_Infected())//V24
//
//                        - (((1 - newestSimulation.getMortality_Rate()) * (simulationsValuesRepository
//                        .findAll().stream().filter(e -> e.getDay() == finalI + 1 - finalNewestSimulation1
//                                .getNumber_Of_Days_To_Recovery()))
//                        .collect(Collectors.toList()).get((int) (simulationsValuesRepository.findAll().stream()
//                                .filter(e -> e.getDay() == finalI + 1 - finalNewestSimulation1
//                                        .getNumber_Of_Days_To_Recovery())).count() - 1)
//                        .getNumber_Of_Infected()))//Z24
//                ));
//
//
//                var hel = (((1 - newestSimulation.getMortality_Rate()) * (simulationsValuesRepository
//                        .findAll().stream().filter(e -> e.getDay() == finalI + 1 - finalNewestSimulation1
//                                .getNumber_Of_Days_To_Recovery()))
//                        .collect(Collectors.toList()).get((int) (simulationsValuesRepository.findAll().stream()
//                                .filter(e -> e.getDay() == finalI + 1 - finalNewestSimulation1
//                                        .getNumber_Of_Days_To_Recovery())).count() - 1)
//                        .getNumber_Of_Infected()));
//
//
//                var ded = (newestSimulation.getMortality_Rate() * (double) ((simulationsValuesRepository.findAll()
//                        .stream().filter(e -> e.getDay() == finalI + 1 - finalNewestSimulation1
//                                .getNumber_Of_Days_To_Death()))
//                        .collect(Collectors.toList()).get((int) (simulationsValuesRepository.findAll().stream()
//                                .filter(e -> e.getDay() == (finalI + 1 - finalNewestSimulation1
//                                        .getNumber_Of_Days_To_Death()))).count() - 1).getNumber_Of_Infected()));
                /////////////

                var simId = simulationsValuesRepository.save(simulation_values);
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
                        .day(i)
                        .healthy_Prone_To_Infection(newestSimulationsVal.getHealthy_Prone_To_Infection() -
                                ((newestSimulation.getHow_Many_One_Infects())
                                        * newestSimulationsVal.getNumber_Of_Infected()))
                        .number_Of_Infected((newestSimulation.getHow_Many_One_Infects())
                                * newestSimulationsVal.getNumber_Of_Infected()
                                + newestSimulationsVal.getNumber_Of_Infected())
                        .regained_Health_And_Immunity(0d)
                        .dead(0d)
                        .build();

                var simId2 = simulationsValuesRepository.save(simulation_values);
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
                        .day(i)
                        .healthy_Prone_To_Infection(newestSimulationsVal.getHealthy_Prone_To_Infection() -
                                ((newestSimulation.getHow_Many_One_Infects())
                                        * newestSimulationsVal.getNumber_Of_Infected()))
                        .number_Of_Infected((newestSimulation.getHow_Many_One_Infects())
                                * newestSimulationsVal.getNumber_Of_Infected()
                                + newestSimulationsVal.getNumber_Of_Infected())
                        .regained_Health_And_Immunity(0d)
                        .dead(0d)
                        .build();

                var simId2 = simulationsValuesRepository.save(simulation_values);
                var simVal2 = newestSimulation.getSimulationsValues();
                simVal2.add(simId2);
                newestSimulation.setSimulationsValues(simVal2);
                simulationRepository.save(newestSimulation);

            }
        }

    }


}
