package me.synology.techrevive.teacher.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class HelloEndpointIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldReturnHelloWorldWhenCallGetHello() {
        ResponseEntity<String> response = restTemplate.getForEntity("/hello", String.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Hello World", response.getBody());
    }

    @Test
    public void shouldReturnHelloWorldWithCorrectContentType() {
        ResponseEntity<String> response = restTemplate.getForEntity("/hello", String.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("text/plain;charset=UTF-8", response.getHeaders().getContentType().toString());
        assertEquals("Hello World", response.getBody());
    }

    @Test
    public void shouldAllowGetRequestWithoutAuthentication() {
        ResponseEntity<String> response = restTemplate.getForEntity("/hello", String.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}