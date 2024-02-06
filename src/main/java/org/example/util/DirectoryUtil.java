package org.example.util;

import org.example.dto.DirectoryInfoByExtension;
import org.example.dto.FileInfo;

import java.io.File;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;

public class DirectoryUtil {
    public static Optional<Hashtable<String, DirectoryInfoByExtension>> getDirectoryInfoByExtension(String path, Boolean isRecursive,
                                                                                          Integer maxDepths, Integer nowDeps, HashSet<String> includeExtension,
                                                                                          HashSet<String> excludeExtension, ThreadPoolExecutor executor) {
        File folder = new File(path);
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.printf("[ERROR] Cannot get directory by path: %s\n", path);
            return Optional.empty();
        }

        Stack<File> directoriesForStatistic = new Stack<>();
        directoriesForStatistic.add(folder);
        Hashtable<String, DirectoryInfoByExtension> info = new Hashtable<>();
        if (executor != null) {
            synchronized (info) {
                synchronized (directoriesForStatistic) {
                    while (!directoriesForStatistic.isEmpty()) {
                        executor.submit(() -> {
                            handleDirectory(isRecursive, directoriesForStatistic, info, includeExtension, excludeExtension, maxDepths, nowDeps);
                        });
                        if (executor.getQueue().size() >= executor.getMaximumPoolSize() * 10)
                            executor.getQueue().forEach(Runnable::run);
                    }
                }
            }
        } else {
            while (!directoriesForStatistic.isEmpty()) {
                handleDirectory(isRecursive, directoriesForStatistic, info, includeExtension, excludeExtension, maxDepths, nowDeps);
            }
        }
        return Optional.of(info);
    }

    private static void handleDirectory( Boolean isRecursive, Stack<File> directoriesForStatistic,  Hashtable<String, DirectoryInfoByExtension> info,  HashSet<String> includeExtension,
                                 HashSet<String> excludeExtension,  Integer maxDepths, Integer nowDeps) {
        File currentFolder;
        currentFolder = directoriesForStatistic.pop();
        for (final File file : currentFolder.listFiles()) {
            System.out.printf("FILE %s\n", file.getPath());
            if (file.isFile()) {
                Optional<String> fileExtension = getFileExtension(file.getPath());
                if (fileExtension.isEmpty()
                        || (includeExtension != null && !includeExtension.contains(fileExtension.get()))
                        || (excludeExtension != null && excludeExtension.contains(fileExtension.get())))
                    continue;
                DirectoryInfoByExtension currentExtensionInDirectory = info.getOrDefault(fileExtension.get(), new DirectoryInfoByExtension());
                FileInfo fileInfo = FileUtil.getFileInfo(file.toString(), fileExtension.get());
                currentExtensionInDirectory.setFileAmount(currentExtensionInDirectory.getFileAmount() + 1);
                currentExtensionInDirectory.setDataFromFileInfo(fileInfo);
                info.put(fileExtension.get(), currentExtensionInDirectory);
            } else if (file.isDirectory() && isRecursive && (maxDepths == null || (nowDeps <= maxDepths))) {
                System.out.printf("FOLER %s\n", file.getPath());
                directoriesForStatistic.push(file);
            }
        }
    }
    private static Optional<String> getFileExtension(String filePath) {
        if (filePath == null) return Optional.empty();
        return Optional.of(filePath).filter(f -> f.contains("."))
                .map(f -> f.substring(filePath.lastIndexOf(".") + 1));
    }
}
