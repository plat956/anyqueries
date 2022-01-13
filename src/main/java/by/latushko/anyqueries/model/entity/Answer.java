package by.latushko.anyqueries.model.entity;

import java.time.LocalDateTime;
import java.util.List;

public class Answer extends BaseEntity<Long> {
    private String text;
    private LocalDateTime creationDate;
    private LocalDateTime editingDate;
    private Boolean solution;
    private Long questionId;
    private User author;
    private transient Integer rating;
    private transient Integer currentUserGrade;
    private transient List<Attachment> attachments;

    public Answer() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getEditingDate() {
        return editingDate;
    }

    public void setEditingDate(LocalDateTime editingDate) {
        this.editingDate = editingDate;
    }

    public Boolean getSolution() {
        return solution;
    }

    public void setSolution(Boolean solution) {
        this.solution = solution;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getCurrentUserGrade() {
        return currentUserGrade;
    }

    public void setCurrentUserGrade(Integer currentUserGrade) {
        this.currentUserGrade = currentUserGrade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Answer answer = (Answer) o;

        if (text != null ? !text.equals(answer.text) : answer.text != null) return false;
        if (creationDate != null ? !creationDate.equals(answer.creationDate) : answer.creationDate != null)
            return false;
        if (editingDate != null ? !editingDate.equals(answer.editingDate) : answer.editingDate != null) return false;
        if (solution != null ? !solution.equals(answer.solution) : answer.solution != null) return false;
        if (questionId != null ? !questionId.equals(answer.questionId) : answer.questionId != null) return false;
        if (author != null ? !author.equals(answer.author) : answer.author != null) return false;
        if (rating != null ? !rating.equals(answer.rating) : answer.rating != null) return false;
        if (currentUserGrade != null ? !currentUserGrade.equals(answer.currentUserGrade) : answer.currentUserGrade != null)
            return false;
        return attachments != null ? attachments.equals(answer.attachments) : answer.attachments == null;
    }

    @Override
    public int hashCode() {
        int result = text != null ? text.hashCode() : 0;
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (editingDate != null ? editingDate.hashCode() : 0);
        result = 31 * result + (solution != null ? solution.hashCode() : 0);
        result = 31 * result + (questionId != null ? questionId.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (rating != null ? rating.hashCode() : 0);
        result = 31 * result + (currentUserGrade != null ? currentUserGrade.hashCode() : 0);
        result = 31 * result + (attachments != null ? attachments.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Answer{");
        sb.append("text='").append(text).append('\'');
        sb.append(", creationDate=").append(creationDate);
        sb.append(", editingDate=").append(editingDate);
        sb.append(", solution=").append(solution);
        sb.append(", questionId=").append(questionId);
        sb.append(", author=").append(author);
        sb.append(", rating=").append(rating);
        sb.append(", currentUserGrade=").append(currentUserGrade);
        sb.append(", attachments=").append(attachments);
        sb.append('}');
        return sb.toString();
    }
}
