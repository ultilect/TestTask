package org.example.util;

import org.example.dto.FileInfo;

import java.io.*;
import java.util.StringTokenizer;

public class FileUtil {
    private static final String JAVA_COMMENT_MARK = "//";
    private static final String BASH_COMMENT_MARK = "#";
    public static FileInfo getFileInfo(String filePath, String fileExtension){
        File file = new File(filePath);
        if(!file.exists() || !file.isFile()) {
            System.out.printf("Cannot get file by path: %s", filePath);
            return new FileInfo();
        }
        try (BufferedReader fileReader = new BufferedReader(new FileReader(filePath))) {
            Long fileSize = file.length();
            Long totalLines = 0L;
            Long totalNotEmptyLines = 0L;
            Long linesWithComments = 0L;
            String line;
            StringTokenizer tokenizer;
            while ((line = fileReader.readLine()) != null) {
                tokenizer = new StringTokenizer(line);
                totalLines++;
                if (tokenizer.hasMoreTokens()) {
                    totalNotEmptyLines++;
                    final String token = tokenizer.nextToken();
                    if ((fileExtension.equalsIgnoreCase("java") && token.startsWith(JAVA_COMMENT_MARK))
                            || (fileExtension.equalsIgnoreCase("sh") && token.startsWith(BASH_COMMENT_MARK))) linesWithComments++;
                }
            }
            return new FileInfo(fileSize, totalLines, totalNotEmptyLines, linesWithComments);
        } catch (IOException ex) {
            System.out.printf("[ERROR] Cannot open file by path %s\n", filePath);
            return new FileInfo();
        }
    }
}
