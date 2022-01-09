package by.latushko.anyqueries.util.pagination;

import by.latushko.anyqueries.model.entity.BaseEntity;

import java.util.Collections;
import java.util.List;

import static by.latushko.anyqueries.util.AppProperty.APP_RECORDS_PER_PAGE;

public final class Paginated<T extends BaseEntity> {
    private final List<T> content;
    private final long totalPages;

    public Paginated(List<T> data) {
        if(data != null && !data.isEmpty()) {
            this.totalPages = (long) Math.ceil((double) data.get(0).getTotal() / APP_RECORDS_PER_PAGE);
            this.content = data;
        } else {
            this.totalPages = 0L;
            this.content = Collections.emptyList();
        }
    }

    public long getTotalPages() {
        return totalPages;
    }

    public List<T> getContent() {
        return content;
    }
}
