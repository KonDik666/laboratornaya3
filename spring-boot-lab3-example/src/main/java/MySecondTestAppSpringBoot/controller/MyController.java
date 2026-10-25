package MySecondTestAppSpringBoot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import MySecondTestAppSpringBoot.exception.ValidationFailedException;
import MySecondTestAppSpringBoot.exception.UnsupportedCodeException;
import MySecondTestAppSpringBoot.model.Codes;
import MySecondTestAppSpringBoot.model.ErrorCodes;
import MySecondTestAppSpringBoot.model.ErrorMessages;
import MySecondTestAppSpringBoot.model.Request;
import MySecondTestAppSpringBoot.model.Response;
import MySecondTestAppSpringBoot.service.ModifyResponseService;
import MySecondTestAppSpringBoot.service.ValidationService;
import MySecondTestAppSpringBoot.util.DateTimeUtil;

import javax.validation.Valid;
import java.util.Date;

@Slf4j
@RestController
public class MyController {

    private final ValidationService validationService;
    private final ModifyResponseService modifyResponseService;

    @Autowired
    public MyController(ValidationService validationService,
                        @Qualifier("ModifySystemTimeResponseService") ModifyResponseService modifyResponseService) {
        this.validationService = validationService;
        this.modifyResponseService = modifyResponseService;
    }

    @PostMapping(value = "/feedback")
    public ResponseEntity<Response> feedback(@Valid @RequestBody Request request,
                                             BindingResult bindingResult) {

        log.info("request: {}", request);

        Response response = Response.builder()
                .uid(request.getUid())
                .operationUid(request.getOperationUid())
                .systemTime(DateTimeUtil.getCustomFormat().format(new Date()))
                .code(Codes.SUCCESS)
                .errorCode(ErrorCodes.EMPTY)
                .errorMessage(ErrorMessages.EMPTY)
                .build();
        log.info("response created: {}", response);

        try {
            validationService.isValid(bindingResult);
            if ("123".equals(request.getUid())) {
                log.error("UnsupportedCodeException: uid={}, message={}",
                        request.getUid(), ErrorMessages.UNSUPPORTED.getName());
                throw new UnsupportedCodeException("Не поддерживаемая ошибка");
            }
        } catch (ValidationFailedException e) {
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.VALIDATION_EXCEPTION);
            response.setErrorMessage(ErrorMessages.VALIDATION);
            log.info("response sent: status={}, response={}", HttpStatus.BAD_REQUEST, response);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (UnsupportedCodeException e) {
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNSUPPORTED_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UNSUPPORTED);
            log.info("response sent: status={}, response={}", HttpStatus.BAD_REQUEST, response);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Unexpected exception: uid={}", request.getUid(), e);
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNKNOWN_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UNKNOWN);
            log.info("response sent: status={}, response={}", HttpStatus.INTERNAL_SERVER_ERROR, response);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response = modifyResponseService.modify(response);
        log.info("response sent: status={}, response={}", HttpStatus.OK, response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
