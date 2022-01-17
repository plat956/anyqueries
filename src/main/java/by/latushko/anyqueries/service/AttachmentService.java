package by.latushko.anyqueries.service;

import by.latushko.anyqueries.model.entity.Attachment;
import jakarta.servlet.http.Part;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static by.latushko.anyqueries.util.AppProperty.APP_UPLOAD_DIR;

public interface AttachmentService {
    String IMAGE_DIRECTORY_PATH = APP_UPLOAD_DIR + File.separator + "images" + File.separator;
    String FILE_DIRECTORY_PATH = APP_UPLOAD_DIR + File.separator + "files" + File.separator;

    List<Attachment> findByQuestionId(Long id);
    Optional<String> uploadFile(Part part);
    Optional<String> uploadAvatar(List<Part> parts);
    boolean deleteAvatar(String avatar);
    boolean deleteFile(String file);
    boolean deleteAttachmentsFiles(List<Attachment> attachments);
    Optional<String> getFileExtension(String fileName);
}
