package by.latushko.anyqueries.service;

import by.latushko.anyqueries.model.entity.Question;
import by.latushko.anyqueries.model.entity.User;
import jakarta.servlet.http.Part;

import java.util.List;
import java.util.Optional;

public interface QuestionService {
    List<String> findTitleLikeOrderedAndLimited(String pattern, int limit);
    Long countTotalByAuthorId(Long userId);
    Long countTotalNotClosed();
    Long countTotalNotClosedByAuthorId(Long authorId);
    Optional<Question> create(Long categoryId, String title, String text, User author, List<Part> attachments);
}
