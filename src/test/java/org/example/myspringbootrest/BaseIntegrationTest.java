package org.example.myspringbootrest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Sql("/sql/init_table.sql")
@Sql(scripts = {"/sql/cleanup_table.sql", "/sql/reset_sequence.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public abstract class BaseIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    protected Map<String, Object> jsonMapper(String path) throws IOException {
        Resource resource = new ClassPathResource(path);
        return objectMapper.readValue(resource.getInputStream(), new TypeReference<>() {
        });
    }
}