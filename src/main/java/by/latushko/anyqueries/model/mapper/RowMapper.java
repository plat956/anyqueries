package by.latushko.anyqueries.model.mapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The Row mapper interface.
 * Defines different opportunities of entity row mappers
 *
 * @param <E> the entity class parameter
 */
@FunctionalInterface
public interface RowMapper<E> {
    /**
     * Map single row with column prefix.
     *
     * @param resultSet the result set
     * @param prefix    the prefix
     * @return the optional with mapped entity object or empty one
     */
    Optional<E> mapRow(ResultSet resultSet, String prefix);

    /**
     * Map single row.
     *
     * @param resultSet the result set
     * @return the optional with mapped entity object or empty one
     */
    default Optional<E> mapRow(ResultSet resultSet) {
        return mapRow(resultSet, "");
    }

    /**
     * Map rows list.
     *
     * @param resultSet the result set
     * @return the list of mapped entity objects
     * @throws SQLException if the sql exception was thrown during the rows mapping
     */
    default List<E> mapRows(ResultSet resultSet) throws SQLException {
        List<E> entities = new ArrayList<>();
        while(resultSet.next()) {
            Optional<E> entity = mapRow(resultSet);
            entity.ifPresent(entities::add);
        }
        return entities;
    }

    /**
     * Checks if the result set has a column.
     *
     * @param resultSet  the result set
     * @param columnName the column name
     * @return the boolean, true if the column exists in the result set, otherwise false
     * @throws SQLException if the sql exception was thrown during the column existing check
     */
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