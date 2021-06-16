package com.infotamia.weather.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Mohammed Al-Ani
 */
public class FileUtils {

    public static byte[] loadFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(FileUtils.class.getClassLoader().getResourceAsStream(filename))))) {
            String data = reader.lines().collect(Collectors.joining(System.lineSeparator()));
            return data.getBytes();
        } catch (IOException e) {
            return new byte[0];
        }
    }

}
