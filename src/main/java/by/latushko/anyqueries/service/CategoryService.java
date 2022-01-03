package by.latushko.anyqueries.service;

import by.latushko.anyqueries.model.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findTop5Categories();
    List<Category> findAllOrderByNameAsc();
}
