package org.example.dto;

public class DirectoryInfoByExtension {
    private Long fileAmount;
    private Long size;
    private Long totalLines;
    private Long totalNotEmptyLines;
    private Long linesWithComments;

    public DirectoryInfoByExtension() {
        this.fileAmount = 0L;
        this.size= 0L;
        this.totalLines= 0L;
        this.totalNotEmptyLines= 0L;
        this.linesWithComments= 0L;
    }

    public Long getFileAmount() {
        return fileAmount;
    }

    public void setFileAmount(Long fileAmount) {
        this.fileAmount = fileAmount;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getTotalLines() {
        return totalLines;
    }

    public void setTotalLines(Long totalLines) {
        this.totalLines = totalLines;
    }

    public Long getTotalNotEmptyLines() {
        return totalNotEmptyLines;
    }

    public void setTotalNotEmptyLines(Long totalNotEmptyLines) {
        this.totalNotEmptyLines = totalNotEmptyLines;
    }

    public Long getLinesWithComments() {
        return linesWithComments;
    }

    public void setLinesWithComments(Long linesWithComments) {
        this.linesWithComments = linesWithComments;
    }

    public void setDataFromFileInfo(FileInfo fileInfo) {
        this.size += fileInfo.size();
        this.totalLines += fileInfo.totalLines();
        this.totalNotEmptyLines += fileInfo.totalNotEmptyLines();
        this.linesWithComments += fileInfo.linesWithComments();
    }

    public void add(DirectoryInfoByExtension info) {
        this.fileAmount += info.getFileAmount();
        this.size += info.getSize();
        this.totalLines += info.getTotalLines();
        this.totalNotEmptyLines += info.getTotalNotEmptyLines();
        this.linesWithComments += info.getLinesWithComments();
    }

    @Override
    public String toString() {
        return "DirectoryInfoByExtension{" +
                "fileAmount=" + fileAmount +
                ", size=" + size +
                ", totalLines=" + totalLines +
                ", totalNotEmptyLines=" + totalNotEmptyLines +
                ", linesWithComments=" + linesWithComments +
                '}';
    }
}
