package by.latushko.anyqueries.service;

import by.latushko.anyqueries.model.entity.Question;
import by.latushko.anyqueries.model.entity.User;
import jakarta.servlet.http.Part;

import java.util.List;
import java.util.Optional;

public interface QuestionService {
    List<String> findTitleLikeOrderedAndLimited(String pattern, int limit);
    Long countTotalQuestionsByAuthorId(Long userId);
    Long countTotalNotClosedQuestions();
    Long countTotalNotClosedQuestionsByAuthorId(Long authorId);
    Optional<Question> create(Long categoryId, String title, String text, User author, List<Part> attachments);
}