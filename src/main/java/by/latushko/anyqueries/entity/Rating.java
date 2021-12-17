package by.latushko.anyqueries.entity;

public class Rating extends BaseEntity<Long> {
    private Boolean grade;
    private User user;
    private Answer answer;

    public Rating() {
    }

    public Boolean getGrade() {
        return grade;
    }

    public void setGrade(Boolean grade) {
        this.grade = grade;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rating rating = (Rating) o;

        if (grade != null ? !grade.equals(rating.grade) : rating.grade != null) return false;
        if (user != null ? !user.equals(rating.user) : rating.user != null) return false;
        return answer != null ? answer.equals(rating.answer) : rating.answer == null;
    }

    @Override
    public int hashCode() {
        int result = grade != null ? grade.hashCode() : 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (answer != null ? answer.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Rating{");
        sb.append("grade=").append(grade);
        sb.append(", user=").append(user);
        sb.append(", answer=").append(answer);
        sb.append('}');
        return sb.toString();
    }
}
