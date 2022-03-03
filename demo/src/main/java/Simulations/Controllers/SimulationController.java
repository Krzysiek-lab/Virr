package Simulations.Controllers;

import Simulations.Entity.Simulation;
import Simulations.Entity.SimulationsValues;
import Simulations.Repositories.SimulationRepository;
import Simulations.Repositories.SimulationsValuesRepository;
import Simulations.Services.EachRemainingDayService;
import Simulations.Services.SimulationService;
import Simulations.ViewModels.SimulationViewModel;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@AllArgsConstructor
public class SimulationController {


    public final SimulationRepository simulationRepository;
    public final SimulationsValuesRepository simulationsValuesRepository;
    public final SimulationService simulationService;
    private final EachRemainingDayService eachRemainingDayService;//


    @GetMapping("SimulationsForm")
    public String form(Model model) {
        model.addAttribute("sim", new SimulationViewModel());
        return "save";
    }

    //0
    @PostMapping("SimulationsForm")
    public String addSimulations(@ModelAttribute("sim") @Valid SimulationViewModel simulationViewModel,
                                 BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            if (simulationViewModel.getId() == null) {
                return "save";
            } else {
                return "update";
            }
        }
        if (simulationViewModel.getId() == null) {
            simulationService.addSimulation(simulationViewModel);
        } else {
            simulationService.updateSimulation(simulationViewModel);
        }
        return "redirect:/allSimulations";
    }


    @GetMapping("updateSimulation/{id}")
    public String updateSimulation(@PathVariable(value = "id") Long id, Model model) {
        var find = simulationRepository.getById(id);
        SimulationViewModel sim = simulationService.simulationToViewModel(find);
        model.addAttribute("sim", sim);
        return "update";
    }


    @GetMapping("deleteSimulation/{id}")
    public String deleteSimulation(@PathVariable(value = "id") Long id) {
        var find = simulationRepository.getById(id);
        simulationRepository.delete(find);
        return "redirect:/allSimulations";
    }


    @GetMapping("SimulationByName/{name}")
    public String showSimulationsByName(@PathVariable("name") String name, Model model) {
        model.addAttribute("simulations", simulationRepository.findByName(name));
        return "SimulationByName";
    }


    @GetMapping("allSimulations")
    public String showSimulations(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                  @RequestParam(value = "size", defaultValue = "5") Integer size, Model model) {


        Page<Simulation> ratePage = simulationService.paginationSimulations(PageRequest.of(page - 1, size));
        model.addAttribute("allP", ratePage);
        int totalPages = ratePage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "allSimulations";
    }


    @GetMapping("SimulationsValues/{name}/{day}")
    public String showSimulationsPerDay(@PathVariable("day") Double day, @PathVariable("name") String name, Model model) {
        var simulation = simulationRepository.findByName(name);
        model.addAttribute("simulationsVal", simulation.getSimulationsValues().stream().filter(e -> e.getDay()
                .equals(day)).collect(Collectors.toList()));
        return "sims";
    }


    @GetMapping("SimulationsValues")
    public String showAllSimulationsValues(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                           @RequestParam(value = "size", defaultValue = "5") Integer size, Model model) {

        Page<SimulationsValues> ratePage = simulationService
                .paginationSimulationValues(PageRequest.of(page - 1, size));
        model.addAttribute("allP", ratePage);
        int totalPages = ratePage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
//        model.addAttribute("simulationsValues", simulationsValuesRepository.findAll());
        return "allSimsValues";
    }


    @GetMapping("SimulationsValuesById/{id}/")
    public String showSimulationsValuesForSimulation(@PathVariable("id") Long id,
                                                     @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                     @RequestParam(value = "size", defaultValue = "5") Integer size,
                                                     Model model) {
        Page<SimulationsValues> ratePage = simulationService.pagination(id, PageRequest.of(page - 1, size));
        model.addAttribute("allP", ratePage);
        int totalPages = ratePage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("s", simulationRepository.findAll());
        return "allSimsSimulationValues";
    }


}

