package by.latushko.anyqueries.service;

import by.latushko.anyqueries.model.entity.Answer;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.util.pagination.Paginated;
import by.latushko.anyqueries.util.pagination.RequestPage;
import jakarta.servlet.http.Part;

import java.util.List;
import java.util.Optional;

public interface AnswerService {
    Long countByUserId(Long userId);
    Paginated<Answer> findPaginatedByQuestionIdOrderByCreationDateAsc(RequestPage page, Long questionId, Long userId);
    boolean changeRating(Long answerId, Boolean grade, Long userId);
    Integer calculateRatingByAnswerId(Long answerId);
    boolean setSolution(Long answerId, boolean solution, Long userId);
    Optional<Answer> create(Long question, String text, User user, List<Part> attachments);
    Integer calculateLastPageByQuestionId(Long id);
    boolean delete(Long id);
    Optional<Answer> update(Long id, String text, List<Part> attachments);
}
