package by.latushko.anyqueries.service.impl;

import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.exception.EntityTransactionException;
import by.latushko.anyqueries.model.dao.AttachmentDao;
import by.latushko.anyqueries.model.dao.BaseDao;
import by.latushko.anyqueries.model.dao.EntityTransaction;
import by.latushko.anyqueries.model.dao.impl.AttachmentDaoImpl;
import by.latushko.anyqueries.model.entity.Attachment;
import by.latushko.anyqueries.service.AttachmentService;
import jakarta.servlet.http.Part;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AttachmentServiceImpl implements AttachmentService {
    private static final Logger logger = LogManager.getLogger();
    private static final String FILE_EXTENSION_DELIMITER = ".";
    private static final String AVATAR_PREFIX = "avatar_";
    private static final int AVATAR_MAX_SIZE = 190;
    private static final int FILE_EXTENSION_MAX_LENGTH = 20;
    private static final int FILE_NAME_MAX_LENGTH = 40;
    private static final String FILE_EMPTY_NAME = "noname";
    private static AttachmentService instance;

    private AttachmentServiceImpl() {
    }

    public static AttachmentService getInstance() {
        if (instance == null) {
            instance = new AttachmentServiceImpl();
        }
        return instance;
    }

    @Override
    public Optional<String> uploadFile(Part part) {
        createUploadDirectoryIfNotExists(FILE_DIRECTORY_PATH);
        String fileName = part.getSubmittedFileName();
        Optional<String> extension = getFileExtension(fileName);
        if(fileName.contains(FILE_EXTENSION_DELIMITER)) {
            fileName = fileName.substring(0, fileName.lastIndexOf(FILE_EXTENSION_DELIMITER));
        }
        if(fileName.isEmpty()) {
            fileName = FILE_EMPTY_NAME;
        } else if(fileName.length() > FILE_NAME_MAX_LENGTH) {
            fileName = fileName.substring(0, FILE_NAME_MAX_LENGTH);
        }
        fileName = String.format("%s_%s%s", fileName, RandomStringUtils.randomAlphanumeric(8), extension.orElse(""));
        try {
            part.write(FILE_DIRECTORY_PATH + fileName);
            return Optional.of(fileName);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> uploadAvatar(List<Part> parts) {
        if(parts.isEmpty()) {
            return Optional.empty();
        }
        Part part = parts.get(0);
        createUploadDirectoryIfNotExists(IMAGE_DIRECTORY_PATH);
        String fileName = part.getSubmittedFileName();
        Optional<String> extension = getFileExtension(fileName);
        if(extension.isEmpty()) {
            return Optional.empty();
        }
        fileName = AVATAR_PREFIX + RandomStringUtils.randomAlphanumeric(25) + extension.get();
        try {
            part.write(IMAGE_DIRECTORY_PATH + fileName);
            boolean resizeResult = resizeAvatar(fileName);
            if(resizeResult) {
                return Optional.of(fileName);
            } else {
                deleteAvatar(fileName);
                return Optional.empty();
            }
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean deleteAvatar(String avatar) {
        try {
            return Files.deleteIfExists(Paths.get(IMAGE_DIRECTORY_PATH + avatar));
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public Optional<String> getFileExtension(String fileName) {
        if(fileName != null && !fileName.isEmpty() && fileName.contains(FILE_EXTENSION_DELIMITER)) {
            String extension = fileName.substring(fileName.lastIndexOf(FILE_EXTENSION_DELIMITER));
            if(extension.length() - 1 > FILE_EXTENSION_MAX_LENGTH) {
                extension = extension.substring(0, FILE_EXTENSION_MAX_LENGTH + 1);
            }
            return Optional.of(extension);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public boolean deleteFile(String file) {
        try {
            return Files.deleteIfExists(Paths.get(FILE_DIRECTORY_PATH + file));
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean deleteAttachmentsFiles(List<Attachment> attachments) {
        for(Attachment a: attachments) {
            boolean result = deleteFile(a.getFile());
            if(!result) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<Attachment> findByQuestionId(Long id) {
        BaseDao attachmentDao = new AttachmentDaoImpl();
        List<Attachment> attachments = new ArrayList<>();
        try (EntityTransaction transaction = new EntityTransaction(attachmentDao)) {
            try {
                attachments = ((AttachmentDao)attachmentDao).findByQuestionId(id);
                transaction.commit();
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during retrieving attachment by question id", e);
        }
        return attachments;
    }

    private boolean resizeAvatar(String avatar) {
        try {
            String file = IMAGE_DIRECTORY_PATH + avatar;
            File inputFile = new File(file);
            BufferedImage inputImage = ImageIO.read(inputFile);
            BufferedImage outputImage = new BufferedImage(AVATAR_MAX_SIZE, AVATAR_MAX_SIZE, inputImage.getType());
            Graphics2D g2d = outputImage.createGraphics();
            g2d.drawImage(inputImage, 0, 0, AVATAR_MAX_SIZE, AVATAR_MAX_SIZE, null);
            g2d.dispose();
            String formatName = file.substring(file.lastIndexOf(".") + 1);
            ImageIO.write(outputImage, formatName, new File(file));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private void createUploadDirectoryIfNotExists(String directory) {
        File uploadDir = new File(directory);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }
}
