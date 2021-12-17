package by.latushko.anyqueries.entity;

public class Attachment extends BaseEntity<Long> {
    private String file;

    public Attachment() {
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Attachment that = (Attachment) o;

        return file != null ? file.equals(that.file) : that.file == null;
    }

    @Override
    public int hashCode() {
        return file != null ? file.hashCode() : 0;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Attachment{");
        sb.append("file='").append(file).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
