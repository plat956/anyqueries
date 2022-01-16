package by.latushko.anyqueries.model.dao.impl;

import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.model.dao.BaseDao;
import by.latushko.anyqueries.model.dao.RatingDao;
import by.latushko.anyqueries.model.entity.Rating;
import by.latushko.anyqueries.model.mapper.RowMapper;
import by.latushko.anyqueries.model.mapper.impl.RatingMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static by.latushko.anyqueries.model.mapper.TableColumnName.RATING_GRADE_SUM;

public class RatingDaoImpl extends BaseDao<Long, Rating> implements RatingDao {
    private static final String SQL_CREATE_QUERY = """
            INSERT INTO rating(grade, answer_id, user_id) 
            VALUES (?, ?, ?)""";
    private static final String SQL_UPDATE_QUERY = """
            UPDATE rating 
            SET grade = ?, answer_id = ?, user_id = ? 
            WHERE id = ?""";
    private static final String SQL_FIND_BY_ANSWER_ID_AND_USER_ID_QUERY = """
            SELECT id, grade, answer_id, user_id 
            FROM rating 
            WHERE answer_id = ?
            AND user_id = ?""";
    private static final String SQL_SUM_GRADE_BY_ANSWER_ID_QUERY = """
            SELECT sum(grade) as grade_sum 
            FROM rating 
            WHERE answer_id = ?""";

    private RowMapper mapper = new RatingMapper();

    @Override
    public Optional<Rating> findById(Long id) throws DaoException {
        throw new UnsupportedOperationException("Method findById(Long id) is unsupported");
    }

    @Override
    public boolean create(Rating rating) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_CREATE_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)){
            statement.setInt(1, rating.getGrade());
            statement.setLong(2, rating.getAnswerId());
            statement.setLong(3, rating.getUserId());

            if(statement.executeUpdate() >= 0) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if(resultSet.next()) {
                    Long generatedId = resultSet.getLong(1);
                    rating.setId(generatedId);
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to create rating by calling create(Rating rating) method", e);
        }
        return false;
    }

    @Override
    public Optional<Rating> update(Rating rating) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_QUERY)){
            statement.setInt(1, rating.getGrade());
            statement.setLong(2, rating.getAnswerId());
            statement.setLong(3, rating.getUserId());
            statement.setLong(4, rating.getId());

            if(statement.executeUpdate() >= 0) {
                return Optional.of(rating);
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to update rating by calling update(Rating rating) method", e);
        }
        return Optional.empty();
    }

    @Override
    public boolean delete(Long id) throws DaoException {
        throw new UnsupportedOperationException("Method delete(Long id) is unsupported");
    }

    @Override
    public Optional<Rating> findByAnswerIdAndUserId(Long answerId, Long userId) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ANSWER_ID_AND_USER_ID_QUERY)){
            statement.setLong(1, answerId);
            statement.setLong(2, userId);
            try(ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    return mapper.mapRow(resultSet);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find rating by calling findByAnswerIdAndUserId(Long answerId, Long userId) method", e);
        }
    }

    @Override
    public Integer sumGradeByAnswerId(Long id) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SUM_GRADE_BY_ANSWER_ID_QUERY)){
            statement.setLong(1, id);
            try(ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    return resultSet.getInt(RATING_GRADE_SUM);
                } else {
                    return 0;
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find sum rating by calling sumGradeByAnswerId(Long id) method", e);
        }
    }
}
