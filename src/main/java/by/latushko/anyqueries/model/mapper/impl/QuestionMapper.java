package by.latushko.anyqueries.model.mapper.impl;

import by.latushko.anyqueries.model.entity.Category;
import by.latushko.anyqueries.model.entity.Question;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.model.mapper.RowMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

import static by.latushko.anyqueries.model.mapper.TableColumnName.*;

public class QuestionMapper implements RowMapper<Question> {
    private static final Logger logger = LogManager.getLogger();

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
            if(hasColumn(resultSet, prefix + QUESTION_SOLVED)) {
                question.setSolved(resultSet.getBoolean(prefix + QUESTION_SOLVED));
            }
            CategoryMapper categoryMapper = new CategoryMapper();
            Optional<Category> category = categoryMapper.mapRow(resultSet, CATEGORY_PREFIX);
            category.ifPresent(question::setCategory);
            UserMapper userMapper = new UserMapper();
            Optional<User> author = userMapper.mapRow(resultSet, USER_PREFIX);
            author.ifPresent(question::setAuthor);
            return Optional.of(question);
        } catch (SQLException e) {
            logger.error("Failed to fetch question data from resultSet", e);
            return Optional.empty();
        }
    }
}
