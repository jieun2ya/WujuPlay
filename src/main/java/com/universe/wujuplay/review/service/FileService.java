package com.universe.wujuplay.review.service;

import com.universe.wujuplay.review.model.FileDTO;
import com.universe.wujuplay.review.model.FileEntity;
import com.universe.wujuplay.review.model.ReviewEntity;
import com.universe.wujuplay.review.repository.FileRepository;
import com.universe.wujuplay.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {

    private final ReviewRepository reviewRepository;
    private final FileRepository fileRepository;

    @Transactional
    public void saveFile(ReviewEntity reviewEntity, FileDTO fileDTO) {
        fileDTO.setReviewEntity(reviewEntity);
        fileRepository.save(fileDTO.toEntity());
    }

    // 추가 파일 저장
    @Transactional
    public void saveFiles(ReviewEntity reviewEntity, List<FileDTO> fileDTOList) {

        for (FileDTO fileDTO : fileDTOList) {
            fileDTO.setReviewEntity(reviewEntity);
            fileRepository.save(fileDTO.toEntity());
        }
    }

    // ReviewId로 파일 찾기

    // 썸네일 찾기
    public List<FileEntity> findThumbnailByReviewId(Long reviewId) {

        List<FileEntity> thumbnailEntity = fileRepository.findByReviewEntityReviewIdAndThumbnailAndDeletedDateIsNull(reviewId, 1);

        return thumbnailEntity;

    }
    // 추가 이미지 찾기
    public List<FileEntity> findAllByReviewId(Long reviewId) {

        List<FileEntity> fileEntityList = fileRepository.findByReviewEntityReviewIdAndThumbnailAndDeletedDateIsNull(reviewId, 0);

        return fileEntityList;
    }

    // 파일 리스트 조회 (edit 용도)
//    public List<FileEntity> findAllByIdIn(List<Long> ids) {
//        if (CollectionUtils.isEmpty(ids)) {
//            return Collections.emptyList();
//        }
//        return fileRepository.findAllByIdIn(ids);
//    }

    // 파일 삭제
    @Transactional
    public void deleteByIdIn(List<Long> ids) {
        fileRepository.deleteByIdIn(ids);
    }
}
