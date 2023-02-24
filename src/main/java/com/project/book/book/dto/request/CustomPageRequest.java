package com.project.book.book.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Objects;

import static org.springframework.data.domain.PageRequest.*;

@Getter
@AllArgsConstructor
public class CustomPageRequest {

    private static final int DEFAULT_PAGE_LIMIT = 30;
    private static final int DEFAULT_PAGE_OFFSET = 0;
    private static final int MIN_DISPLAY_LIMIT = 1;
    private static final int MAX_DISPLAY_LIMIT = 100;

    private Integer offset;
    private Integer limit;

    public Pageable toPageable() {
        setAsOffset();
        setAsLimit();
        return of(offset, limit, Sort.by(Sort.Direction.DESC, "id"));
    }

    private void setAsOffset() {
        if (Objects.isNull(offset)) {
            offset = DEFAULT_PAGE_OFFSET;
        }
    }

    private void setAsLimit() {
        if (Objects.isNull(limit)) {
            limit = DEFAULT_PAGE_LIMIT;
        }
    }
}
