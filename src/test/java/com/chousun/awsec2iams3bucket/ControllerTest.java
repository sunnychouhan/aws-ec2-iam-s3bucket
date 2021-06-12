package com.chousun.awsec2iams3bucket;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

class ControllerTest {

    @Test
    public void test() {
        ClassPathResource classPathResource = new ClassPathResource("/test.txt", this.getClass().getClassLoader());
        try {
            InputStream inputStream = classPathResource.getInputStream();
            StringBuilder stringBuilder = Controller.displayTextInputStream(inputStream);
            System.out.println("stringBuilder = " + stringBuilder);
            Assertions.assertNotNull(stringBuilder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}