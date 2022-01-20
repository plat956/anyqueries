package by.latushko.anyqueries.model.mapper.impl;

import by.latushko.anyqueries.model.entity.Category;
import by.latushko.anyqueries.model.mapper.RowMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static by.latushko.anyqueries.model.mapper.TableColumnName.*;

public class CategoryMapper implements RowMapper<Category> {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public Optional<Category> mapRow(ResultSet resultSet, String prefix) {
        try {
            Category category = new Category();
            category.setId(resultSet.getLong(prefix + CATEGORY_ID));
            category.setName(resultSet.getString(prefix + CATEGORY_NAME));
            category.setColor(resultSet.getString(prefix + CATEGORY_COLOR));
            if(hasColumn(resultSet, prefix + CATEGORY_QUESTIONS_COUNT)) {
                category.setQuestionsCount(resultSet.getLong(prefix + CATEGORY_QUESTIONS_COUNT));
            }
            if(hasColumn(resultSet, prefix + TOTAL)) {
                category.setTotal(resultSet.getLong(prefix + TOTAL));
            }
            return Optional.of(category);
        } catch (SQLException e) {
            logger.error("Failed to fetch category data from resultSet", e);
            return Optional.empty();
        }
    }
}
