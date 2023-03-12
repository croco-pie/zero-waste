package com.zerowaste.zwb;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles(profiles = "unit")
@AutoConfigureEmbeddedDatabase
class ZeroWasteBotApplicationTest {

    @Test
    void contextLoads() {
    }
}
