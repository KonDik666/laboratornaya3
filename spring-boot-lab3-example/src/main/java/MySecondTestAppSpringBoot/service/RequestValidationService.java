package MySecondTestAppSpringBoot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;
import org.springframework.validation.BindingResult;
import MySecondTestAppSpringBoot.exception.ValidationFailedException;

@Slf4j
@Service
public class RequestValidationService implements ValidationService {

    @Override
    public void isValid(BindingResult bindingResult) throws ValidationFailedException {
        log.info("Validation started: request={}", bindingResult.getTarget());
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                log.error("ValidationFailedException: request={}, error={}",
                        bindingResult.getTarget(), error);
            }
            throw new ValidationFailedException(bindingResult.getFieldError().toString());
        }
        log.info("Validation passed: request={}", bindingResult.getTarget());
    }
}
