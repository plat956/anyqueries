package by.latushko.anyqueries.model.mapper.impl;

import by.latushko.anyqueries.model.entity.Answer;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.model.mapper.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

import static by.latushko.anyqueries.model.mapper.TableColumnName.*;

public class AnswerMapper implements RowMapper<Answer> {
    @Override
    public Optional<Answer> mapRow(ResultSet resultSet, String fieldPrefix) {
        try {
            Answer answer = new Answer();
            answer.setId(resultSet.getLong(fieldPrefix + ANSWER_ID));
            answer.setText(resultSet.getString(fieldPrefix + ANSWER_TEXT));
            answer.setCreationDate(resultSet.getObject(fieldPrefix + ANSWER_CREATION_DATE, LocalDateTime.class));
            answer.setEditingDate(resultSet.getObject(fieldPrefix + ANSWER_EDITING_DATE, LocalDateTime.class));
            answer.setSolution(resultSet.getBoolean(fieldPrefix + ANSWER_SOLUTION));
            answer.setQuestionId(resultSet.getLong(fieldPrefix + ANSWER_QUESTION_ID));
            if(hasColumn(resultSet, fieldPrefix + ANSWER_CURRENT_USER_GRADE)) {
                answer.setCurrentUserGrade(resultSet.getInt(fieldPrefix + ANSWER_CURRENT_USER_GRADE));
            }
            if(hasColumn(resultSet, fieldPrefix + ANSWER_RATING)) {
                answer.setRating(resultSet.getInt(fieldPrefix + ANSWER_RATING));
            }
            if(hasColumn(resultSet, fieldPrefix + TOTAL)) {
                answer.setTotal(resultSet.getLong(fieldPrefix + TOTAL));
            }
            UserMapper userMapper = new UserMapper();
            Optional<User> author = userMapper.mapRow(resultSet, USER_PREFIX);
            if(author.isPresent()) {
                answer.setAuthor(author.get());
            }
            return Optional.of(answer);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }
}
