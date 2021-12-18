package by.latushko.anyqueries.model.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@FunctionalInterface
public interface RowMapper<E> {
    Optional<E> mapRow(ResultSet resultSet);

    default List<E> mapRows(ResultSet resultSet) throws SQLException {
        List<E> entities = new ArrayList<>();
        while(resultSet.next()) {
            Optional<E> entity = mapRow(resultSet);
            if(entity.isPresent()) {
                entities.add(entity.get());
            }
        }
        return entities;
    }
}