package by.latushko.anyqueries.model.mapper.impl;

import by.latushko.anyqueries.model.entity.Category;
import by.latushko.anyqueries.model.mapper.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static by.latushko.anyqueries.model.mapper.TableColumnName.*;

public class CategoryMapper implements RowMapper<Category> {
    @Override
    public Optional<Category> mapRow(ResultSet resultSet) {
        try {
            Category category = new Category();
            category.setId(resultSet.getLong(CATEGORY_ID));
            category.setName(resultSet.getString(CATEGORY_NAME));
            category.setColor(resultSet.getString(CATEGORY_COLOR));
            if(hasColumn(resultSet, CATEGORY_QUESTIONS_COUNT)) {
                category.setQuestionsCount(resultSet.getLong(CATEGORY_QUESTIONS_COUNT));
            }
            return Optional.of(category);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }
}
