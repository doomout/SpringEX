package org.zerock.springex.sample;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Log4j2
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/root-context.xml")
public class SampleTests {
    @Autowired //스프링에서 사용하는 의존성 주입 어노테이션 (빈이 존재하면 여기에 주입해라)
    private SampleService sampleService;

    @Test
    public void testSerbice1() {
        log.info(sampleService);
        Assertions.assertNotNull(sampleService);
    }
}
