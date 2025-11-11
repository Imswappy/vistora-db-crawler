package com.example.schemacrawler;

import com.example.schemacrawler.service.ModelGeneratorService;
import com.example.schemacrawler.service.SchemaCrawlerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test class for MySQL Schema Crawler application.
 */
@SpringBootTest
class MysqlSchemaCrawlerApplicationTests {

    @Autowired
    private SchemaCrawlerService schemaCrawlerService;

    @Autowired
    private ModelGeneratorService modelGeneratorService;

    /**
     * Test if application context loads successfully.
     */
    @Test
    void contextLoads() {
        assertNotNull(schemaCrawlerService);
        assertNotNull(modelGeneratorService);
    }

    /**
     * Test schema crawler service.
     */
    @Test
    void testSchemaCrawlerService() {
        assertNotNull(schemaCrawlerService);
    }

    /**
     * Test model generator service.
     */
    @Test
    void testModelGeneratorService() {
        assertNotNull(modelGeneratorService);
    }

}