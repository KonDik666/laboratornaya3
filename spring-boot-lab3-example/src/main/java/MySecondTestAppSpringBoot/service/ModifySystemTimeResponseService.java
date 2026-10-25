package MySecondTestAppSpringBoot.service;

import MySecondTestAppSpringBoot.model.Response;
import MySecondTestAppSpringBoot.util.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
@Qualifier("ModifySystemTimeResponseService")
public class ModifySystemTimeResponseService implements ModifyResponseService {

    @Override
    public Response modify(Response response) {
        String previousSystemTime = response.getSystemTime();
        response.setSystemTime(DateTimeUtil.getCustomFormat().format(new Date()));
        log.info("systemTime changed: uid={}, previous={}, current={}",
                response.getUid(), previousSystemTime, response.getSystemTime());
        return response;
    }
}
