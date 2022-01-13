package by.latushko.anyqueries.service;

import by.latushko.anyqueries.model.entity.Answer;
import by.latushko.anyqueries.util.pagination.Paginated;
import by.latushko.anyqueries.util.pagination.RequestPage;

public interface AnswerService {
    Long countTotalAnswersByUserId(Long userId);
    Paginated<Answer> findPaginatedByQuestionIdOrderByCreationDateAsc(RequestPage page, Long questionId, Long userId);
    boolean changeRating(Long answerId, Boolean grade, Long userId);
    Integer calculateRatingByAnswerId(Long answerId);
}
