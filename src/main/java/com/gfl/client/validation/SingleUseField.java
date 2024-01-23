package com.gfl.client.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { SingleUseFieldValidator.class })
public @interface SingleUseField {
    String message() default "Specify only one of useTimes or useAlways";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}