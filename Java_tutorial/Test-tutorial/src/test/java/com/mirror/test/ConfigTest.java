package com.mirror.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfigTest {
    Config config;

    @BeforeEach
    public void setUp() {
        config = new Config();
    }

    @AfterEach
    public void tearDown() {
        config = null;
    }

    @Test
    @DisabledOnJre(JRE.JAVA_8)
    @EnabledIfSystemProperty(named = "os.arch", matches = ".*64.*")
    @EnabledOnOs(OS.WINDOWS)
    @EnabledIfEnvironmentVariable(named = "DEBUG", matches = "true")
    void testWindows() {
        assertEquals("C:\\test.ini", config.getConfigFile("test.ini"));
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    @EnabledOnOs({ OS.LINUX, OS.MAC })
    void testLinuxAndMac() {
        assertEquals("/usr/local/test.cfg", config.getConfigFile("test.cfg"));
    }
}