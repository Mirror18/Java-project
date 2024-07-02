package com.mirror.annotation.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * @author mirror
 */
public class AppService {
    @Value("classpath:/log.txt")
    private Resource resource;

    private String logo;

    @PostConstruct
    public void init() throws IOException{
        try(var reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))){
            this.logo = reader.lines().collect(Collectors.joining("\n"));
        }
    }
}
