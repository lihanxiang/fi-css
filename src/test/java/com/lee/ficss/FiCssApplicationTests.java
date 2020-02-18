package com.lee.ficss;

import com.lee.ficss.mapper.AgendaMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FiCssApplicationTests {

    @Autowired
    private AgendaMapper mapper;

    @Test
    void contextLoads() {
    }

    @Test
    public void createAgenda(){

    }
}
