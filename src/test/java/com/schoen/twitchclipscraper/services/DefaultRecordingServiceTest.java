package com.schoen.twitchclipscraper.services;

import com.schoen.twitchclipscraper.services.interfaces.RecordingService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
class DefaultRecordingServiceTest {

    @Autowired
    RecordingService systemUnderTest;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testRecord(){

    }
}
