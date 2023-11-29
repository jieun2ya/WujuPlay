package com.universe.wujuplay.review.repository;

import com.universe.wujuplay.review.model.FileDTO;
import com.universe.wujuplay.review.model.FileEntity;
import com.universe.wujuplay.review.model.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {

    /**
     * ReviewId로 파일 리스트 조회
     */
    List<FileEntity> findByReviewEntityReviewIdAndThumbnailAndDeletedDateIsNull(Long reviewId, int thumbnail);
    //List<FileEntity> findByReviewEntityReviewIdAndThumbnail(Long reviewId, int thumbnail);

    /**
     * 파일 수정, 삭제 -> deletedDate 업데이트
     *
     * update file
     * set deleted_date=now()
     * where file_id = 71;
     * */
    @Modifying
    @Query("UPDATE FileEntity f SET f.deletedDate = CURRENT_TIMESTAMP WHERE f.fileId IN :ids")
    void deleteByIdIn(List<Long> ids);

    // 리뷰 목록 대표이미지
    FileEntity findByReviewEntityReviewIdAndThumbnailAndDeletedDateIsNull(long reviewId, int thumbnail);

}