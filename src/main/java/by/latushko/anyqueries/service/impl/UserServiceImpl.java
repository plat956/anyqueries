package by.latushko.anyqueries.service.impl;

import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.exception.EntityTransactionException;
import by.latushko.anyqueries.model.dao.BaseDao;
import by.latushko.anyqueries.model.dao.EntityTransaction;
import by.latushko.anyqueries.model.dao.UserDao;
import by.latushko.anyqueries.model.dao.impl.UserDaoImpl;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.model.entity.UserHash;
import by.latushko.anyqueries.service.UserService;
import by.latushko.anyqueries.util.encryption.PasswordEncoder;
import by.latushko.anyqueries.util.encryption.impl.BCryptPasswordEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Optional;

import static by.latushko.anyqueries.service.AttachmentService.IMAGE_DIRECTORY_PATH;
import static by.latushko.anyqueries.util.AppProperty.APP_ACTIVATION_LINK_ALIVE_HOURS;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LogManager.getLogger();
    private static final String ACTIVATION_HASH_ADDITIONAL_SALT = "#@бЫрвалГ?";
    private static final String CREDENTIAL_KEY_ADDITIONAL_SALT = "A3>rE(wY%.LA)4V!";
    private static final String CREDENTIAL_TOKEN_ADDITIONAL_SALT = ";{(NP3yE4aG4fkZT";
    private static UserService instance;
    private final PasswordEncoder passwordEncoder = BCryptPasswordEncoder.getInstance();

    private UserServiceImpl() {
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserServiceImpl();
        }
        return instance;
    }

    @Override
    public User createNewUser(String firstName, String lastName, String middleName, String email, String telegram, String login, String password) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setMiddleName(middleName);
        user.setEmail(email);
        user.setTelegram(telegram);
        user.setLogin(login);
        user.setCredentialKey(passwordEncoder.encode(CREDENTIAL_KEY_ADDITIONAL_SALT + login));
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(User.Role.USER);
        user.setStatus(User.Status.INACTIVE);
        return user;
    }

    @Override
    public UserHash generateUserHash(User user) {
        UserHash userHash = new UserHash();
        userHash.setUser(user);
        userHash.setHash(passwordEncoder.encode(ACTIVATION_HASH_ADDITIONAL_SALT + user.getLogin()));
        userHash.setExpires(LocalDateTime.now().plusHours(APP_ACTIVATION_LINK_ALIVE_HOURS));
        return userHash;
    }

    @Override
    public Optional<User> findUserByLogin(String login) {
        BaseDao userDao = new UserDaoImpl();

        Optional<User> userOptional = Optional.empty();
        try (EntityTransaction transaction = new EntityTransaction(userDao)) {
            try {
                userOptional = ((UserDao)userDao).findUserByLogin(login);
                transaction.commit();
            } catch (EntityTransactionException | DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during retrieving user by login", e);
        }
        return userOptional;
    }

    @Override
    public Optional<User> findUserByCredentialKey(String key) {
        BaseDao userDao = new UserDaoImpl();

        Optional<User> userOptional = Optional.empty();
        try (EntityTransaction transaction = new EntityTransaction(userDao)) {
            try {
                userOptional = ((UserDao)userDao).findUserByCredentialKey(key);
                transaction.commit();
            } catch (EntityTransactionException | DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during retrieving user by credential key", e);
        }
        return userOptional;
    }

    @Override
    public boolean updateLastLoginDate(User user) {
        boolean result = false;
        user.setLastLoginDate(LocalDateTime.now());
        BaseDao userDao = new UserDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction(userDao)) {
            try {
                userDao.update(user);
                transaction.commit();
                result = true;
            } catch (EntityTransactionException | DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during update user last login date", e);
        }
        return result;
    }

    @Override
    public Optional<User> findIfExistsByLoginAndPassword(String login, String password) {
        Optional<User> user = findUserByLogin(login);
        if(user.isPresent() && passwordEncoder.check(password, user.get().getPassword())) {
            return user;
        }
        return Optional.empty();
    }

    @Override
    public String getCredentialToken(User user) {
        String tokenSource = getCredentialTokenSource(user);
        return passwordEncoder.encode(tokenSource);
    }

    @Override
    public Optional<User> findIfExistsByCredentialsKeyAndToken(String key, String token) {
        Optional<User> user = findUserByCredentialKey(key);
        if(user.isPresent()) {
            String tokenSource = getCredentialTokenSource(user.get());
            if (passwordEncoder.check(tokenSource, token)) {
                return user;
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean checkIfExistsByLogin(String login) {
        BaseDao userDao = new UserDaoImpl();
        boolean result = false;
        try (EntityTransaction transaction = new EntityTransaction(userDao)) {
            try {
                result = ((UserDao)userDao).existsByLogin(login);
                transaction.commit();
            } catch (EntityTransactionException | DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during checking user existing by login", e);
        }
        return result;
    }

    @Override
    public boolean checkIfExistsByEmail(String email) {
        BaseDao userDao = new UserDaoImpl();
        boolean result = false;
        try (EntityTransaction transaction = new EntityTransaction(userDao)) {
            try {
                result = ((UserDao)userDao).existsByEmail(email);
                transaction.commit();
            } catch (EntityTransactionException | DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during checking user existing by email", e);
        }
        return result;
    }

    @Override
    public boolean checkIfExistsByTelegram(String telegram) {
        BaseDao userDao = new UserDaoImpl();
        boolean result = false;
        try (EntityTransaction transaction = new EntityTransaction(userDao)) {
            try {
                result = ((UserDao)userDao).existsByTelegram(telegram);
                transaction.commit();
            } catch (EntityTransactionException | DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during checking user existing by telegram", e);
        }
        return result;
    }

    @Override
    public boolean checkIfExistsByEmailExceptUserId(String email, Long userId) {
        BaseDao userDao = new UserDaoImpl();
        boolean result = false;
        try (EntityTransaction transaction = new EntityTransaction(userDao)) {
            try {
                result = ((UserDao)userDao).existsByEmailExceptUserId(email, userId);
                transaction.commit();
            } catch (EntityTransactionException | DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during checking user existing by email", e);
        }
        return result;
    }

    @Override
    public boolean checkIfExistsByTelegramExceptUserId(String telegram, Long userId) {
        BaseDao userDao = new UserDaoImpl();
        boolean result = false;
        try (EntityTransaction transaction = new EntityTransaction(userDao)) {
            try {
                result = ((UserDao)userDao).existsByTelegramExceptUserId(telegram, userId);
                transaction.commit();
            } catch (EntityTransactionException | DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during checking user existing by telegram", e);
        }
        return result;
    }

    @Override
    public boolean checkIfExistsByLoginExceptUserId(String login, Long userId) {
        BaseDao userDao = new UserDaoImpl();
        boolean result = false;
        try (EntityTransaction transaction = new EntityTransaction(userDao)) {
            try {
                result = ((UserDao)userDao).existsByLoginExceptUserId(login, userId);
                transaction.commit();
            } catch (EntityTransactionException | DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during checking user existing by login", e);
        }
        return result;
    }

    @Override
    public boolean updateUserData(User user, String firstName, String lastName, String middleName, String email, String telegram, String login) {
        BaseDao userDao = new UserDaoImpl();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setMiddleName(middleName);
        user.setEmail(email);
        user.setTelegram(telegram);
        user.setLogin(login);
        try (EntityTransaction transaction = new EntityTransaction(userDao)) {
            try {
                userDao.update(user);
                transaction.commit();
                return true;
            } catch (EntityTransactionException | DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Failed to update user profile data", e);
        }
        return false;
    }

    @Override
    public boolean changePassword(User user, String password) {
        BaseDao userDao = new UserDaoImpl();
        user.setPassword(passwordEncoder.encode(password));
        try (EntityTransaction transaction = new EntityTransaction(userDao)) {
            try {
                userDao.update(user);
                transaction.commit();
                return true;
            } catch (EntityTransactionException | DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Failed to update user password", e);
        }
        return false;
    }

    @Override
    public boolean checkPassword(User user, String password) {
        return passwordEncoder.check(password, user.getPassword());
    }

    @Override
    public boolean updateAvatar(User user, String avatar) {
        BaseDao userDao = new UserDaoImpl();
        String oldAvatar = IMAGE_DIRECTORY_PATH + user.getAvatar();
        user.setAvatar(avatar);
        try (EntityTransaction transaction = new EntityTransaction(userDao)) {
            try {
                Files.deleteIfExists(new File(oldAvatar).toPath());
                userDao.update(user);
                transaction.commit();
                return true;
            } catch (IOException | EntityTransactionException | DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Failed to update user avatar", e);
        }
        return false;
    }

    @Override
    public boolean resizeAvatar(String avatar) {
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

    @Override
    public String findUserAvatar(Long userId) {
        BaseDao userDao = new UserDaoImpl();
        Optional<String> avatar = null;
        try (EntityTransaction transaction = new EntityTransaction(userDao)) {
            try {
                avatar = ((UserDao)userDao).findAvatarByUserId(userId);
                transaction.commit();

                if(avatar != null) {
                    return IMAGE_DIRECTORY_PATH + avatar.get();
                } else {
                    return null;
                }
            } catch (EntityTransactionException | DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Failed to find user avatar", e);
        }
        return null;
    }

    private String getCredentialTokenSource(User user) {
        return CREDENTIAL_TOKEN_ADDITIONAL_SALT + user.getLogin();
    }
}
