package by.latushko.anyqueries.service;

import by.latushko.anyqueries.model.entity.Attachment;
import jakarta.servlet.http.Part;

import java.util.List;
import java.util.Optional;

public interface AttachmentService {
    List<Attachment> findByQuestionId(Long id);
    Optional<String> uploadFile(Part part);
    Optional<String> uploadAvatar(List<Part> parts);
    boolean deleteAvatar(String avatar);
    boolean deleteFile(String file);
    Optional<String> getFileExtension(String fileName);
    String detectMimeType(String file);
    String encodeFileName(String fileName);
    String getImagePath(String image);
    String getFilePath(String file);
}
