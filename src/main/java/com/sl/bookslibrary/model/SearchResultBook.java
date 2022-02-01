package com.sl.bookslibrary.model;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class SearchResultBook {

    private String key;
    private String title;
    private List<String> author_name;
    private String cover_i;
    private int first_publish_year;
}
