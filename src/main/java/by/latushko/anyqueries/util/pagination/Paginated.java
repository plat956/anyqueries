package by.latushko.anyqueries.util.pagination;

import by.latushko.anyqueries.model.entity.BaseEntity;

import java.util.List;

import static by.latushko.anyqueries.util.AppProperty.APP_RECORDS_PER_PAGE;

/**
 * The class represents paginated data
 *
 * @param <T> the entity class parameter
 */
public final class Paginated<T extends BaseEntity> {
    private final List<T> content;
    private final long totalPages;

    /**
     * Instantiates a new Paginated.
     * Fills the content and calculates total available pages
     *
     * @param data the list of entity objects. eg. returned from database
     */
    public Paginated(List<T> data) {
        if(data != null && !data.isEmpty()) {
            this.totalPages = (long) Math.ceil((double) data.get(0).getTotal() / APP_RECORDS_PER_PAGE);
            this.content = data;
        } else {
            this.totalPages = 0L;
            this.content = List.of();
        }
    }

    /**
     * Gets total pages.
     *
     * @return the total pages count
     */
    public long getTotalPages() {
        return totalPages;
    }

    /**
     * Gets content.
     *
     * @return the list of entity objects
     */
    public List<T> getContent() {
        return content;
    }
}
