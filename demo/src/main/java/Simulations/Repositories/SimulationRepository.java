package Simulations.Repositories;

import Simulations.Entity.Simulation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SimulationRepository extends JpaRepository<Simulation, Long> {

    @Query("select s from Simulation s where s.simulation_Name = :simulation_Name")
    Simulation findByName(@Param("simulation_Name") String simulation_Name);
}