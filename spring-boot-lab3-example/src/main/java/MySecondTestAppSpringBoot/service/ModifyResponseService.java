package MySecondTestAppSpringBoot.service;

import MySecondTestAppSpringBoot.model.Response;
import org.springframework.stereotype.Service;

@Service
public interface ModifyResponseService {

    Response modify(Response response);
}
