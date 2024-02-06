package org.example.util;

import org.example.dto.DirectoryInfoByExtension;
import org.example.dto.FileInfo;

import java.io.File;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;

public class DirectoryUtil {
    public static Optional<Hashtable<String, DirectoryInfoByExtension>> getDirectoryInfoByExtension(String path, Boolean isRecursive,
                                                                                          Integer maxDepths, HashSet<String> includeExtension,
                                                                                          HashSet<String> excludeExtension, ThreadPoolExecutor executor) {
        File folder = new File(path);
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.printf("[ERROR] Cannot get directory by path: %s\n", path);
            return Optional.empty();
        }

        Stack<StackInfo> directoriesForStatistic = new Stack<>();
        directoriesForStatistic.add(new StackInfo(folder,0));
        Hashtable<String, DirectoryInfoByExtension> info = new Hashtable<>();
        if (executor != null) {
            synchronized (info) {
                synchronized (directoriesForStatistic) {
                    while (!directoriesForStatistic.isEmpty()) {
                        executor.submit(() -> {
                            handleDirectory(isRecursive, directoriesForStatistic, info, includeExtension, excludeExtension, maxDepths);
                        });
                        if (executor.getQueue().size() >= executor.getMaximumPoolSize() * 10)
                            executor.getQueue().forEach(Runnable::run);
                    }
                }
            }
        } else {
            while (!directoriesForStatistic.isEmpty()) {
                handleDirectory(isRecursive, directoriesForStatistic, info, includeExtension, excludeExtension, maxDepths);
            }
        }
        return Optional.of(info);
    }

    private static void handleDirectory(Boolean isRecursive, Stack<StackInfo> directoriesForStatistic, Hashtable<String, DirectoryInfoByExtension> info, HashSet<String> includeExtension,
                                        HashSet<String> excludeExtension, Integer maxDepths) {
        StackInfo infoFromStack = directoriesForStatistic.pop();
        File currentFolder = infoFromStack.getFile();
        for (final File file : currentFolder.listFiles()) {
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
            } else if (file.isDirectory() && isRecursive && (maxDepths == null || (infoFromStack.getRecursiveDepth() <= maxDepths))) {
                directoriesForStatistic.push(new StackInfo(file, infoFromStack.getRecursiveDepth()+1));
            }
        }
    }
    private static Optional<String> getFileExtension(String filePath) {
        if (filePath == null) return Optional.empty();
        return Optional.of(filePath).filter(f -> f.contains("."))
                .map(f -> f.substring(filePath.lastIndexOf(".") + 1));
    }
}

class StackInfo {
    private File file;
    private Integer recursiveDepth;

    public StackInfo(File file, Integer recursiveDepth) {
        this.file = file;
        this.recursiveDepth = recursiveDepth;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Integer getRecursiveDepth() {
        return recursiveDepth;
    }

    public void setRecursiveDepth(Integer recursiveDepth) {
        this.recursiveDepth = recursiveDepth;
    }
}