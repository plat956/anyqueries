package by.latushko.anyqueries.model.dao;

import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.model.entity.Rating;

import java.util.Optional;

public interface RatingDao {
    Optional<Rating> findByAnswerIdAndUserId(Long answerId, Long userId) throws DaoException;
    Integer sumGradeByAnswerId(Long id) throws DaoException;
}
