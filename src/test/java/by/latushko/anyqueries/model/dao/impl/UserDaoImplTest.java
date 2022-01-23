package by.latushko.anyqueries.model.dao.impl;

import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.model.dao.BaseDao;
import by.latushko.anyqueries.model.dao.UserDao;
import by.latushko.anyqueries.model.entity.User;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class UserDaoImplTest {

    @Mock(extraInterfaces = UserDao.class)
    private BaseDao userDao;
    private List<User> users;

    @BeforeClass
    public void setUp() throws DaoException {
        MockitoAnnotations.openMocks(this);

        users = new ArrayList<>();

        User user = new User();
        user.setId(1L);
        user.setRole(User.Role.USER);
        user.setStatus(User.Status.ACTIVE);
        user.setLogin("urtyser");
        user.setEmail("user@gmail.com");
        user.setTelegram("user123");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setMiddleName("Michael");
        users.add(user);

        user = new User();
        user.setId(2L);
        user.setRole(User.Role.USER);
        user.setStatus(User.Status.INACTIVE);
        user.setLogin("qwerty");
        user.setEmail("qwerty@gmail.com");
        user.setTelegram("qwerty123");
        user.setFirstName("Ivan");
        user.setLastName("Petrov");
        user.setMiddleName("Petrovich");
        users.add(user);

        user = new User();
        user.setId(3L);
        user.setRole(User.Role.USER);
        user.setStatus(User.Status.BANNED);
        user.setLogin("asd_rty");
        user.setEmail("asd@gmail.com");
        user.setTelegram("asd123");
        user.setFirstName("Виталий");
        user.setLastName("Кудряшев");
        user.setMiddleName("Васильевич");
        users.add(user);

        when(userDao.findById(anyLong())).
                thenAnswer(invocation -> users.stream().
                        filter(u -> u.getId().equals(invocation.getArgument(0))).
                        findAny());

        when(((UserDao)userDao).findInactiveByTelegram(anyString())).
                thenAnswer(invocation -> users.stream().
                        filter(u ->
                                u.getTelegram() != null &&
                                u.getTelegram().equals(invocation.getArgument(0)) &&
                                u.getStatus() == User.Status.INACTIVE).
                        findAny());

        when(((UserDao)userDao).findLoginByLoginContainsOrderByLoginAscLimitedTo(anyString(), anyInt())).
                thenAnswer(invocation -> users.stream().
                        filter(u -> u.getLogin().contains(invocation.getArgument(0))).
                        sorted(Comparator.comparing(User::getLogin)).
                        map(User::getLogin).
                        limit(Long.valueOf(invocation.getArgument(1, Integer.class)))
                .toList());

        when(((UserDao) userDao).existsByEmail(anyString())).
                thenAnswer(invocation -> users.stream().
                        anyMatch(u -> u.getEmail() != null && u.getEmail().equals(invocation.getArgument(0))));

        when(((UserDao) userDao).existsByLoginExceptUserId(anyString(), anyLong())).
                thenAnswer(invocation -> users.stream().
                        anyMatch(u -> u.getLogin().equals(invocation.getArgument(0)) &&
                                !u.getId().equals(invocation.getArgument(1))));

        when(userDao.delete(anyLong())).
                thenAnswer(invocation -> users.removeIf(u -> u.getId().equals(invocation.getArgument(0))));

        when(userDao.create(any(User.class))).
                thenAnswer(invocation -> {
                    User u = invocation.getArgument(0, User.class);
                    long lastId = users.stream().max(Comparator.comparingLong(User::getId)).map(User::getId).orElse(0L);
                    u.setId(++lastId);
                    return users.add(u);
                });

        when(userDao.update(any(User.class))).
                thenAnswer(invocation -> {
                    User u = invocation.getArgument(0, User.class);
                    boolean exists = users.stream().anyMatch(ur -> ur.getId().equals(u.getId()));
                    Optional<User> result = Optional.empty();
                    if(exists) {
                        boolean removed = users.removeIf(ur -> ur.getId().equals(ur.getId()));
                        if (removed && users.add(u)) {
                            result = Optional.of(u);
                        }
                    }
                    return result;
                });
    }

    @Test
    public void testFindById() throws DaoException {
        Optional<User> user = userDao.findById(1L);
        assertTrue(user.isPresent());
    }

    @Test
    public void testFindByIdEmpty() throws DaoException {
        Optional<User> user = userDao.findById(999L);
        assertTrue(user.isEmpty());
    }

    @Test
    public void testCreate() throws DaoException {
        User user = new User();
        user.setRole(User.Role.MODERATOR);
        user.setStatus(User.Status.INACTIVE);
        user.setLogin("moder");
        user.setEmail("moder@gmail.com");
        user.setTelegram("moder123");
        user.setFirstName("Lisa");
        user.setLastName("Boss");
        user.setMiddleName("Sidorova");
        boolean result = userDao.create(user);
        assertTrue(result && user.getId() != null);
    }

    @Test
    public void testUpdate() throws DaoException {
        String newName = "Peter";
        Optional<User> userOptional = userDao.findById(1L);
        User u = userOptional.get();
        u.setFirstName(newName);
        userOptional = userDao.update(u);

        assertTrue(userOptional.isPresent() &&
                userOptional.get().getFirstName().equals(newName));
    }

    @Test
    public void testDelete() throws DaoException {
        boolean deleted = userDao.delete(3L);
        assertTrue(deleted);
    }

    @Test
    public void testFindLoginByLoginContainsOrderByLoginAscLimitedTo() throws DaoException {
        List<String> logins = ((UserDao)userDao).findLoginByLoginContainsOrderByLoginAscLimitedTo("rty", 2);
        assertEquals(2, logins.size());
    }

    @Test
    public void testFindLoginByLoginContainsOrderByLoginAscLimitedToEmpty() throws DaoException {
        List<String> logins = ((UserDao)userDao).findLoginByLoginContainsOrderByLoginAscLimitedTo("zzzzz", 2);
        assertTrue(logins.isEmpty());
    }

    @Test
    public void testFindInactiveByTelegram() throws DaoException {
        Optional<User> user = ((UserDao)userDao).findInactiveByTelegram("qwerty123");
        assertTrue(user.isPresent());
    }

    @Test
    public void testFindInactiveByTelegramEmpty() throws DaoException {
        Optional<User> user = ((UserDao)userDao).findInactiveByTelegram("adminTlgrm");
        assertTrue(user.isEmpty());
    }

    @Test
    public void testExistsByEmail() throws DaoException {
        boolean exists = ((UserDao)userDao).existsByEmail("user@gmail.com");
        assertTrue(exists);
    }

    @Test
    public void testNotExistsByEmail() throws DaoException {
        boolean exists = ((UserDao)userDao).existsByEmail("boss@gmail.com");
        assertFalse(exists);
    }

    @Test
    public void testExistsByLoginExceptUserId() throws DaoException {
        boolean exists = ((UserDao)userDao).existsByLoginExceptUserId("qwerty", 1L);
        assertTrue(exists);
    }

    @Test
    public void testNotExistsByLoginExceptUserId() throws DaoException {
        boolean exists = ((UserDao)userDao).existsByLoginExceptUserId("qwerty", 2L);
        assertFalse(exists);
    }

    @AfterClass
    public void tearDown() throws DaoException {
        verify(userDao, atLeast(2)).findById(anyLong());
    }
}