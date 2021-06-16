package com.infotamia.weather.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author Mohammed Al-Ani
 */
@Component
public class ConfigRunner implements CommandLineRunner {
    Logger logger = LoggerFactory.getLogger(ConfigRunner.class);
    @Override
    public void run(String... args) throws Exception {
        logger.info("DO SOME STUFF");
    }
}
