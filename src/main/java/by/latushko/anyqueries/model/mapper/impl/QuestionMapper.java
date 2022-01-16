package by.latushko.anyqueries.model.mapper.impl;

import by.latushko.anyqueries.model.entity.Category;
import by.latushko.anyqueries.model.entity.Question;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.model.mapper.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

import static by.latushko.anyqueries.model.mapper.TableColumnName.*;

public class QuestionMapper implements RowMapper<Question> {
    @Override
    public Optional<Question> mapRow(ResultSet resultSet, String prefix) {
        try {
            Question question = new Question();
            question.setId(resultSet.getLong(prefix + QUESTION_ID));
            question.setTitle(resultSet.getString(prefix + QUESTION_TITLE));
            question.setText(resultSet.getString(prefix + QUESTION_TEXT));
            question.setCreationDate(resultSet.getObject(prefix + QUESTION_CREATION_DATE, LocalDateTime.class));
            question.setEditingDate(resultSet.getObject(prefix + QUESTION_EDITING_DATE, LocalDateTime.class));
            question.setClosed(resultSet.getBoolean(prefix + QUESTION_CLOSED));
            if(hasColumn(resultSet, prefix + QUESTION_ANSWERS_COUNT)) {
                question.setAnswersCount(resultSet.getLong(prefix + QUESTION_ANSWERS_COUNT));
            }
            if(hasColumn(resultSet, prefix + TOTAL)) {
                question.setTotal(resultSet.getLong(prefix + TOTAL));
            }
            String q = "";
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columns = metaData.getColumnCount();
            for (int i = 1; i <= columns; i++) {
                q += metaData.getColumnName(i) + ", ";
            }

            if(hasColumn(resultSet, prefix + QUESTION_SOLVED)) {
                question.setSolved(resultSet.getBoolean(prefix + QUESTION_SOLVED));
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
