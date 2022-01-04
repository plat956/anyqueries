package by.latushko.anyqueries.service.impl;

import by.latushko.anyqueries.service.AttachmentService;
import by.latushko.anyqueries.util.file.FileHelper;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Optional;

public class AttachmentServiceImpl implements AttachmentService {
    private static final String UNDER_SCORE_CHARACTER = "_";
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
        createFileUploadDirectoryIfNotExists();
        String fileName = part.getSubmittedFileName();
        String ext = FileHelper.getExtension(fileName).get();
        fileName += UNDER_SCORE_CHARACTER + Calendar.getInstance().getTimeInMillis() + ext;
        try {
            part.write(FILE_DIRECTORY_PATH + fileName);
            return Optional.of(fileName);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    private void createFileUploadDirectoryIfNotExists() {
        File uploadDir = new File(FILE_DIRECTORY_PATH);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }
}
