package Simulations.Services;

import Simulations.Entity.Simulation;
import Simulations.Entity.SimulationsValues;
import Simulations.Repositories.SimulationRepository;
import Simulations.Repositories.SimulationsValuesRepository;
import Simulations.ViewModels.SimulationViewModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SimulationService {

    private final SimulationRepository simulationRepository;
    private final SimulationsValuesRepository simulationsValuesRepository;
    private final DayOneService simulationService;
    private final EachRemainingDayService eachRemainingDayService;


    public void addSimulation(SimulationViewModel simulationViewModel) {
        Simulation simulation = Simulation.builder()
                .simulation_Name(simulationViewModel.getSimulation_Name())
                .simulation_Time(simulationViewModel.getSimulation_Time())
                .population_Size(simulationViewModel.getPopulation_Size())
                .initial_Infected_Number(simulationViewModel.getInitial_Infected_Number())
                .how_Many_One_Infects(simulationViewModel.getHow_Many_One_Infects())
                .mortality_Rate(simulationViewModel.getMortality_Rate())
                .number_Of_Days_To_Recovery(simulationViewModel.getNumber_Of_Days_To_Recovery())
                .number_Of_Days_To_Death(simulationViewModel.getNumber_Of_Days_To_Death())
                .simulationsValues(simulationViewModel.getSimulationsValues())
                .build();
        simulationRepository.save(simulation);

        simulationService.generateSimulationValues(simulation.getSimulation_Name(), simulation.getPopulation_Size()
                , simulation.getInitial_Infected_Number(), simulation.getHow_Many_One_Infects()
                , simulation.getMortality_Rate(), simulation.getNumber_Of_Days_To_Recovery()
                , simulation.getNumber_Of_Days_To_Death(), simulation.getSimulation_Time(),
                simulation.getSimulationsValues());
    }


    public void updateSimulation(SimulationViewModel simulationViewModel) {
        var simulation = simulationRepository.getById(simulationViewModel.getId());

        simulation.setSimulation_Name(simulationViewModel.getSimulation_Name());//ok
        simulation.setSimulation_Time(simulationViewModel.getSimulation_Time());//ok
        simulation.setPopulation_Size(simulationViewModel.getPopulation_Size());//ok
        simulation.setInitial_Infected_Number(simulationViewModel.getInitial_Infected_Number());//ok
        simulation.setHow_Many_One_Infects(simulationViewModel.getHow_Many_One_Infects());//ok
        simulation.setMortality_Rate(simulationViewModel.getMortality_Rate());//ok
        simulation.setNumber_Of_Days_To_Recovery(simulationViewModel.getNumber_Of_Days_To_Recovery());//ok
        simulation.setNumber_Of_Days_To_Death(simulationViewModel.getNumber_Of_Days_To_Death());//ok
        simulation.setSimulationsValues(simulationViewModel.getSimulationsValues());

        simulationRepository.save(simulation);

        simulationService.generateSimulationValues(simulation.getSimulation_Name(), simulation.getPopulation_Size()
                , simulation.getInitial_Infected_Number(), simulation.getHow_Many_One_Infects()
                , simulation.getMortality_Rate(), simulation.getNumber_Of_Days_To_Recovery()
                , simulation.getNumber_Of_Days_To_Death(), simulation.getSimulation_Time(),
                simulation.getSimulationsValues()
        );
    }


    public SimulationViewModel simulationToViewModel(Simulation simulation) {

        return SimulationViewModel.builder()
                .id(simulation.getId())
                .simulation_Name(simulation.getSimulation_Name())
                .how_Many_One_Infects(simulation.getHow_Many_One_Infects())
                .mortality_Rate(simulation.getMortality_Rate())
                .initial_Infected_Number(simulation.getInitial_Infected_Number())
                .number_Of_Days_To_Death(simulation.getNumber_Of_Days_To_Death())
                .population_Size(simulation.getPopulation_Size())
                .number_Of_Days_To_Recovery(simulation.getNumber_Of_Days_To_Recovery())
                .simulation_Time(simulation.getSimulation_Time())
                .simulationsValues(simulation.getSimulationsValues())
                .build();
    }


    public Page<Simulation> paginationSimulations(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Simulation> list;
        if (simulationRepository.count() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = (int) Math.min(startItem + pageSize, simulationRepository.count());
            list = simulationRepository.findAll().subList(startItem, toIndex);
        }
        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), simulationRepository.count());
    }

    public Page<SimulationsValues> paginationSimulationValues(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<SimulationsValues> list;
        if (simulationsValuesRepository.count() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = (int) Math.min(startItem + pageSize, simulationsValuesRepository.count());
            list = simulationsValuesRepository.findAll().subList(startItem, toIndex);
        }
        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), simulationsValuesRepository.count());
    }


    public Page<SimulationsValues> pagination(Long id, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<SimulationsValues> list;
        if (eachRemainingDayService.getSimulations().stream().filter(e -> e.getId().equals(id))
                .collect(Collectors.toList()).get(0).getSimulationsValues().size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, (eachRemainingDayService.getSimulations().stream()
                    .filter(e -> e.getId().equals(id)).collect(Collectors.toList()).get(0)
                    .getSimulationsValues().size()));
            list = eachRemainingDayService.getSimulations().stream()
                    .filter(e -> e.getId().equals(id)).collect(Collectors.toList()).get(0)
                    .getSimulationsValues().subList(startItem, toIndex);
        }
        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), eachRemainingDayService.getSimulations()
                .stream().filter(e -> e.getId().equals(id)).collect(Collectors.toList()).get(0)
                .getSimulationsValues().size());
    }

}
