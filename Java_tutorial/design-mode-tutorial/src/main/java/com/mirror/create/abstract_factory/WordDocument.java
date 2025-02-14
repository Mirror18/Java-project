package com.mirror.create.abstract_factory;

import java.io.IOException;
import java.nio.file.Path;

// Word文档接口:
public interface WordDocument {
    void save(Path path) throws IOException;
}