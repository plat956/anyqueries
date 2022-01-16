package by.latushko.anyqueries.model.entity;

import java.time.LocalDateTime;

public class Question extends BaseEntity<Long> {
    private String title;
    private String text;
    private LocalDateTime creationDate;
    private LocalDateTime editingDate;
    private Boolean closed;
    private Category category;
    private User author;
    private transient long answersCount;
    private transient Boolean solved;

    public Question() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Boolean getClosed() {
        return closed;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public long getAnswersCount() {
        return answersCount;
    }

    public void setAnswersCount(long answersCount) {
        this.answersCount = answersCount;
    }

    public Boolean getSolved() {
        return solved;
    }

    public void setSolved(Boolean solved) {
        this.solved = solved;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Question question = (Question) o;

        if (title != null ? !title.equals(question.title) : question.title != null) return false;
        if (text != null ? !text.equals(question.text) : question.text != null) return false;
        if (creationDate != null ? !creationDate.equals(question.creationDate) : question.creationDate != null)
            return false;
        if (editingDate != null ? !editingDate.equals(question.editingDate) : question.editingDate != null)
            return false;
        if (closed != null ? !closed.equals(question.closed) : question.closed != null) return false;
        if (category != null ? !category.equals(question.category) : question.category != null) return false;
        return author != null ? author.equals(question.author) : question.author == null;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (editingDate != null ? editingDate.hashCode() : 0);
        result = 31 * result + (closed != null ? closed.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Question{");
        sb.append("title='").append(title).append('\'');
        sb.append(", text='").append(text).append('\'');
        sb.append(", creationDate=").append(creationDate);
        sb.append(", editingDate=").append(editingDate);
        sb.append(", closed=").append(closed);
        sb.append(", category=").append(category);
        sb.append(", author=").append(author);
        sb.append('}');
        return sb.toString();
    }
}
