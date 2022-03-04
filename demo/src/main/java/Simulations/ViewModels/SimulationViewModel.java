package Simulations.ViewModels;

import Simulations.Annotations.GreaterThan;
import Simulations.Entity.SimulationsValues;
import lombok.*;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@GreaterThan(message = "field number of days to death can not be equal or bigger than field days to recovery")
public class SimulationViewModel {

    private Long id;

    @NotEmpty(message = "field can not be empty")
    private String simulation_Name;

    @NotNull(message = "field can not be empty")
    @DecimalMax("10000000000.0")
    @DecimalMin("0.0")
    private Double population_Size;

    private Double initial_Infected_Number;

    @NotNull(message = "field can not be empty")
    private Double how_Many_One_Infects;

    @NotNull(message = "field can not be empty")
    @DecimalMax("1.0")
    @DecimalMin("0.001")
    private Double mortality_Rate;

    private @NotNull(message = "field can not be empty") @DecimalMin("1.0")
    Double number_Of_Days_To_Recovery;

    private @NotNull(message = "field can not be empty") @DecimalMin("1.0")
    Double number_Of_Days_To_Death;

    private @NotNull(message = "field can not be empty")
    Double simulation_Time;

    List<SimulationsValues> simulationsValues = new ArrayList<>();
}