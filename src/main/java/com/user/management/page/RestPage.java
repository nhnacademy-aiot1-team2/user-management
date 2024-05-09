package com.user.management.page;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * page 객체를 deserialization 하기 위한 Page 구현 클래스
 *
 * @param <T> page 내부 content에 담을 클래스
 * @author parksangwon
 * @version 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true, value = {"pageable"})
public class RestPage<T> extends PageImpl<T> {
    /**
     * Instantiates a new Rest page.
     *
     * @param content the content
     * @param page    the page
     * @param size    the size
     * @param total   the total
     */
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public RestPage(@JsonProperty("content") List<T> content,
                    @JsonProperty("number") int page,
                    @JsonProperty("size") int size,
                    @JsonProperty("totalElements") long total) {
        super(content, PageRequest.of(page, size), total);
    }

    /**
     * Instantiates a new Rest page.
     *
     * @param page the page
     */
    public RestPage(Page<T> page) {
        super(page.getContent(), page.getPageable(), page.getTotalElements());
    }
}
