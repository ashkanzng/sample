package Contracts;

import Services.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageInterface {
    StorageService store(MultipartFile file) throws Exception;
}
