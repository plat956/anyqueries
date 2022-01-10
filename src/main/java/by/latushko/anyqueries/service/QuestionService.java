package by.latushko.anyqueries.service;

import by.latushko.anyqueries.model.entity.Question;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.util.pagination.Paginated;
import by.latushko.anyqueries.util.pagination.RequestPage;
import jakarta.servlet.http.Part;

import java.util.List;
import java.util.Optional;

public interface QuestionService {
    Integer QUESTION_SEARCH_QUERY_MAX_LENGTH = 40;
    List<String> findTitleByTitleLikeAndCategoryIdAndAuthorIdOrderedAndLimited(String pattern, Long categoryId, Long userId, int limit);
    Long countTotalByAuthorId(Long userId);
    Long countTotalNotClosed();
    Long countTotalNotClosedByAuthorId(Long authorId);
    boolean checkManagementAccess(Long questionId, User user);
    Optional<Question> create(Long categoryId, String title, String text, User author, List<Part> attachments);
    Paginated<Question> findByQueryParametersOrderByNewest(RequestPage page, boolean resolved, boolean newestFirst, Long authorId, Long categoryId, String titlePattern);
    boolean delete(Long id, User initiator);
}
