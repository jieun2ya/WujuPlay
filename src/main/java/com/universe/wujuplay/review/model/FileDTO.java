package com.universe.wujuplay.review.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
public class FileDTO {

    private Long fileId;
    private ReviewEntity reviewEntity;
    private String originName;
    private String fileName;
    private String path;
    private long size;
    private int thumbnail;
    private Date createdDate;
    private Date deletedDate;

    @Builder
    public FileDTO(long fileId, ReviewEntity reviewEntity, String originName, String fileName, String path, long size, int thumbnail, Date createdDate, Date deletedDate) {
        this.fileId = fileId;
        this.reviewEntity = reviewEntity;
        this.originName = originName;
        this.fileName = fileName;
        this.path = path;
        this.size = size;
        this.thumbnail = thumbnail;
        this.createdDate = createdDate;
        this.deletedDate = deletedDate;
    }

    public static FileDTO from(FileEntity fileEntity) {
        final long fileId = fileEntity.getFileId();
        final ReviewEntity reviewEntity = fileEntity.getReviewEntity();
        final String originName = fileEntity.getOriginName();
        final String fileName = fileEntity.getFileName();
        final String path = fileEntity.getPath();
        final long size = fileEntity.getSize();
        final int thumbnail = fileEntity.getThumbnail();
        final Date createdDate = fileEntity.getCreatedDate();
        final Date deletedDate = fileEntity.getDeletedDate();

        return new FileDTO(fileId, reviewEntity, originName, fileName, path, size, thumbnail, createdDate, deletedDate);
    }

    public FileEntity toEntity() {
        return FileEntity.builder()
                .reviewEntity(reviewEntity)
                .originName(originName)
                .fileName(fileName)
                .path(path)
                .size(size)
                .thumbnail(thumbnail)
                .build();
    }
}
