package org.example.util;

import org.example.OutputStatisticType;
import org.example.dto.DirectoryInfoByExtension;

import java.util.Hashtable;

public class OutputUtil {
    public static String createOutputOfStatistic(Hashtable<String, DirectoryInfoByExtension> info, OutputStatisticType type) {
        switch (type) {
            case PLAIN -> {
                return createPlainOutputOfStatistic(info);
            }
            default -> {
                return "";
            }
        }
    }

    private static String createPlainOutputOfStatistic(Hashtable<String, DirectoryInfoByExtension> info) {
        StringBuilder outputBuilder = new StringBuilder();
        outputBuilder.append("Статистика по расширениям файла:\n");
        for(final String key: info.keySet()) {
            outputBuilder.append(String.format("Расширение %s:\n", key));
            outputBuilder.append(String.format("-Количество файлов: %s\n", info.get(key).getFileAmount()));
            outputBuilder.append(String.format("-Общий размер в байтах: %s\n", info.get(key).getSize()));
            outputBuilder.append(String.format("-Общее количество строк: %s\n", info.get(key).getTotalLines()));
            outputBuilder.append(String.format("-Общее количество не пустых строк: %s\n", info.get(key).getTotalNotEmptyLines()));
            outputBuilder.append(String.format("-Общее количество строк с комментариями: %s\n", info.get(key).getLinesWithComments()));
        }

        return outputBuilder.toString();
    }
}
