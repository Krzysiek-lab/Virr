package Simulations.Entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

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
    private BigDecimal day;

    @Column(name = "Pi")
    private BigDecimal number_Of_Infected;

    @Column(name = "Pv")
    private BigDecimal healthy_Prone_To_Infection;

    @Column(name = "Pm")
    private BigDecimal dead;

    @Column(name = "Pr")
    private BigDecimal regained_Health_And_Immunity;

    @ManyToOne
    @JoinColumn(name = "simulation_id", referencedColumnName = "id")
    private Simulation simulation;


}