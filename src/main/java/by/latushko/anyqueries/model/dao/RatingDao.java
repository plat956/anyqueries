package by.latushko.anyqueries.model.dao;

import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.model.entity.Rating;

import java.util.Optional;

/**
 * The Rating Data Access Object interface.
 * Describes signatures of this DAO implementation methods
 * Contains abstract methods to create extended CRUD operations for Rating entity
 */
public interface RatingDao {
    /**
     * Find by answer id and user id.
     *
     * @param answerId the answer id
     * @param userId   the user id
     * @return the optional with found Rating object or empty one
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    Optional<Rating> findByAnswerIdAndUserId(Long answerId, Long userId) throws DaoException;

    /**
     * Sum grade by answer id.
     *
     * @param id the answer id
     * @return the sum of the answer grades in Integer
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    Integer sumGradeByAnswerId(Long id) throws DaoException;
}
