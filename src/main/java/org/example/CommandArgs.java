package org.example;

import com.beust.jcommander.Parameter;

public class CommandArgs {
    @Parameter
    private String path;
    @Parameter(names = "--recursive", description = "выполнять обход дерева рекурсивно")
    private Boolean recursive = false;

    @Parameter(names = "--max-depth", description = "глубина рекурсивного обхода")
    private Integer maxDepth;

    @Parameter(names = "--thread", description = " количество потоков используемого для обхода")
    private Integer threadAmount;

    @Parameter(names = "--include-ext", description = "обрабатывать файлы только с указанными расширениями")
    private String includeExtension;

    @Parameter(names = "--exclude-ext", description = "не обрабатывать файлы только указанными расширениями")
    private String excludeExtension;

    public String getPath() {
        return path;
    }

    public Boolean getRecursive() {
        return recursive;
    }

    public Integer getMaxDepth() {
        return maxDepth;
    }

    public Integer getThreadAmount() {
        return threadAmount;
    }

    public String getIncludeExtension() {
        return includeExtension;
    }

    public String getExcludeExtension() {
        return excludeExtension;
    }
}
