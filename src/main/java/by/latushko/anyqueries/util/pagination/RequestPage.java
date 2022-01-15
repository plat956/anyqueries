package by.latushko.anyqueries.util.pagination;

import static by.latushko.anyqueries.util.AppProperty.APP_RECORDS_PER_PAGE;

public final class RequestPage {
    private final int page;
    private final int limit;
    private final int offset;

    public RequestPage(String pageParameter) {
        int tempPage;
        try {
            tempPage = Integer.valueOf(pageParameter);
        } catch (NumberFormatException ex) {
            tempPage = 1;
        }
        this.page = tempPage;
        this.limit = APP_RECORDS_PER_PAGE;
        this.offset = (this.page - 1) * this.limit;
    }

    public int getPage() {
        return page;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }
}
