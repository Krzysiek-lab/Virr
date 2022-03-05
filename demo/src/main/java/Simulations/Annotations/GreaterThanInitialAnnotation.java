package Simulations.Annotations;

import Simulations.ViewModels.SimulationViewModel;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = RangeInitialValidator.class)
public @interface GreaterThanInitialAnnotation {


    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}


class RangeInitialValidator implements ConstraintValidator<GreaterThanInitialAnnotation, SimulationViewModel> {

    @Override
    public void initialize(GreaterThanInitialAnnotation date) {
    }

    @Override
    public boolean isValid(SimulationViewModel dto, ConstraintValidatorContext constraintValidatorContext) {
        if (dto.getNumber_Of_Days_To_Death() == null || dto.getSimulation_Time() == null) {
            return false;
        }
        return dto.getHow_Many_One_Infects().doubleValue() < dto.getPopulation_Size().doubleValue()
                && dto.getInitial_Infected_Number().doubleValue() < dto.getPopulation_Size().doubleValue();
    }
}
