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
    Paginated<Category> findAllPaginatedOrderByNameAsc(RequestPage page);
}
