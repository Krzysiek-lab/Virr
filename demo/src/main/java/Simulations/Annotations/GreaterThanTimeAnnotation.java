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
@Constraint(validatedBy = RangeTimeValidator.class)
public @interface GreaterThanTimeAnnotation {


    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}


class RangeTimeValidator implements ConstraintValidator<GreaterThanTimeAnnotation, SimulationViewModel> {

    @Override
    public void initialize(GreaterThanTimeAnnotation date) {
    }

    @Override
    public boolean isValid(SimulationViewModel dto, ConstraintValidatorContext constraintValidatorContext) {
        if (dto.getNumber_Of_Days_To_Death() == null || dto.getSimulation_Time() == null) {
            return false;
        }
        return dto.getNumber_Of_Days_To_Death().doubleValue() < dto.getSimulation_Time().doubleValue()
                && dto.getSimulation_Time().doubleValue() > dto.getNumber_Of_Days_To_Recovery().doubleValue();
    }
}
