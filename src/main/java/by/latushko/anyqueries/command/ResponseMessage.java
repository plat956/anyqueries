package by.latushko.anyqueries.command;

public class ResponseMessage {
    public enum Type {
        SUCCESS("success"),
        DANGER("danger"),
        INFO("info"),
        WARNING("warning");

        private String mode;

        Type(String mode) {
            this.mode = mode;
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }
    }

    private Type type;
    private String text;
    private String notice;

    public ResponseMessage(Type type, String text, String notice) {
        this.type = type;
        this.text = text;
        this.notice = notice;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }
}
