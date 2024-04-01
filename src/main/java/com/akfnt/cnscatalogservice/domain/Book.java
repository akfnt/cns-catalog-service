package com.akfnt.cnscatalogservice.domain;

public record Book(
        String isbn,
        String title,
        String author,
        Double price
) { }
