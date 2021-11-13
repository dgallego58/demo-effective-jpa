package com.demo.infrastructure.port.input.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.EnumMap;
import java.util.Map;

@Schema(description = "the filters used, offset is where the record start, and limit how many records will be loaded", example = "{\n" +
        "  \"limit\": 20,\n" +
        "  \"offset\":" +
        " 0,\n" +
        "  " +
        "  \"" +
        "filters\": {\n" +
        "    \"name\": \"Gabriel García Márquez\",\n" +
        "    \"title\": \"Cien Años de Soledad\",\n" +
        "    \"location\": \"Universidad Nacional\"\n" +
        "  }" +
        "\n" +
        "}")
public class FilterDTO {

    private int offset;
    private int limit;
    private Map<Filter, Object> filters;

    public FilterDTO() {
        this.offset = 0;
        this.limit = 20;
        this.filters = new EnumMap<>(Filter.class);
    }

    public int getOffset() {
        return offset;
    }

    public FilterDTO setOffset(int offset) {
        this.offset = offset;
        return this;
    }

    public int getLimit() {
        return limit;
    }

    public FilterDTO setLimit(int limit) {
        this.limit = limit;
        return this;
    }

    public Map<Filter, Object> getFilters() {
        return filters;
    }

    public FilterDTO setFilters(Map<Filter, Object> filters) {
        this.filters = filters;
        return this;
    }
}
