package com.universe.wujuplay.S3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AwsS3Service {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public List<String> uploadS3(List<MultipartFile> multipartlist) {
        // 이미지 url 받아올 리스트
        List<String> imageUrls = new ArrayList<>();

        for (MultipartFile file : multipartlist) {
            String fileName = createFileName(file.getOriginalFilename()); // 파일 이름을 생성
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            System.out.println("Uploading photo: " + fileName);

            try (InputStream inputStream = file.getInputStream()) {
                // S3에 업로드 및 저장
                amazonS3.putObject(new PutObjectRequest(bucket + "/Review", fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Photo upload failed.");
            }

            // Accessible URL 가져옴
            imageUrls.add(amazonS3.getUrl(bucket + "/Review", fileName).toString());

        }return imageUrls;
    }

    // uploadDB는 BoardService 에서

    // S3에 저장되어있는 미디어 파일 삭제
    public void deleteFile(String fileName) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
    }


    // 파일 업로드 시 중복 피하기 위해 파일명 난수화
    public String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }


    // file 형식이 잘못된 경우를 확인하기 위한 것, 파일 타입과 상관없이 업로드할 수 있게 하기 위해 .의 존재 유무만 판단
    public String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일(" + fileName + ") 입니다.");
        }
    }
}
