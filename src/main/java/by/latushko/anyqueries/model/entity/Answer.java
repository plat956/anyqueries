package by.latushko.anyqueries.model.entity;

import java.time.LocalDateTime;
import java.util.Set;

public class Answer extends BaseEntity<Long> {
    private String text;
    private LocalDateTime creationDate;
    private LocalDateTime editingDate;
    private Boolean solution;
    private Question question;
    private User author;
    private Answer parent;
    private Set<Attachment> attachments;

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

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Answer getParent() {
        return parent;
    }

    public void setParent(Answer parent) {
        this.parent = parent;
    }

    public Set<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(Set<Attachment> attachments) {
        this.attachments = attachments;
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
        if (question != null ? !question.equals(answer.question) : answer.question != null) return false;
        if (author != null ? !author.equals(answer.author) : answer.author != null) return false;
        if (parent != null ? !parent.equals(answer.parent) : answer.parent != null) return false;
        return attachments != null ? attachments.equals(answer.attachments) : answer.attachments == null;
    }

    @Override
    public int hashCode() {
        int result = text != null ? text.hashCode() : 0;
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (editingDate != null ? editingDate.hashCode() : 0);
        result = 31 * result + (solution != null ? solution.hashCode() : 0);
        result = 31 * result + (question != null ? question.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
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
        sb.append(", question=").append(question);
        sb.append(", author=").append(author);
        sb.append(", parent=").append(parent);
        sb.append(", attachments=").append(attachments);
        sb.append('}');
        return sb.toString();
    }
}
