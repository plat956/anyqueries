package by.latushko.anyqueries.validator.impl;

import by.latushko.anyqueries.util.file.FileHelper;
import by.latushko.anyqueries.validator.FileUploadValidator;

import java.util.Optional;

import static by.latushko.anyqueries.util.AppProperty.APP_UPLOAD_AVATAR_EXTENSIONS;

public class UploadAvatarValidator implements FileUploadValidator {
    private static FileUploadValidator instance;

    private UploadAvatarValidator() {
    }

    public static FileUploadValidator getInstance() {
        if(instance == null) {
            instance = new UploadAvatarValidator();
        }
        return instance;
    }

    @Override
    public boolean validate(String fileName) {
        if(fileName == null || fileName.isEmpty()) {
            return false;
        }
        Optional<String> extension = FileHelper.getExtension(fileName);
        if(extension.isEmpty() || (extension.isPresent() && !APP_UPLOAD_AVATAR_EXTENSIONS.contains(extension.get()))) {
            return false;
        }
        return true;
    }


}
