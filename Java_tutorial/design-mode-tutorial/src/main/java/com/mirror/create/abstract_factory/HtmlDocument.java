package com.mirror.create.abstract_factory;

import java.io.IOException;
import java.nio.file.Path;

/**
 * @author mirror
 */
public interface HtmlDocument {
    String toHtml();
    void save(Path path) throws IOException;
}