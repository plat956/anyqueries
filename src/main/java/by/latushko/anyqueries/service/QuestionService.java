package by.latushko.anyqueries.service;

import java.util.List;

public interface QuestionService {
    List<String> findTitleLikeOrderedAndLimited(String pattern, int limit);
    Long countTotalQuestionsByAuthorId(Long userId);
    Long countTotalNotClosedQuestions();
    Long countTotalNotClosedQuestionsByAuthorId(Long authorId);
}
