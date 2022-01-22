package by.latushko.anyqueries.util.pagination;

import static by.latushko.anyqueries.util.AppProperty.APP_RECORDS_PER_PAGE;

/**
 * The class represents data page requested by user
 */
public final class RequestPage {
    private final int page;
    private final int limit;
    private final int offset;

    /**
     * Instantiates a new Request page.
     * Calculates limit and offset values to make sql data selection
     *
     * @param pageParameter the page parameter. E.g from http request
     */
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

    /**
     * Gets the page.
     *
     * @return the page
     */
    public int getPage() {
        return page;
    }

    /**
     * Gets the limit.
     *
     * @return the limit
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Gets the offset.
     *
     * @return the offset
     */
    public int getOffset() {
        return offset;
    }
}
