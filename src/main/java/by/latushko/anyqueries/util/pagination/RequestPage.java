package by.latushko.anyqueries.util.pagination;

import static by.latushko.anyqueries.util.AppProperty.APP_RECORDS_PER_PAGE;

public final class RequestPage {
    private final int page;
    private final int limit;
    private final int offset;

    public RequestPage(String pageParameter) {
        this.limit = APP_RECORDS_PER_PAGE;
        this.page = pageParameter != null && !pageParameter.isEmpty() ? Integer.valueOf(pageParameter) : 1;
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
