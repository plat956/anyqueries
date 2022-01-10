package by.latushko.anyqueries.model.mapper.impl;

import by.latushko.anyqueries.model.entity.Category;
import by.latushko.anyqueries.model.mapper.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static by.latushko.anyqueries.model.mapper.TableColumnName.*;

public class CategoryMapper implements RowMapper<Category> {
    @Override
    public Optional<Category> mapRow(ResultSet resultSet, String fieldPrefix) {
        try {
            Category category = new Category();
            category.setId(resultSet.getLong(fieldPrefix+ CATEGORY_ID));
            category.setName(resultSet.getString(fieldPrefix + CATEGORY_NAME));
            category.setColor(resultSet.getString(fieldPrefix + CATEGORY_COLOR));
            if(hasColumn(resultSet, fieldPrefix + CATEGORY_QUESTIONS_COUNT)) {
                category.setQuestionsCount(resultSet.getLong(fieldPrefix + CATEGORY_QUESTIONS_COUNT));
            }
            if(hasColumn(resultSet, fieldPrefix + TOTAL)) {
                category.setTotal(resultSet.getLong(fieldPrefix + TOTAL));
            }
            return Optional.of(category);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }
}
