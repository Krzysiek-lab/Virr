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


        //pozyskanie najnowszej symulacji by miec jej paramtery wejsciowe
        var sim = getSimulations();
        var newestSimulation = simulationRepository.getById(sim.get(sim.size() - 1).getId());


        //pozyskanie najnowszej symulacji by miec jej wartosci
        var simVal = getSimulationsValues();
        var newestSimulationsVal = simulationsValuesRepository
                .getById(simVal.get(simVal.size() - 1).getId());


        //zapisanie do bazy TO LOGIKA!!!
        for (long i = 2; i < newestSimulation.getSimulation_Time(); i++) {
            if (newestSimulationsVal.getHealthy_Prone_To_Infection() <= 0) {
                break;
            }

            if (i >= newestSimulation.getNumber_Of_Days_To_Recovery()) {

/////////////////////////
                sim = getSimulations();
                newestSimulation = simulationRepository.getById(sim.get(sim.size() - 1).getId());


                simVal = getSimulationsValues();
                newestSimulationsVal = simulationsValuesRepository
                        .getById(simVal.get(simVal.size() - 1).getId());
////////////////////////

                SimulationsValues simulation_values;
                simulation_values = SimulationsValues.builder()
                        .day(i)// ok

                        .healthy_Prone_To_Infection(newestSimulation.getPopulation_Size() -
                                ((newestSimulation.getHow_Many_One_Infects() + 1)
                                        * newestSimulationsVal.getNumber_Of_Infected()))// ok

                        .number_Of_Infected((long) ((long) ((newestSimulation.getHow_Many_One_Infects() + 1)
                                * newestSimulationsVal.getNumber_Of_Infected())
                                - (newestSimulation.getMortality_Rate()//L24
                                * (simulationsValuesRepository.findAll().get((int) (simulationsValuesRepository.findAll()
                                .get((simulationsValuesRepository.findAll().size() - 1))
                                .getDay() - newestSimulation.getNumber_Of_Days_To_Death())).getNumber_Of_Infected()))//V24

                                - (1 - newestSimulation.getMortality_Rate()
                                * (simulationsValuesRepository.findAll().get((int) (simulationsValuesRepository
                                .findAll().get((simulationsValuesRepository.findAll().size() - 1))
                                .getDay() - newestSimulation.getNumber_Of_Days_To_Recovery()))
                                .getNumber_Of_Infected()))))//Z24


                        .regained_Health_And_Immunity((long) ((1 - newestSimulation.getMortality_Rate()
                                * (simulationsValuesRepository.findAll().get((int) (simulationsValuesRepository
                                .findAll().get((simulationsValuesRepository.findAll().size() - 1))
                                .getDay() - newestSimulation.getNumber_Of_Days_To_Recovery()))
                                .getNumber_Of_Infected()))))// Z24


                        .dead((long) (newestSimulation.getMortality_Rate()//L24
                                * (simulationsValuesRepository.findAll().get((int) (simulationsValuesRepository
                                .findAll().get((simulationsValuesRepository.findAll().size() - 1))
                                .getDay() - newestSimulation.getNumber_Of_Days_To_Death())).getNumber_Of_Infected())))//V24

                        .build();

                var simId = simulationsValuesRepository.save(simulation_values);
                var simVal2 = newestSimulation.getSimulationsValues();
                simVal2.add(simId);
                newestSimulation.setSimulationsValues(simVal);
                simulationRepository.save(newestSimulation);

            } else {

                /////////////////////////
                sim = getSimulations();
                newestSimulation = simulationRepository.getById(sim.get(sim.size() - 1).getId());


                simVal = getSimulationsValues();
                newestSimulationsVal = simulationsValuesRepository
                        .getById(simVal.get(simVal.size() - 1).getId());
                ////////////////////////


                SimulationsValues simulation_values;
                simulation_values = SimulationsValues.builder()
                        .day(i)// ok
                        .healthy_Prone_To_Infection(newestSimulation.getPopulation_Size() -
                                ((newestSimulation.getHow_Many_One_Infects() + 1)
                                        * newestSimulationsVal.getNumber_Of_Infected()))// ok

                        .number_Of_Infected((long) ((newestSimulation.getHow_Many_One_Infects())
                                * newestSimulationsVal.getNumber_Of_Infected()
                                + newestSimulationsVal.getNumber_Of_Infected()))//moze

                        .regained_Health_And_Immunity(0L)// ok
                        .dead(0L)// ok
                        .build();

                var simId2 = simulationsValuesRepository.save(simulation_values);
                var simVal2 = newestSimulation.getSimulationsValues();
                simVal2.add(simId2);
                newestSimulation.setSimulationsValues(simVal);
                simulationRepository.save(newestSimulation);

            }
        }

        simulationsValuesRepository.delete(simulationsValuesRepository.findAll().get(0));//szopka cd
    }


}
