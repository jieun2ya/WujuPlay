package com.universe.wujuplay.review.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Table(name = "FILE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileEntity {

    @Id
    @Column(name = "FILE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long fileId;

    @ManyToOne //(fetch = FetchType.LAZY)
    @JoinColumn(name = "REVIEW_ID")
    private ReviewEntity reviewEntity;

    @Column(name = "ORIGIN_NAME")
    private String originName;

    @Column(name = "FILE_NAME")
    private String fileName;

    @Column(name = "PATH")
    private String path;

    @Column(name = "SIZE")
    private long size;

    @Column(name = "THUMBNAIL", columnDefinition = "int default 0")
    private int thumbnail;

    @CreationTimestamp
    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;

    @Column(name = "DELETED_DATE")
    private Timestamp deletedDate;

    @Builder
    public FileEntity(long fileId, ReviewEntity reviewEntity, String originName, String fileName, String path, long size, int thumbnail, Timestamp createdDate, Timestamp deletedDate) {
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
}
