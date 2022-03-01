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
@Constraint(validatedBy = RangeValidator.class)
public @interface GreaterThan {


    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}


class RangeValidator implements ConstraintValidator<GreaterThan, SimulationViewModel> {

    @Override
    public void initialize(GreaterThan date) {
    }

    @Override
    public boolean isValid(SimulationViewModel dto, ConstraintValidatorContext constraintValidatorContext) {
        if (dto.getNumber_Of_Days_To_Death() == null || dto.getNumber_Of_Days_To_Recovery() == null) {
            return false;// z true zmiana
        }
        return dto.getNumber_Of_Days_To_Death() < dto.getNumber_Of_Days_To_Recovery();
    }
}
