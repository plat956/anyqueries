package by.latushko.anyqueries.model.mapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@FunctionalInterface
public interface RowMapper<E> {
    Optional<E> mapRow(ResultSet resultSet, String fieldPrefix);

    default Optional<E> mapRow(ResultSet resultSet) {
        return mapRow(resultSet, "");
    }

    default List<E> mapRows(ResultSet resultSet, String fieldPrefix) throws SQLException {
        List<E> entities = new ArrayList<>();
        while(resultSet.next()) {
            Optional<E> entity = mapRow(resultSet, fieldPrefix);
            if(entity.isPresent()) {
                entities.add(entity.get());
            }
        }
        return entities;
    }

    default List<E> mapRows(ResultSet resultSet) throws SQLException {
        return mapRows(resultSet, "");
    }

    default boolean hasColumn(ResultSet resultSet, String columnName) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columns = metaData.getColumnCount();
        for (int i = 1; i <= columns; i++) {
            if (columnName.equals(metaData.getColumnName(i))) {
                return true;
            }
        }
        return false;
    }
}