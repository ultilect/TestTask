package org.example.dto;

public record FileInfo(Long size,
                       Long totalLines,
                       Long totalNotEmptyLines,
                       Long linesWithComments) {
    public FileInfo() {
        this(0L,0L,0L,0L);
    }
}
