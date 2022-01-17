package by.latushko.anyqueries.service.impl;

import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.exception.EntityTransactionException;
import by.latushko.anyqueries.model.dao.AttachmentDao;
import by.latushko.anyqueries.model.dao.BaseDao;
import by.latushko.anyqueries.model.dao.EntityTransaction;
import by.latushko.anyqueries.model.dao.UserDao;
import by.latushko.anyqueries.model.dao.impl.AttachmentDaoImpl;
import by.latushko.anyqueries.model.dao.impl.UserDaoImpl;
import by.latushko.anyqueries.model.entity.Attachment;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.model.entity.UserHash;
import by.latushko.anyqueries.service.AttachmentService;
import by.latushko.anyqueries.service.UserService;
import by.latushko.anyqueries.util.encryption.PasswordEncoder;
import by.latushko.anyqueries.util.encryption.impl.BCryptPasswordEncoder;
import by.latushko.anyqueries.util.pagination.Paginated;
import by.latushko.anyqueries.util.pagination.RequestPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static by.latushko.anyqueries.util.AppProperty.APP_ACTIVATION_LINK_ALIVE_HOURS;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LogManager.getLogger();
    private static final String USER_HASH_ADDITIONAL_SALT = "#@бЫрвалГ?";
    private static final String CREDENTIAL_KEY_ADDITIONAL_SALT = "A3>rE(wY%.LA)4V!";
    private static final String CREDENTIAL_TOKEN_ADDITIONAL_SALT = ";{(NP3yE4aG4fkZT";
    private static UserService instance;
    private PasswordEncoder passwordEncoder = BCryptPasswordEncoder.getInstance();

    private UserServiceImpl() {
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserServiceImpl();
        }
        return instance;
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        Optional<User> user = findByLogin(login);
        if(user.isPresent() && passwordEncoder.check(password, user.get().getPassword())) {
            return user;
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByCredentialKeyAndCredentialToken(String key, String token) {
        Optional<User> user = findByCredentialKey(key);
        if(user.isPresent()) {
            String tokenSource = getCredentialTokenSource(user.get());
            if (passwordEncoder.check(tokenSource, token)) {
                return user;
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findById(Long id) {
        Optional<User> user = Optional.empty();
        if(id != null) {
            BaseDao userDao = new UserDaoImpl();
            try (EntityTransaction transaction = new EntityTransaction(userDao)) {
                try {
                    user = userDao.findById(id);
                    transaction.commit();
                } catch (DaoException e) {
                    transaction.rollback();
                }
            } catch (EntityTransactionException e) {
                logger.error("Something went wrong during retrieving user by id", e);
            }
        }
        return user;
    }

    @Override
    public Paginated<User> findPaginatedByLoginContainsOrderByRoleAsc(RequestPage page, String loginPattern) {
        BaseDao userDao = new UserDaoImpl();
        List<User> users = new ArrayList<>();
        try (EntityTransaction transaction = new EntityTransaction(userDao)) {
            try {
                users = ((UserDao)userDao).findByLoginContainsOrderByRoleAscLimitedTo(loginPattern, page.getOffset(), page.getLimit());
                transaction.commit();
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during retrieving users by login containing with limit", e);
        }
        return new Paginated<>(users);
    }

    @Override
    public List<String> findLoginByLoginContainsOrderByLoginAscLimitedTo(String loginPattern, int limit) {
        BaseDao userDao = new UserDaoImpl();
        List<String> logins = new ArrayList<>();
        try (EntityTransaction transaction = new EntityTransaction(userDao)) {
            try {
                logins = ((UserDao)userDao).findLoginByLoginContainsOrderByLoginAscLimitedTo(loginPattern, limit);
                transaction.commit();
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during retrieving users logins by login containing with limit", e);
        }
        return logins;
    }

    @Override
    public boolean existsByLogin(String login) {
        BaseDao userDao = new UserDaoImpl();
        boolean result = false;
        try (EntityTransaction transaction = new EntityTransaction(userDao)) {
            try {
                result = ((UserDao)userDao).existsByLogin(login);
                transaction.commit();
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during checking user existing by login", e);
        }
        return result;
    }

    @Override
    public boolean existsByEmail(String email) {
        BaseDao userDao = new UserDaoImpl();
        boolean result = false;
        try (EntityTransaction transaction = new EntityTransaction(userDao)) {
            try {
                result = ((UserDao)userDao).existsByEmail(email);
                transaction.commit();
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during checking user existing by email", e);
        }
        return result;
    }

    @Override
    public boolean existsByTelegram(String telegram) {
        BaseDao userDao = new UserDaoImpl();
        boolean result = false;
        try (EntityTransaction transaction = new EntityTransaction(userDao)) {
            try {
                result = ((UserDao)userDao).existsByTelegram(telegram);
                transaction.commit();
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during checking user existing by telegram", e);
        }
        return result;
    }

    @Override
    public boolean existsByEmailExceptUserId(String email, Long userId) {
        BaseDao userDao = new UserDaoImpl();
        boolean result = false;
        try (EntityTransaction transaction = new EntityTransaction(userDao)) {
            try {
                result = ((UserDao)userDao).existsByEmailExceptUserId(email, userId);
                transaction.commit();
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during checking user existing by email", e);
        }
        return result;
    }

    @Override
    public boolean existsByTelegramExceptUserId(String telegram, Long userId) {
        BaseDao userDao = new UserDaoImpl();
        boolean result = false;
        try (EntityTransaction transaction = new EntityTransaction(userDao)) {
            try {
                result = ((UserDao)userDao).existsByTelegramExceptUserId(telegram, userId);
                transaction.commit();
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during checking user existing by telegram", e);
        }
        return result;
    }

    @Override
    public boolean existsByLoginExceptUserId(String login, Long userId) {
        BaseDao userDao = new UserDaoImpl();
        boolean result = false;
        try (EntityTransaction transaction = new EntityTransaction(userDao)) {
            try {
                result = ((UserDao)userDao).existsByLoginExceptUserId(login, userId);
                transaction.commit();
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during checking user existing by login", e);
        }
        return result;
    }

    @Override
    public boolean updateLastLoginDate(User user) {
        BaseDao userDao = new UserDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction(userDao)) {
            try {
                user.setLastLoginDate(LocalDateTime.now());
                Optional<User> userOptional = userDao.update(user);
                if(userOptional.isPresent()) {
                    transaction.commit();
                    return true;
                }
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during update user last login date", e);
        }
        return false;
    }

    @Override
    public boolean update(User user, String firstName, String lastName, String middleName,
                          String email, String telegram, String login) {
        BaseDao userDao = new UserDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction(userDao)) {
            try {
                updateUserObject(user, firstName, lastName, middleName, email, telegram, login);
                Optional<User> userOptional = userDao.update(user);
                if(userOptional.isPresent()) {
                    transaction.commit();
                    return true;
                }
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Failed to update user data", e);
        }
        return false;
    }

    @Override
    public boolean update(Long userId, String firstName, String lastName, String middleName, String email,
                          String telegram, String login, User.Status status, User.Role role) {
        if(userId != null) {
            BaseDao userDao = new UserDaoImpl();
            try (EntityTransaction transaction = new EntityTransaction(userDao)) {
                try {
                    Optional<User> userOptional = userDao.findById(userId);
                    if (userOptional.isPresent()) {
                        User user = userOptional.get();
                        updateUserObject(user, firstName, lastName, middleName, email, telegram, login, status, role);
                        userOptional = userDao.update(user);
                        if(userOptional.isPresent()) {
                            transaction.commit();
                            return true;
                        }
                    }
                } catch (DaoException e) {
                    transaction.rollback();
                }
            } catch (EntityTransactionException e) {
                logger.error("Failed to update user data", e);
            }
        }
        return false;
    }

    @Override
    public boolean updateAvatar(User user, String avatar) {
        BaseDao userDao = new UserDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction(userDao)) {
            try {
                AttachmentService attachmentService = AttachmentServiceImpl.getInstance();
                attachmentService.deleteAvatar(user.getAvatar());
                user.setAvatar(avatar);
                Optional<User> userOptional = userDao.update(user);
                if(userOptional.isPresent()) {
                    transaction.commit();
                    return true;
                }
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Failed to update user avatar", e);
        }
        return false;
    }

    @Override
    public boolean delete(Long id) {
        boolean result = false;
        if(id != null) {
            BaseDao userDao = new UserDaoImpl();
            BaseDao attachmentDao = new AttachmentDaoImpl();
            try (EntityTransaction transaction = new EntityTransaction(userDao, attachmentDao)) {
                try {
                    List<Attachment> attachments = ((AttachmentDao) attachmentDao).findByUserId(id);
                    boolean deleteUser = userDao.delete(id);
                    if(deleteUser) {
                        AttachmentService attachmentService = AttachmentServiceImpl.getInstance();
                        for(Attachment a: attachments) {
                            attachmentDao.delete(a.getId());
                            attachmentService.deleteFile(a.getFile());
                        }
                        transaction.commit();
                        result = true;
                    }
                } catch (DaoException e) {
                    transaction.rollback();
                }
            } catch (EntityTransactionException e) {
                logger.error("Failed to delete user", e);
            }
        }
        return result;
    }

    @Override
    public User createUserObject(String firstName, String lastName, String middleName, String email,
                                 String telegram, String login, String password) {
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
        userHash.setHash(passwordEncoder.encode(USER_HASH_ADDITIONAL_SALT + user.getLogin()));
        userHash.setExpires(LocalDateTime.now().plusHours(APP_ACTIVATION_LINK_ALIVE_HOURS));
        return userHash;
    }

    @Override
    public String generateCredentialToken(User user) {
        String tokenSource = getCredentialTokenSource(user);
        return passwordEncoder.encode(tokenSource);
    }

    @Override
    public boolean changePassword(User user, String password) {
        BaseDao userDao = new UserDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction(userDao)) {
            try {
                user.setPassword(passwordEncoder.encode(password));
                Optional<User> userOptional = userDao.update(user);
                if(userOptional.isPresent()) {
                    transaction.commit();
                    return true;
                }
            } catch (DaoException e) {
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

    private Optional<User> findByLogin(String login) {
        BaseDao userDao = new UserDaoImpl();
        Optional<User> userOptional = Optional.empty();
        try (EntityTransaction transaction = new EntityTransaction(userDao)) {
            try {
                userOptional = ((UserDao)userDao).findByLogin(login);
                transaction.commit();
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during retrieving user by login", e);
        }
        return userOptional;
    }

    private Optional<User> findByCredentialKey(String key) {
        BaseDao userDao = new UserDaoImpl();
        Optional<User> userOptional = Optional.empty();
        try (EntityTransaction transaction = new EntityTransaction(userDao)) {
            try {
                userOptional = ((UserDao)userDao).findByCredentialKey(key);
                transaction.commit();
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during retrieving user by credential key", e);
        }
        return userOptional;
    }

    private String getCredentialTokenSource(User user) {
        return CREDENTIAL_TOKEN_ADDITIONAL_SALT + user.getLogin();
    }

    private void updateUserObject(User user, String firstName, String lastName, String middleName,
                                  String email, String telegram, String login) {
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setMiddleName(middleName);
        user.setEmail(email);
        user.setTelegram(telegram);
        user.setLogin(login);
    }

    private void updateUserObject(User user, String firstName, String lastName, String middleName,
                                  String email, String telegram, String login, User.Status status, User.Role role) {
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setMiddleName(middleName);
        user.setEmail(email);
        user.setTelegram(telegram);
        user.setLogin(login);
        user.setStatus(status);
        user.setRole(role);
    }
}
