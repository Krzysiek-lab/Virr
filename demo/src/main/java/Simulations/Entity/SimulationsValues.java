package Simulations.Entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Table
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SimulationsValues {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;

    @Column(name = "Day")
    private Long day;

    @Column(name = "Pi")
    private Long number_Of_Infected;

    @Column(name = "Pv")
    private Double healthy_Prone_To_Infection;

    @Column(name = "Pm")
    private Long dead;

    @Column(name = "Pr")
    private Long regained_Health_And_Immunity;

    @JoinColumn(name = "simulation_id")
    @ManyToOne
    Simulation simulation;


}