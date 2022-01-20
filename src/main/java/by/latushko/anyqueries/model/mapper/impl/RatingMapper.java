package by.latushko.anyqueries.model.mapper.impl;

import by.latushko.anyqueries.model.entity.Rating;
import by.latushko.anyqueries.model.mapper.RowMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static by.latushko.anyqueries.model.mapper.TableColumnName.*;

public class RatingMapper implements RowMapper<Rating> {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public Optional<Rating> mapRow(ResultSet resultSet, String prefix) {
        try {
            Rating rating = new Rating();
            rating.setId(resultSet.getLong(prefix + RATING_ID));
            rating.setGrade(resultSet.getInt(prefix + RATING_GRADE));
            rating.setAnswerId(resultSet.getLong(prefix + RATING_ANSWER_ID));
            rating.setUserId(resultSet.getLong(prefix + RATING_USER_ID));
            return Optional.of(rating);
        } catch (SQLException e) {
            logger.error("Failed to fetch rating data from resultSet", e);
            return Optional.empty();
        }
    }
}
