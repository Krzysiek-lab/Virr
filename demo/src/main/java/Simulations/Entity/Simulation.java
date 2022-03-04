package Simulations.Entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Builder
@Table
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Simulation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;

    @Column(name = "N")
    private String simulation_Name;

    @Column(name = "P")
    private BigDecimal population_Size;

    @Column(name = "I")
    private BigDecimal initial_Infected_Number;

    @Column(name = "R")
    private BigDecimal how_Many_One_Infects;

    @Column(name = "M")
    private BigDecimal mortality_Rate;

    @Column(name = "Ti")
    private BigDecimal number_Of_Days_To_Recovery;

    @Column(name = "Tm")
    private BigDecimal number_Of_Days_To_Death;

    @Column(name = "Ts")
    private BigDecimal simulation_Time;

    @OneToMany(mappedBy = "simulation", cascade = CascadeType.REMOVE)
    private List<SimulationsValues> simulationsValues;
}