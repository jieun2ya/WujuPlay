package com.universe.wujuplay.review.controller;

import com.universe.wujuplay.S3.AwsS3Service;
import com.universe.wujuplay.common.file.FileUtils;
import com.universe.wujuplay.meet.model.MeetEntity;
import com.universe.wujuplay.meet.service.MeetService;
import com.universe.wujuplay.member.model.MemberEntity;
import com.universe.wujuplay.member.model.MemberResponse;
import com.universe.wujuplay.member.service.MemberService;
import com.universe.wujuplay.review.model.*;
import com.universe.wujuplay.review.service.FileService;
import com.universe.wujuplay.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/review")
@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final FileService fileService;
    private final MemberService memberService;
    private final MeetService meetService;
    private final FileUtils fileUtils;
    private final AwsS3Service awsS3Service;



    // 리뷰 전체 목록
    @GetMapping
    public String reviewList(Model model, HttpServletRequest request) {
        Long memberId = memberService.getMemberIdFromAccessTokenCookie(request);
        List<ResponseReviewDTO> responseReviewDTOList = reviewService.reviewList();

        model.addAttribute("review", responseReviewDTOList);
        model.addAttribute("memberId", memberId);
        return "review/reviewList";
    }

    // 리뷰 상세 보기
    @GetMapping("/{reviewId}")
    public String reviewDetail(@PathVariable long reviewId, HttpServletRequest request, Model model) {

        ReviewEntity reviewEntity = reviewService.findById(reviewId);
        ReviewDTO reviewDTO = ReviewDTO.from(reviewEntity);

        model.addAttribute("review", reviewDTO);

        // 로그인한 회원과 리뷰 작성자가 동일한지 확인
        Long memberId = memberService.getMemberIdFromAccessTokenCookie(request);
        boolean isWriter = reviewEntity.getWriter().getMemberId().equals(memberId);

        model.addAttribute("isWriter", isWriter);

        // 썸네일 파일
        FileEntity thumbnail = fileService.findThumbnailByReviewId(reviewId).get(0);

        model.addAttribute("thumbnail", thumbnail);

        // 추가 이미지 파일
        List<FileEntity> fileEntityList = fileService.findAllByReviewId(reviewId);
        List<FileDTO> fileDTOList = new ArrayList<>();
        for (FileEntity fileEntity : fileEntityList) {
            fileDTOList.add(FileDTO.from(fileEntity));
        }

        model.addAttribute("file", fileDTOList);
        model.addAttribute("memberId", memberId);
        return "review/reviewDetail";
    }

    // 리뷰 작성 페이지
    @GetMapping("/{meetId}/new")
    public String addReview(@PathVariable("meetId") long meetId, Model model, HttpServletRequest request) {
        Long memberId = memberService.getMemberIdFromAccessTokenCookie(request);

        MeetEntity meetEntity = meetService.findById(meetId);
        ReviewEntity reviewEntity = ReviewEntity.builder().build();
        ReviewDTO reviewDTO = ReviewDTO.from(reviewEntity);
        reviewDTO.setMeetEntity(meetEntity);

        model.addAttribute("review", reviewDTO);
        model.addAttribute("memberId", memberId);
        return "review/createOrUpdateReview";
    }

    // 리뷰 작성 등록 처리
    @PostMapping("/{meetId}/new")
    public String addReview(HttpServletRequest request, ReviewDTO reviewDTO, @RequestParam("thumbnail") MultipartFile thumbnailFile, @RequestParam("multipartFile") MultipartFile[] multipartFiles) {

        Long memberId = memberService.getMemberIdFromAccessTokenCookie(request);
        MemberEntity loginMemberEntity = memberService.findById(memberId);
        reviewDTO.setWriter(loginMemberEntity);
        ReviewEntity reviewEntity = reviewService.save(reviewDTO.toEntity());

        // 썸네일 등록
        List<String> thumbnailFileURL = awsS3Service.uploadS3(Arrays.asList(thumbnailFile));
        FileDTO thumbnailDTO = fileUtils.uploadFile(thumbnailFile,thumbnailFileURL.get(0));
        thumbnailDTO.setThumbnail(1);
        fileService.saveFile(reviewEntity, thumbnailDTO);

        // 추가 이미지 등록
        if (multipartFiles != null && multipartFiles.length > 0 && !multipartFiles[0].isEmpty()) {
            List<String> fileURL = awsS3Service.uploadS3((Arrays.asList(multipartFiles)));
            List<FileDTO> fileDTOList = fileUtils.uploadFiles((Arrays.asList(multipartFiles)), fileURL);

            fileService.saveFiles(reviewEntity, fileDTOList);
        }

        return "redirect:/review";
    }

    // 리뷰 수정 페이지
    @GetMapping("/{reviewId}/edit")
    public String updateReview(@PathVariable("reviewId") long reviewId, Model model, HttpServletRequest request) {
        Long memberId = memberService.getMemberIdFromAccessTokenCookie(request);

        ReviewEntity reviewEntity = reviewService.findById(reviewId);
        ReviewDTO updateReviewDTO = ReviewDTO.from(reviewEntity);

        model.addAttribute("review", updateReviewDTO);

        // 썸네일 파일
        FileEntity thumbnail = fileService.findThumbnailByReviewId(reviewId).get(0);

        model.addAttribute("thumbnail", thumbnail);

        // 추가 이미지 파일
        List<FileEntity> fileEntityList = fileService.findAllByReviewId(reviewId);
        List<FileDTO> fileDTOList = new ArrayList<>();
        for (FileEntity fileEntity : fileEntityList) {
            fileDTOList.add(FileDTO.from(fileEntity));
        }

        model.addAttribute("file", fileDTOList);
        model.addAttribute("memberId", memberId);
        return "review/createOrUpdateReview";
    }

    // 리뷰 수정 처리
    // 리뷰 수정 시 파일 수정/삭제하면 파일 deletedDate 업데이트 처리
    @PostMapping("/{reviewId}/edit")
    public String updateReview(@PathVariable("reviewId") long reviewId, ReviewDTO reviewDTO, @RequestParam(value = "multipartFile", required = false) MultipartFile[] multipartFiles, @RequestParam(required = false) String idList) {
        ReviewEntity reviewEntity = reviewService.findById(reviewId);
        reviewService.update(reviewId, reviewDTO.toEntity());

        // 새로운 파일 처리
        if (multipartFiles != null && multipartFiles.length > 0 && !multipartFiles[0].isEmpty()) {
            List<String> fileURL = awsS3Service.uploadS3((Arrays.asList(multipartFiles)));
            List<FileDTO> fileDTOList = fileUtils.uploadFiles((Arrays.asList(multipartFiles)), fileURL);
            fileService.saveFiles(reviewEntity, fileDTOList);
        }

        // 파일 삭제 처리
        if (idList != null && !idList.isEmpty()) {
            List<Long> ids = Arrays.stream(idList.split(","))
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
            fileService.deleteByIdIn(ids);
        }

        return "redirect:/review/" + reviewId;
    }

    // 리뷰 검색
    @GetMapping("/search")
    public String search(@RequestParam(value = "keyword", required = false) String keyword, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        Long memberId = memberService.getMemberIdFromAccessTokenCookie(request);

        if (keyword == null || keyword.trim().isEmpty()) {
            return "redirect:/review";
        }
        List<ResponseReviewDTO> responseReviewDTOList = reviewService.searchList(keyword);
        if (responseReviewDTOList.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "검색 결과가 없습니다.");
            return "redirect:/review";
        }
        model.addAttribute("review", responseReviewDTOList);
        model.addAttribute("memberId", memberId);
        return "review/reviewList";
    }

}