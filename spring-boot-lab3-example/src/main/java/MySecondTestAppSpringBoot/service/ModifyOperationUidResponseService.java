package MySecondTestAppSpringBoot.service;

import MySecondTestAppSpringBoot.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@Qualifier("ModifyOperationUidResponseService")
public class ModifyOperationUidResponseService implements ModifyResponseService {

    @Override
    public Response modify(Response response) {
        String previousOperationUid = response.getOperationUid();
        UUID uuid = UUID.randomUUID();
        response.setOperationUid(uuid.toString());
        log.info("operationUid changed: uid={}, previous={}, current={}",
                response.getUid(), previousOperationUid, response.getOperationUid());
        return response;
    }
}
