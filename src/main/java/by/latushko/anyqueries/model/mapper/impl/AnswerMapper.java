package by.latushko.anyqueries.model.mapper.impl;

import by.latushko.anyqueries.model.entity.Answer;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.model.mapper.RowMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

import static by.latushko.anyqueries.model.mapper.TableColumnName.*;

public class AnswerMapper implements RowMapper<Answer> {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public Optional<Answer> mapRow(ResultSet resultSet, String prefix) {
        try {
            Answer answer = new Answer();
            answer.setId(resultSet.getLong(prefix + ANSWER_ID));
            answer.setText(resultSet.getString(prefix + ANSWER_TEXT));
            answer.setCreationDate(resultSet.getObject(prefix + ANSWER_CREATION_DATE, LocalDateTime.class));
            answer.setEditingDate(resultSet.getObject(prefix + ANSWER_EDITING_DATE, LocalDateTime.class));
            answer.setSolution(resultSet.getBoolean(prefix + ANSWER_SOLUTION));
            answer.setQuestionId(resultSet.getLong(prefix + ANSWER_QUESTION_ID));
            if(hasColumn(resultSet, prefix + ANSWER_CURRENT_USER_GRADE)) {
                answer.setCurrentUserGrade(resultSet.getInt(prefix + ANSWER_CURRENT_USER_GRADE));
            }
            if(hasColumn(resultSet, prefix + ANSWER_RATING)) {
                answer.setRating(resultSet.getInt(prefix + ANSWER_RATING));
            }
            if(hasColumn(resultSet, prefix + TOTAL)) {
                answer.setTotal(resultSet.getLong(prefix + TOTAL));
            }
            UserMapper userMapper = new UserMapper();
            Optional<User> author = userMapper.mapRow(resultSet, USER_PREFIX);
            author.ifPresent(answer::setAuthor);
            return Optional.of(answer);
        } catch (SQLException e) {
            logger.error("Failed to fetch answer data from resultSet", e);
            return Optional.empty();
        }
    }
}
