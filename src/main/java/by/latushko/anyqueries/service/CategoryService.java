package by.latushko.anyqueries.service;

import by.latushko.anyqueries.model.entity.Category;
import by.latushko.anyqueries.util.pagination.Paginated;
import by.latushko.anyqueries.util.pagination.RequestPage;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> findTop5();
    List<Category> findAllOrderByNameAsc();
    Optional<String> findNameById(Long id);
    Paginated<Category> findAllPaginatedByNameLikeOrderByNameAsc(RequestPage page, String namePattern);
    boolean checkIfExistsByName(String name);
    boolean checkIfExistsByNameAndIdNot(String name, Long id);
    Optional<Category> create(String name, String color);
    Optional<Category> findById(Long id);
    boolean update(Long id, String name, String color);
    boolean delete(Long id);
    List<String> findNameByNameContainsOrderByNameAscLimitedTo(String namePattern, int limit);
}
