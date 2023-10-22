package com.example.AbnAmroTask.controller;

import com.example.AbnAmroTask.service.ProcessReportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class FileUploadControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProcessReportService processReportService;

    @Test
    void testUploadFile() throws Exception {
        File outputFile = File.createTempFile("Output", "txt");
        outputFile.deleteOnExit();
        FileWriter fileWriter = new FileWriter(outputFile, true);
        BufferedWriter bw = new BufferedWriter(fileWriter);
        bw.write("and this is the output");
        bw.close();
        when(processReportService.generateReport()).thenReturn(outputFile);

        String input = "this is the input";
        MockMultipartFile file = new MockMultipartFile("file", "file.txt", MediaType.MULTIPART_FORM_DATA_VALUE, input.getBytes());
        mvc.perform(MockMvcRequestBuilders.multipart("/api/upload")
                .file(file))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/csv"))
                .andExpect(MockMvcResultMatchers.content().bytes("and this is the output".getBytes()));
    }
}