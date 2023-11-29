package com.universe.wujuplay.common.file;


import com.universe.wujuplay.review.model.FileDTO;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileUtils {
// TODO: 저장 경로 변경하기
    //private final String uploadPath = Paths.get("C:", "review", "upload-files").toString();

    /**
     * 다중 파일 업로드
     * @param multipartFiles - 파일 객체 List
     * @return DB에 저장할 파일 정보 List
     */
    public List<FileDTO> uploadFiles(final List<MultipartFile> multipartFiles, List<String> url) {
        List<FileDTO> files = new ArrayList<>();

        for(int i=0; i<multipartFiles.size(); i++){
            if (multipartFiles.get(i).isEmpty()) {
                continue;
            }
            files.add(uploadFile(multipartFiles.get(i), url.get(i)));
        }

        return files;
    }

    /**
     * 단일 파일 업로드
     * @param multipartFile - 파일 객체
     * @return DB에 저장할 파일 정보
     */
    public FileDTO uploadFile(final MultipartFile multipartFile, String url) {

        if (multipartFile.isEmpty()) {
            return null;
        }

        String saveName = generateSaveFilename(multipartFile.getOriginalFilename()); // fileName
        //String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd")).toString();
        String uploadPath = url;
        //File uploadFile = new File(uploadPath);

//        try {
//            multipartFile.transferTo(uploadFile);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        return FileDTO.builder()
                .originName(multipartFile.getOriginalFilename())
                .fileName(saveName)
                .path(uploadPath)
                .size(multipartFile.getSize())
                .build();
    }

    /**
     * 저장 파일명 생성
     * @param filename 원본 파일명
     * @return 디스크에 저장할 파일명
     */
    private String generateSaveFilename(final String filename) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String extension = StringUtils.getFilenameExtension(filename);
        return uuid + "." + extension;
    }

    /**
     * 업로드 경로 반환
     * @return 업로드 경로
     */
//    private String getUploadPath() {
//        return makeDirectories(uploadPath);
//    }

    /**
     * 업로드 경로 반환
     * @param addPath - 추가 경로
     * @return 업로드 경로
     */
//    private String getUploadPath(final String addPath) {
//        return makeDirectories(uploadPath + File.separator + addPath);
//    }private String getUploadPath(final String addPath) {
//        return makeDirectories(uploadPath + File.separator + addPath);
//    }

    /**
     * 업로드 폴더(디렉터리) 생성
     * @param path - 업로드 경로
     * @return 업로드 경로
     */
    private String makeDirectories(final String path) {
        File dir = new File(path);
        if (dir.exists() == false) {
            dir.mkdirs();
        }
        return dir.getPath();
    }

}