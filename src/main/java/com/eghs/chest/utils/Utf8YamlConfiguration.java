package com.eghs.chest.utils;

import com.google.common.io.Files;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class Utf8YamlConfiguration extends YamlConfiguration {
    public void save(File file) throws IOException {
        Validate.notNull(file, "File cannot be null");
        Files.createParentDirs(file);
        String data = saveToString();
        FileOutputStream stream = new FileOutputStream(file);
        try (OutputStreamWriter writer = new OutputStreamWriter(stream, StandardCharsets.UTF_8)) {
            writer.write(data);
        }
    }
}
