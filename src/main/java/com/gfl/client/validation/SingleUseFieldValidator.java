package com.gfl.client.validation;

import com.gfl.client.model.ProxyConfigHolder;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SingleUseFieldValidator implements ConstraintValidator<SingleUseField, ProxyConfigHolder> {

    @Override
    public boolean isValid(ProxyConfigHolder proxy, ConstraintValidatorContext context) {
        return !(proxy.getUseTimes() != null && proxy.isUseAlways());
    }
}
