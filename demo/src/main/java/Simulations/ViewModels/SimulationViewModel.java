package Simulations.ViewModels;

import Simulations.Annotations.GreaterThan;
import Simulations.Annotations.GreaterThanInitialAnnotation;
import Simulations.Annotations.GreaterThanTimeAnnotation;
import Simulations.Entity.SimulationsValues;
import lombok.*;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@GreaterThan(message = "field number of days to death can not be equal or bigger than field days to recovery")
@GreaterThanTimeAnnotation(message = "field simulation Time can not be equal or smaller than field days to death " +
        "and field days to recovery")
@GreaterThanInitialAnnotation(message = "fields initial infected number and how many one infects can not be equal or " +
        "bigger than field population size")

public class SimulationViewModel {

    private Long id;

    @NotEmpty(message = "field can not be empty")
    private String simulation_Name;

    @NotNull(message = "field can not be empty")
    @DecimalMax("10000000000.0")
    @DecimalMin("0.0")
    private BigDecimal population_Size;

    private BigDecimal initial_Infected_Number;

    @NotNull(message = "field can not be empty")
    private BigDecimal how_Many_One_Infects;

    @NotNull(message = "field can not be empty")
    @DecimalMax("1.0")
    @DecimalMin("0.001")
    private BigDecimal mortality_Rate;

    @NotNull(message = "field can not be empty")
    @DecimalMin("1.0")
    private BigDecimal number_Of_Days_To_Recovery;

    @NotNull(message = "field can not be empty")
    @DecimalMin("1.0")
    private BigDecimal number_Of_Days_To_Death;

    @NotNull(message = "field can not be empty")
    private BigDecimal simulation_Time;

    List<SimulationsValues> simulationsValues = new ArrayList<>();
}