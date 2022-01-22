package by.latushko.anyqueries.service;

import by.latushko.anyqueries.model.entity.Attachment;
import jakarta.servlet.http.Part;

import java.util.List;
import java.util.Optional;

/**
 * The Attachment service interface.
 */
public interface AttachmentService {
    /**
     * Find by question id.
     *
     * @param id the question id
     * @return the list of attachments
     */
    List<Attachment> findByQuestionId(Long id);

    /**
     * Upload file.
     *
     * @param part the http request file part
     * @return the optional with uploaded file or empty one
     */
    Optional<String> uploadFile(Part part);

    /**
     * Upload avatar.
     *
     * @param parts the http request files part
     * @return the optional with uploaded avatar or empty one
     */
    Optional<String> uploadAvatar(List<Part> parts);

    /**
     * Delete avatar.
     *
     * @param avatar the avatar image
     * @return the boolean, true if the file was deleted successfully, otherwise false
     */
    boolean deleteAvatar(String avatar);

    /**
     * Delete file boolean.
     *
     * @param file the file
     * @return the boolean, true if the avatar was deleted successfully, otherwise false
     */
    boolean deleteFile(String file);

    /**
     * Gets a file extension.
     *
     * @param fileName the name of a file
     * @return the file extension
     */
    Optional<String> getFileExtension(String fileName);

    /**
     * Detect the mime type.
     *
     * @param file the file
     * @return the mime type of the file
     */
    String detectMimeType(String file);

    /**
     * Encode file name.
     *
     * @param fileName the file name
     * @return the encoded file name
     */
    String encodeFileName(String fileName);

    /**
     * Gets image path.
     *
     * @param image the image file name
     * @return the full image path
     */
    String getImagePath(String image);

    /**
     * Gets file path.
     *
     * @param file the file name
     * @return the full file path
     */
    String getFilePath(String file);
}
