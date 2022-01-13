package by.latushko.anyqueries.model.entity;

public class Rating extends BaseEntity<Long> {
    private Integer grade;
    private Long userId;
    private Long answerId;

    public Rating() {
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rating rating = (Rating) o;

        if (grade != null ? !grade.equals(rating.grade) : rating.grade != null) return false;
        if (userId != null ? !userId.equals(rating.userId) : rating.userId != null) return false;
        return answerId != null ? answerId.equals(rating.answerId) : rating.answerId == null;
    }

    @Override
    public int hashCode() {
        int result = grade != null ? grade.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (answerId != null ? answerId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Rating{");
        sb.append("grade=").append(grade);
        sb.append(", userId=").append(userId);
        sb.append(", answerId=").append(answerId);
        sb.append('}');
        return sb.toString();
    }
}
