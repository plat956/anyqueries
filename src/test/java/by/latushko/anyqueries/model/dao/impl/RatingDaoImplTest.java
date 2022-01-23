package by.latushko.anyqueries.model.dao.impl;

import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.model.dao.RatingDao;
import by.latushko.anyqueries.model.entity.Rating;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

public class RatingDaoImplTest {

    @Mock
    private RatingDao ratingDao;
    private List<Rating> ratings;

    @BeforeClass
    public void setUp() throws DaoException {
        MockitoAnnotations.openMocks(this);
        ratings = new ArrayList<>();

        Rating rating = new Rating();
        rating.setId(1L);
        rating.setGrade(1);
        rating.setUserId(15L);
        rating.setAnswerId(436L);
        ratings.add(rating);

        rating = new Rating();
        rating.setId(2L);
        rating.setGrade(-1);
        rating.setUserId(33L);
        rating.setAnswerId(12L);
        ratings.add(rating);

        rating = new Rating();
        rating.setId(3L);
        rating.setGrade(1);
        rating.setUserId(64L);
        rating.setAnswerId(436L);
        ratings.add(rating);

        when(ratingDao.findByAnswerIdAndUserId(anyLong(), anyLong())).
                thenAnswer(invocation -> ratings.stream().
                        filter(r -> r.getAnswerId().equals(invocation.getArgument(0)) &&
                                r.getUserId().equals(invocation.getArgument(1))).
                        findAny());

        when(ratingDao.sumGradeByAnswerId(anyLong()))
                .thenAnswer(invocation -> ratings.stream().
                        filter(r -> r.getAnswerId().equals(invocation.getArgument(0))).
                        mapToInt(Rating::getGrade).sum());
    }

    @Test
    public void testFindByAnswerIdAndUserId() throws DaoException {
        Optional<Rating> ratingOptional = ratingDao.findByAnswerIdAndUserId(12L, 33L);
        boolean actual = ratingOptional.isPresent();
        assertTrue(actual);
    }

    @Test
    public void testFindByAnswerIdAndUserIdEmpty() throws DaoException {
        Optional<Rating> ratingOptional = ratingDao.findByAnswerIdAndUserId(1L, 38L);
        boolean actual = ratingOptional.isEmpty();
        assertTrue(actual);
    }

    @Test
    public void testSumGradeByAnswerId() throws DaoException {
        Integer actual = ratingDao.sumGradeByAnswerId(436L);
        Integer expected = 2;
        assertEquals(actual, expected);
    }

    @Test
    public void testSumGradeByAnswerIdFalse() throws DaoException {
        Integer actual = ratingDao.sumGradeByAnswerId(12L);
        Integer expected = -99;
        assertNotEquals(actual, expected);
    }
}