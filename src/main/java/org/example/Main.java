package org.example;

import com.beust.jcommander.JCommander;
import org.example.util.DirectoryUtil;
import org.example.util.OutputUtil;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {
    public static void main(String[] args)  {
        CommandArgs parsedArgs = new CommandArgs();
        JCommander.newBuilder().addObject(parsedArgs).build().parse(args);
        String workingDir = System.getProperty("user.dir");
        String path = parsedArgs.getPath();
        if (path == null) {
            path = workingDir;
        } else if (!path.startsWith("/")) {
            path = workingDir + "/" + path;
        }
        HashSet<String> includeExtension = parsedArgs.getIncludeExtension() != null ? new HashSet<>(Arrays.asList(parsedArgs.getIncludeExtension().split(","))) : null;
        HashSet<String> excludeExtension = parsedArgs.getExcludeExtension() != null ? new HashSet<>(Arrays.asList(parsedArgs.getExcludeExtension().split(","))) : null;
        ThreadPoolExecutor executor = parsedArgs.getThreadAmount() != null ? (ThreadPoolExecutor) Executors.newFixedThreadPool(parsedArgs.getThreadAmount()) : null;
        DirectoryUtil.getDirectoryInfoByExtension(path, parsedArgs.getRecursive(), parsedArgs.getMaxDepth(), 0, includeExtension, excludeExtension, executor)
                .ifPresentOrElse((info) -> {
                    if (executor != null) {
                        executor.shutdown();
                        while (!executor.isTerminated()) {
                        }
                    }
                    System.out.println(OutputUtil.createOutputOfStatistic(info, OutputStatisticType.PLAIN));
                }, () -> System.out.println("Не удалось собрать статистику"));
    }
}

