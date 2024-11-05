package vn.job.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vn.job.dto.response.ResFileDTO;
import vn.job.dto.response.error.ResponseData;
import vn.job.dto.response.error.ResponseError;
import vn.job.service.FileService;

import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Date;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
@Slf4j
public class FileController {
    private final FileService fileService;

    @Value("${file.upload-dir}")
    private String baseUri;

    @PostMapping("/upload")
    public ResponseData<ResFileDTO> uploadFile(@RequestParam(name = "file", required = false) MultipartFile file,
                                               @RequestParam("folder") String folder) throws URISyntaxException {
        log.info("Uploading file");
        try {
            this.fileService.createUploadFolder(baseUri + folder);

            String uploadFile = this.fileService.saveFile(file, folder);
            ResFileDTO res = new ResFileDTO(uploadFile, new Date());

            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Upload file successfully",res );
        }
        catch (Exception e) {
            log.error("errorMessage= {} ", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Upload file  failed");
        }

    }

}
