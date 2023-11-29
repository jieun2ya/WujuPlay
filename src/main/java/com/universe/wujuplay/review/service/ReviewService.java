package com.universe.wujuplay.review.service;

import com.universe.wujuplay.meet.model.MeetEntity;
import com.universe.wujuplay.member.model.MemberEntity;
import com.universe.wujuplay.review.model.FileEntity;
import com.universe.wujuplay.review.model.ResponseReviewDTO;
import com.universe.wujuplay.review.model.ReviewDTO;
import com.universe.wujuplay.review.model.ReviewEntity;
import com.universe.wujuplay.review.repository.FileRepository;
import com.universe.wujuplay.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final FileRepository fileRepository;

    public List<ResponseReviewDTO> reviewList() {
        List<ReviewEntity> reviewEntityList = reviewRepository.findAllByOrderByRegdateDesc();
        List<ResponseReviewDTO> responseReviewDTOList = new ArrayList<>();
        for (ReviewEntity reviewEntity : reviewEntityList) {
            FileEntity fileEntity = fileRepository.findByReviewEntityReviewIdAndThumbnailAndDeletedDateIsNull(reviewEntity.getReviewId(), 1).get(0);
            responseReviewDTOList.add(ResponseReviewDTO.builder()
                                                    .reviewId(reviewEntity.getReviewId())
                                                    .title(reviewEntity.getTitle())
                                                    .writer(reviewEntity.getWriter())
                                                    .meetEntity(reviewEntity.getMeetEntity())
                                                    .fileEntity(fileEntity)
                                                    .build());
        }
        return responseReviewDTOList;
    }

    public List<ResponseReviewDTO> searchList(String keyword) {
        List<ReviewEntity> reviewEntityList = reviewRepository.findByKeyword(keyword);
        List<ResponseReviewDTO> responseReviewDTOList = new ArrayList<>();
        for (ReviewEntity reviewEntity : reviewEntityList) {
            FileEntity fileEntity = fileRepository.findByReviewEntityReviewIdAndThumbnailAndDeletedDateIsNull(reviewEntity.getReviewId(), 1).get(0);
            responseReviewDTOList.add(ResponseReviewDTO.builder()
                    .reviewId(reviewEntity.getReviewId())
                    .title(reviewEntity.getTitle())
                    .writer(reviewEntity.getWriter())
                    .meetEntity(reviewEntity.getMeetEntity())
                    .fileEntity(fileEntity)
                    .build());
        }
        return responseReviewDTOList;
    }

    public ReviewEntity findById(Long reviewId) {
        ReviewEntity reviewEntity = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + reviewId));
        return reviewEntity;
    }

    public ReviewEntity save(ReviewEntity reviewEntity) {
        ReviewEntity savedReviewEntity = reviewRepository.save(reviewEntity);
        return savedReviewEntity;
    }

    @Transactional
    public void update(long reviewId, ReviewEntity reviewEntity) {
        ReviewEntity updateReviewEntity = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + reviewId));
        updateReviewEntity.update(reviewEntity.getTitle(), reviewEntity.getContent(), reviewEntity.getPlaceRate());
    }

}
