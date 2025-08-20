package me.synology.techrevive.teacher.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.synology.techrevive.teacher.resources.dto.PostRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SomethingEndpointIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldCalculateValueTimesByThreeAndHalf() throws Exception {
        PostRequest request = new PostRequest("testUser", "test content", 10);

        mockMvc.perform(post("/something")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("35.0"));
    }

    @Test
    public void shouldCalculateValueWithZero() throws Exception {
        PostRequest request = new PostRequest("testUser", "test content", 0);

        mockMvc.perform(post("/something")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("0.0"));
    }

    @Test
    public void shouldCalculateValueWithLargeNumber() throws Exception {
        PostRequest request = new PostRequest("testUser", "test content", 100);

        mockMvc.perform(post("/something")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("350.0"));
    }

    @Test
    public void shouldRejectNegativeValue() throws Exception {
        PostRequest request = new PostRequest("testUser", "test content", -1);

        mockMvc.perform(post("/something")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldAllowAccessWithoutAuthentication() throws Exception {
        PostRequest request = new PostRequest("testUser", "test content", 10);

        mockMvc.perform(post("/something")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("35.0"));
    }

    @Test
    public void shouldReturnBadRequestForInvalidJson() throws Exception {
        String invalidJson = "{invalid json}";

        mockMvc.perform(post("/something")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}