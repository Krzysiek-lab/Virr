package Simulations.Repositories;

import Simulations.Entity.SimulationsValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimulationsValuesRepository extends JpaRepository<SimulationsValues, Long> {

//    @Query("select s from SimulationsValues s where s.day = :day")
//    SimulationsValues findByDay(@Param("day") Long day);
}