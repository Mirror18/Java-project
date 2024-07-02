package com.mirror.Bean;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    public long id;
    public String name;
    public School school;
}
