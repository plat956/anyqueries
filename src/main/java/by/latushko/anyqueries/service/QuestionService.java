package by.latushko.anyqueries.service;

import by.latushko.anyqueries.model.entity.Question;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.util.pagination.Paginated;
import by.latushko.anyqueries.util.pagination.RequestPage;
import jakarta.servlet.http.Part;

import java.util.List;
import java.util.Optional;

public interface QuestionService {
    List<String> findTitleByTitleContainsAndCategoryIdAndAuthorIdOrderByTitleAscLimitedTo(String pattern, Long categoryId,
                                             Long userId, int limit);
    Paginated<Question> findPaginatedByResolvedAndAuthorIdAndCategoryIdAndTitleContainsOrderByNewest(RequestPage page,
                                             boolean resolved, Long authorId, Long categoryId, String titlePattern, boolean newestFirst);
    Optional<Question> findById(Long id);
    Long countByAuthorId(Long authorId);
    Long countNotClosed();
    Long countNotClosedByAuthorId(Long authorId);
    Optional<Question> create(Long categoryId, String title, String text, User author, List<Part> attachments);
    boolean delete(Long id);
    boolean update(Long questionId, Long categoryId, String title, String text, List<Part> attachments);
    boolean changeStatus(Long id, boolean status);
    boolean checkEditAccess(Long id, Long userId);
    boolean checkChangeStatusAccess(Long id, Long userId);
    boolean checkDeleteAccess(Long id, User user);
}
