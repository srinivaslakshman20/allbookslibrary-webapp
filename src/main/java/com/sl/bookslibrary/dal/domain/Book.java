package com.sl.bookslibrary.dal.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;
import java.util.List;

@Table(value = "book_by_id")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Book {

    @Id
    @PrimaryKeyColumn(name = "book_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String id;

    @Column("book_name")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String name;

    @Column("book_description")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String description;

    @Column("published_date")
    @CassandraType(type = CassandraType.Name.DATE)
    private LocalDate publishedDate;

    @Column("cover_ids")
    @CassandraType(type = CassandraType.Name.TEXT)
    private List<String> coverIds;

    @Column("author_ids")
    @CassandraType(type = CassandraType.Name.TEXT)
    private List<String> authorIds;

    @Column("author_names")
    @CassandraType(type = CassandraType.Name.TEXT)
    private List<String> authorNames;


}
