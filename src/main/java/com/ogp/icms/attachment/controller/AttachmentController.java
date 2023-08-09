package com.ogp.icms.attachment.controller;

import com.ogp.icms.attachment.FileStore;
import com.ogp.icms.attachment.domain.UploadFile;
import com.ogp.icms.global.util.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.UUID;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AttachmentController {
    private final FileStore fileStore;

    @Value("${file.dir}")
    private String fileDir;

    /**
     * @TODO 특정 권한 가진 사람만 파일에 접근 가능하게 수정
     */
    @GetMapping("/attach/{path}/{originId}/{fileName}/{realName}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable String path, @PathVariable Long originId, @PathVariable String fileName,
                                                   @PathVariable String realName) throws MalformedURLException {

        UrlResource resource = new UrlResource("file:" + fileStore.getFullPath(path + "/" + fileName));

        String encodedUploadFileName = UriUtils.encode(realName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }

    /**
     * @TODO 특정 권한 가진 사람만 파일에 접근 가능하게 수정
     */
//    @PostMapping("/api/pictures/upload")
//    @ResponseBody
//    public List<String> uploadImages(List<MultipartFile> picture, String name) {
//        List<String> fileNames = new ArrayList<>();
//
//        for (MultipartFile file : picture) {
//            if (file.isEmpty()) {
//                continue; // 빈 파일은 건너뜁니다.
//            }
//
//            String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
//            String newFileName = name + "." +fileStore.extractExt(originalFileName);
//
//            try {
//                File destFile = new File(fileDir+"members/" + File.separator + newFileName);
//                file.transferTo(destFile);
//                fileNames.add(newFileName);
//            } catch (IOException e) {
//                e.printStackTrace();
//                // 파일 저장 중에 예외가 발생하면 처리할 로직을 추가하세요
//            }
//        }
//
//        return fileNames;
//    }
    @PostMapping("/api/pictures/upload")
    @ResponseBody
    public List<String> uploadImages(List<MultipartFile> picture, String name) {
        List<String> fileNames = new ArrayList<>();

        for (MultipartFile file : picture) {
            if (file.isEmpty()) {
                continue; // 빈 파일은 건너뜁니다.
            }

            String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
            String newFileName = name + "." + fileStore.extractExt(originalFileName);

            try {
                File destFile;
                if (file.getContentType().equalsIgnoreCase("image/jpeg")) {
                    // jpg 파일인 경우 png로 변환하여 저장
                    BufferedImage image = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
                    destFile = new File(fileDir + "members/" + File.separator + name + ".png");
                    ImageIO.write(image, "png", destFile);
                } else {
                    // jpg가 아닌 경우 그대로 저장
                    destFile = new File(fileDir + "members/" + File.separator + newFileName);
                    file.transferTo(destFile);
                }

                fileNames.add(newFileName);
            } catch (IOException e) {
                e.printStackTrace();
                // 파일 저장 중에 예외가 발생하면 처리할 로직을 추가하세요
            }
        }

        return fileNames;
    }

    @GetMapping("/pictures/{name}")
    public ResponseEntity<Resource> pictureAttach(@PathVariable String name) throws MalformedURLException {
        String filePath = fileStore.getFullPath("members/" + name + ".png");
        File file = new File(filePath);

        Resource resource;
        String contentDisposition;

        if (!file.exists()) {
            resource = new ClassPathResource("static/images/default.png");
            contentDisposition = "attachment; filename=\"default.png\"";
        }
        else {
            resource = new FileSystemResource(file);
            contentDisposition = "attachment; filename=\"" + name + ".png" + "\"";
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }

}
