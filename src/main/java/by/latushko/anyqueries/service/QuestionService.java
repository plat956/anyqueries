package by.latushko.anyqueries.service;

import by.latushko.anyqueries.model.entity.Question;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.util.pagination.Paginated;
import by.latushko.anyqueries.util.pagination.RequestPage;
import jakarta.servlet.http.Part;

import java.util.List;
import java.util.Optional;

/**
 * The Question service interface.
 */
public interface QuestionService {
    /**
     * Find title by title contains and category id and author id order by title asc limited to.
     *
     * @param pattern    the title pattern
     * @param categoryId the category id
     * @param userId     the user id
     * @param limit      the limit
     * @return the list of question titles
     */
    List<String> findTitleByTitleContainsAndCategoryIdAndAuthorIdOrderByTitleAscLimitedTo(String pattern, Long categoryId, Long userId, int limit);

    /**
     * Find paginated by resolved and author id and category id and title contains order by newest.
     *
     * @param page         the page
     * @param resolved     the resolved status
     * @param authorId     the author id
     * @param categoryId   the category id
     * @param titlePattern the question title pattern
     * @param newestFirst  the newest first flag
     * @return the paginated question objects
     */
    Paginated<Question> findPaginatedByResolvedAndAuthorIdAndCategoryIdAndTitleContainsOrderByNewest(RequestPage page,
                                             boolean resolved, Long authorId, Long categoryId, String titlePattern, boolean newestFirst);

    /**
     * Find by id.
     *
     * @param id the question id
     * @return the optional with found question or empty one
     */
    Optional<Question> findById(Long id);

    /**
     * Count by author id.
     *
     * @param authorId the author id
     * @return the count of found questions
     */
    Long countByAuthorId(Long authorId);

    /**
     * Count not closed.
     *
     * @return the count of found questions
     */
    Long countNotClosed();

    /**
     * Count not closed by author id.
     *
     * @param authorId the author id
     * @return the count of found questions
     */
    Long countNotClosedByAuthorId(Long authorId);

    /**
     * Create question.
     *
     * @param categoryId  the category id
     * @param title       the title
     * @param text        the text
     * @param author      the author
     * @param attachments the attachments
     * @return the optional with a created question or empty one
     */
    Optional<Question> create(Long categoryId, String title, String text, User author, List<Part> attachments);

    /**
     * Delete question.
     *
     * @param id the question id
     * @return the boolean, true if the question was deleted successfully, otherwise false
     */
    boolean delete(Long id);

    /**
     * Update question.
     *
     * @param questionId  the question id
     * @param categoryId  the category id
     * @param title       the title
     * @param text        the text
     * @param attachments the attachments
     * @return the boolean, true if the question was updated successfully, otherwise false
     */
    boolean update(Long questionId, Long categoryId, String title, String text, List<Part> attachments);

    /**
     * Change question status.
     *
     * @param id     the question id
     * @param status the status
     * @return the boolean, true if the question status was changed successfully, otherwise false
     */
    boolean changeStatus(Long id, boolean status);

    /**
     * Check question edit access.
     *
     * @param id     the question id
     * @param userId the user id
     * @return the boolean, true if the edit question access for user is allowed, otherwise false
     */
    boolean checkEditAccess(Long id, Long userId);

    /**
     * Check question change status access.
     *
     * @param id     the question id
     * @param userId the user id
     * @return the boolean, true if the change question status access for user is allowed, otherwise false
     */
    boolean checkChangeStatusAccess(Long id, Long userId);

    /**
     * Check delete access.
     *
     * @param id   the question id
     * @param user the user
     * @return the boolean, true if the delete question access for user is allowed, otherwise false
     */
    boolean checkDeleteAccess(Long id, User user);
}
