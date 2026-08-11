//package bloggingApp.services.impl;
//
//import bloggingApp.services.FileService;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.*;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.UUID;
//@Service
//public class FileServiceImpl implements FileService {
//
//
//    @Override
//    public String uploadImage(String path, MultipartFile file) throws IOException {
//
//        String name = file.getOriginalFilename();
//
//        String randomID = UUID.randomUUID().toString();
//        String fileName1=randomID.concat(name.substring(name.lastIndexOf(".")));
//
//        String filePath = path + File.separator + fileName1;
//
//        File f =new File(filePath);
//        if(!f.exists()){
//            f.mkdirs();
//        }
//
//        Files.copy(file.getInputStream(), Paths.get(filePath));
//
//        return fileName1;
//    }
//
//    @Override
//
//    public InputStream getResource(String path, String fileName) throws FileNotFoundException {
//        String fullPath = path + File.separator + fileName;
//        InputStream is =new FileInputStream(fullPath);
//        return is;
//
//
//    }
//}



package Blogging.App.services.impl;

import Blogging.App.services.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class FileServiceImpl implements FileService {

    private static final Logger logger = Logger.getLogger(FileServiceImpl.class.getName());

    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        String originalName = file.getOriginalFilename();
        if (originalName == null) {
            throw new IllegalArgumentException("File name is invalid");
        }

        String randomID = UUID.randomUUID().toString();
        String fileName = randomID.concat(originalName.substring(originalName.lastIndexOf(".")));

        // Use File.separator for platform compatibility
        String filePath = path + File.separator + fileName;

        File directory = new File(path);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                logger.info("Directory created: " + path);
            } else {
                logger.warning("Failed to create directory: " + path);
            }
        }

        // Write the file
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, Paths.get(filePath));
        } catch (IOException e) {
            logger.severe("Error while copying file: " + e.getMessage());
            throw e; // Re-throw exception to handle higher up
        }

        return fileName;
    }

    @Override
    public InputStream getResource(String path, String fileName) throws FileNotFoundException {
        String fullPath = path + File.separator + fileName;

        // Ensure file exists before returning InputStream
        File file = new File(fullPath);
        if (!file.exists()) {
            logger.severe("File not found: " + fullPath);
            throw new FileNotFoundException("File not found: " + fullPath);
        }

        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            logger.severe("Error opening file: " + fullPath);
            throw e;
        }
    }
}
