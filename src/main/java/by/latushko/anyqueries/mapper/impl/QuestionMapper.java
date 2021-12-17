package by.latushko.anyqueries.mapper.impl;

import by.latushko.anyqueries.entity.Question;
import by.latushko.anyqueries.mapper.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class QuestionMapper implements RowMapper<Question> {
    @Override
    public Optional<Question> mapRow(ResultSet resultSet) {
        try {
            Question question = new Question();
            question.setId(resultSet.getLong("id"));
            question.setTitle(resultSet.getString("title"));
            question.setText(resultSet.getString("text"));

            //todo Как здесь заполнять категорию вопроса?

            //todo: set the rest of fields
            return Optional.of(question);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }
}
