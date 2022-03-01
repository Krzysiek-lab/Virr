package Simulations.Entity;

import lombok.*;

import javax.persistence.*;
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
    private Double population_Size;

    @Column(name = "I")
    private Long initial_Infected_Number;

    @Column(name = "R")
    private Double how_Many_One_Infects;

    @Column(name = "M")
    private Double mortality_Rate;

    @Column(name = "Ti")
    private Long number_Of_Days_To_Recovery;

    @Column(name = "Tm")
    private Long number_Of_Days_To_Death;

    @Column(name = "Ts")
    private Long simulation_Time;

    @OneToMany(mappedBy = "simulation", cascade = CascadeType.REMOVE)
    List<SimulationsValues> simulationsValues;
}