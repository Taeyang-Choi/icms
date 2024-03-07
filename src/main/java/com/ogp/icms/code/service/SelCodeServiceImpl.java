package com.ogp.icms.code.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ogp.icms.attachment.FileStore;
import com.ogp.icms.attachment.domain.UploadFile;
import com.ogp.icms.board.domain.Article;
import com.ogp.icms.cctv.domain.Camera;
import com.ogp.icms.code.dao.SelCodeRepository;
import com.ogp.icms.code.domain.SelCode;
import com.ogp.icms.global.util.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class SelCodeServiceImpl {
    private final SelCodeRepository selCodeRepository;
    private final FileStore fileStore;

    public ResultCode save(SelCode selCode) {
        selCodeRepository.save(selCode);
        return new ResultCode(0, "새로운 코드를 추가하였습니다.");
    }

    public List<SelCode> getAll() {
        return selCodeRepository.findAll();
    }

    public SelCode getById(Long id) {
        return selCodeRepository.getById(id);
    }
    public SelCode getByName(String name) {
        return selCodeRepository.getByName(name);
    }

    /**
     * 코드 수정
     * @TODO 하위 코드가 있을 경우엔 수정 못하게 해야할것들이 있음.
     */
    public ResultCode modifySelCode(SelCode newCode, Long originalId) {
        log.info("{}", originalId);
        log.info("{}", newCode);
        SelCode old = selCodeRepository.getById(originalId);

        // 기본 코드가 아니라면
        old.setCode(newCode.getCode());
        old.setName(newCode.getName());
        old.setActive(newCode.getActive());
        old.setSeq(newCode.getSeq());
        old.setRemarks(newCode.getRemarks());

        return new ResultCode(0, "코드를 수정였습니다.");
    }

    /**
     * @TODO 하위 코드 있을 시 삭제 불가능하게.
     * @param id
     * @return
     */
    public ResultCode deleteById(Long id) {
        selCodeRepository.deleteById(id);

        return new ResultCode(0, "코드를 삭제 했습니다.");
    }

    public List<SelCode> getSelCodesByCode(String kindCode) {
        return selCodeRepository.getByKindCode(kindCode);
    }

    public ResultCode update(SelCode selCode, List<MultipartFile> image, List<MultipartFile> imageF) throws IOException {
        Optional<SelCode> selCodeOptional = selCodeRepository.findById(selCode.getId());

        if(!selCodeOptional.isPresent()) {
            return new ResultCode(0, "오류가 발생하였습니다.");
        }

        SelCode old = selCodeOptional.get();
        ObjectMapper om = new ObjectMapper();
        ObjectNode imageNode = null;
        if (old.getUserFile() == null) {
            imageNode = om.createObjectNode();
        } else {
            JsonNode jn = om.readTree(old.getUserFile());
            if (jn instanceof ObjectNode) imageNode = (ObjectNode) jn;
        }

        //image
        if (!image.get(0).getOriginalFilename().equals("")) {
            System.out.println(image.get(0).getOriginalFilename());
            List<UploadFile> imgs = fileStore.storeFiles(image, "purpose/");
            UploadFile img = imgs.get(0);

            ObjectNode objectNode = om.createObjectNode();
            objectNode.put("store", img.getStoreFileName());
            objectNode.put("real", img.getUploadFileName());
            imageNode.put("default", objectNode.toString());
        }

        //imageF
        if (!imageF.get(0).getOriginalFilename().equals("")) {
            System.out.println(imageF.get(0).getOriginalFilename());
            List<UploadFile> imgs = fileStore.storeFiles(imageF, "purpose/");
            UploadFile img = imgs.get(0);

            ObjectNode objectNode = om.createObjectNode();
            objectNode.put("store", img.getStoreFileName());
            objectNode.put("real", img.getUploadFileName());
            imageNode.put("focus", objectNode.toString());
        }

        old.setCode(selCode.getCode());
        old.setName(selCode.getName());
        old.setActive(selCode.getActive());
        old.setSeq(selCode.getSeq());
        old.setRemarks(selCode.getRemarks());
        old.setUserFile(imageNode.toString());

        return new ResultCode(0, "코드를 수정였습니다.");
    }
}
