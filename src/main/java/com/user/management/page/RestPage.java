package com.user.management.page;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * page 객체를 역직렬화하기 위한 Page 구현 클래스
 *
 * @param <T> 페이지 내부 콘텐츠에 포함될 클래스
 * @author parksangwon
 * @version 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true, value = {"pageable"})
public class RestPage<T> extends PageImpl<T> {
    /**
     * 새로운 Rest page를 인스턴스화합니다.
     *
     * @param content 페이지 내용
     * @param page 페이지 번호
     * @param size 한 페이지의 크기
     * @param total 총 페이지 수
     */
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public RestPage(@JsonProperty("content") List<T> content,
                    @JsonProperty("number") int page,
                    @JsonProperty("size") int size,
                    @JsonProperty("totalElements") long total) {
        super(content, PageRequest.of(page, size), total);
    }

    /**
     * 새로운 Rest page를 인스턴스화합니다.
     *
     * @param page Page<T> 인스턴스
     */
    public RestPage(Page<T> page) {
        super(page.getContent(), page.getPageable(), page.getTotalElements());
    }
}