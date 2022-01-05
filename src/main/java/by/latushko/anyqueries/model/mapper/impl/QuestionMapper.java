package by.latushko.anyqueries.model.mapper.impl;

import by.latushko.anyqueries.model.entity.Category;
import by.latushko.anyqueries.model.entity.Question;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.model.mapper.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

import static by.latushko.anyqueries.model.mapper.TableColumnName.*;

public class QuestionMapper implements RowMapper<Question> {
    @Override
    public Optional<Question> mapRow(ResultSet resultSet, String fieldPrefix) {
        try {
            Question question = new Question();
            question.setId(resultSet.getLong(fieldPrefix + QUESTION_ID));
            question.setTitle(resultSet.getString(fieldPrefix + QUESTION_TITLE));
            question.setText(resultSet.getString(fieldPrefix + QUESTION_TEXT));
            question.setCreationDate(resultSet.getObject(fieldPrefix + QUESTION_CREATION_DATE, LocalDateTime.class));
            question.setEditingDate(resultSet.getObject(fieldPrefix + QUESTION_EDITING_DATE, LocalDateTime.class));
            question.setClosed(resultSet.getBoolean(fieldPrefix + QUESTION_CLOSED));
            if(hasColumn(resultSet, fieldPrefix + QUESTION_ANSWERS_COUNT)) {
                question.setAnswersCount(resultSet.getLong(fieldPrefix + QUESTION_ANSWERS_COUNT));
            }
            if(hasColumn(resultSet, fieldPrefix + TOTAL)) {
                question.setTotal(resultSet.getLong(fieldPrefix + TOTAL));
            }
            CategoryMapper categoryMapper = new CategoryMapper();
            Optional<Category> category = categoryMapper.mapRow(resultSet, CATEGORY_PREFIX);
            if(category.isPresent()) {
                question.setCategory(category.get());
            }
            UserMapper userMapper = new UserMapper();
            Optional<User> author = userMapper.mapRow(resultSet, USER_PREFIX);
            if(author.isPresent()) {
                question.setAuthor(author.get());
            }
            return Optional.of(question);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }
}
