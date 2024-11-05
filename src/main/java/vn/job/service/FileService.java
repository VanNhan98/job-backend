package vn.job.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.job.dto.response.ResFileDTO;
import vn.job.exception.EntityAlreadyExistsException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

@Service
public class FileService {
    @Value("${file.upload-dir}")
    private String baseUri;

    public void createUploadFolder(String folder) throws URISyntaxException, IOException {
        URI uri = new URI(folder);
        Path path = Paths.get(uri);
        File nameFolder = new File(path.toString());
        if (!nameFolder.isDirectory()) {
            Files.createDirectory(nameFolder.toPath());
        } else {
            throw new EntityAlreadyExistsException("Folder already exists");
        }

    }

    public String saveFile(MultipartFile file, String folder) throws URISyntaxException, IOException {
        String finalName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        URI uri = new URI(baseUri + folder + "/" + finalName);
        Path path = Paths.get(uri);
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, path,
                    StandardCopyOption.REPLACE_EXISTING);
        }
        return finalName;
    }


}
