package by.latushko.anyqueries.service;

import by.latushko.anyqueries.model.entity.Answer;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.util.pagination.Paginated;
import by.latushko.anyqueries.util.pagination.RequestPage;
import jakarta.servlet.http.Part;

import java.util.List;
import java.util.Optional;

/**
 * The Answer service interface.
 */
public interface AnswerService {
    /**
     * Find paginated by question id order by creation date asc.
     *
     * @param page       the page
     * @param questionId the question id
     * @param userId     the user id
     * @return the paginated answers
     */
    Paginated<Answer> findPaginatedByQuestionIdOrderByCreationDateAsc(RequestPage page, Long questionId, Long userId);

    /**
     * Create answer.
     *
     * @param question    the question id
     * @param text        the text
     * @param user        the author user object
     * @param attachments the attachments
     * @return the optional with answer or empty one
     */
    Optional<Answer> create(Long question, String text, User user, List<Part> attachments);

    /**
     * Update answer.
     *
     * @param id          the answer id
     * @param text        the text
     * @param attachments the attachments
     * @return the optional with answer or empty one
     */
    Optional<Answer> update(Long id, String text, List<Part> attachments);

    /**
     * Delete answer by id.
     *
     * @param id the answer id
     * @return the boolean, true if the answer was deleted successfully, otherwise false
     */
    boolean delete(Long id);

    /**
     * Count by user id.
     *
     * @param userId the user id
     * @return the long, count users
     */
    Long countByUserId(Long userId);

    /**
     * Change answer rating.
     *
     * @param answerId the answer id
     * @param grade    the grade
     * @param userId   the user id
     * @return the boolean, true if the answer rating was changed successfully, otherwise false
     */
    boolean changeRating(Long answerId, Boolean grade, Long userId);

    /**
     * Calculate rating by answer id.
     *
     * @param answerId the answer id
     * @return the answer rating
     */
    Integer calculateRatingByAnswerId(Long answerId);

    /**
     * Calculate the last page by question id.
     *
     * @param id the question id
     * @return the last page
     */
    Integer calculateLastPageByQuestionId(Long id);

    /**
     * Sets question solution status.
     *
     * @param answerId the answer id
     * @param solution the solution status
     * @param userId   the user id
     * @return the boolean, true if the answer solution status was changed successfully, otherwise false
     */
    boolean setSolution(Long answerId, boolean solution, Long userId);

    /**
     * Check answer edit access.
     *
     * @param id     the answer id
     * @param userId the user id
     * @return the boolean, true if the answer edit access for user is allowed, otherwise false
     */
    boolean checkEditAccess(Long id, Long userId);

    /**
     * Check answer delete access.
     *
     * @param id   the answer id
     * @param user the user
     * @return the boolean, true if the answer delete access for user is allowed, otherwise false
     */
    boolean checkDeleteAccess(Long id, User user);
}
