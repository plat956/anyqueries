package by.latushko.anyqueries.model.dao;

import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.model.entity.UserHash;

public interface UserDao {
    boolean createUserHash(UserHash hash) throws DaoException;
}
