package by.latushko.anyqueries.controller.command;

public class ResponseMessage {
    public enum Type {
        ALERT, POPUP, TOAST
    }
    public enum Level {
        SUCCESS("success"),
        DANGER("danger"),
        INFO("info"),
        WARNING("warning");

        private String mode;

        Level(String mode) {
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
    private Level level;
    private String text;
    private String notice;

    {
        this.type = Type.ALERT;
    }

    public ResponseMessage(Level level, String text, String notice) {
        this.level = level;
        this.text = text;
        this.notice = notice;
    }

    public ResponseMessage(Level level, String text) {
        this.level = level;
        this.text = text;
    }

    public ResponseMessage(Type type, Level level, String text, String notice) {
        this.type = type;
        this.level = level;
        this.text = text;
        this.notice = notice;
    }

    public ResponseMessage(Type type, Level level, String text) {
        this.type = type;
        this.level = level;
        this.text = text;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public void setText(String text) {
        this.text = text;
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
