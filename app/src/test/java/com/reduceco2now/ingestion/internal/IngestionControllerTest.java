package com.reduceco2now.ingestion.internal;

import com.reduceco2now.ingestion.internal.IngestionService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class IngestionControllerTest {

    @Test
    void shouldTriggerIngestionRun() throws Exception {
        IngestionService ingestionService = mock(IngestionService.class);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new IngestionController(ingestionService)).build();

        mockMvc.perform(post("/api/v1/ingestion/run").accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string("Ingestion run successfully completed. Check server logs for details."));

        verify(ingestionService).runIngestion();
    }
}