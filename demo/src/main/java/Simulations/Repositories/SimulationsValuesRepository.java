package Simulations.Repositories;

import Simulations.Entity.SimulationsValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimulationsValuesRepository extends JpaRepository<SimulationsValues, Long> {

}