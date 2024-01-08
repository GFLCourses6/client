package com.gfl.client.util.file;

import java.io.IOException;
import java.util.List;

public interface FileParser {

    <T> List<T> getAllFromFile(String filePath, Class<T> type) throws IOException;
    <T> T getFromFile(String filePath, Class<T> type) throws IOException;
}
