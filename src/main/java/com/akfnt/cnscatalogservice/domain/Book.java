package com.akfnt.cnscatalogservice.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.springframework.data.annotation.*;

import java.time.Instant;

public record Book(
        @Id
        Long id,
        @NotBlank(message = "The book ISBN must be defined.")
        @Pattern(
                regexp = "^([0-9]{10}|[0-9]{13})$",
                message = "The ISBN format must be valid."
        )
        String isbn,
        @NotBlank(message = "The book title must be defined.")
        String title,
        @NotBlank(message = "The book author must be defined.")
        String author,
        @NotNull(message = "The book price must be defined.")
        @Positive(message = "The book price must be greater than zero.")
        Double price,
        String publisher,

        @CreatedDate
        Instant createdDate,
        @LastModifiedDate
        Instant lastModifiedDate,
        @CreatedBy
        String createdBy,
        @LastModifiedBy
        String lastModifiedBy,

        // 낙관적 잠금(optimistic locking)을 위해 사용되는 엔티티 버전 번호
        // 업데이트 시 업데이트를 위해 읽은 이후 변경사항이 있는지 확인 -> 변경사항 있으면 업데이트는 수행되지 않고 예외를 발생시킴
        // 0 부터 시작하고 업데이트 시 마다 자동으로 증가함
        @Version
        int version
) {
        public static Book of(String isbn, String title, String author, Double price, String publisher) {
                // id가 널이고 버전이 0이면 새로운 엔티티로 인식함
                return new Book(null, isbn, title, author, price, publisher, null, null,null, null, 0);
        }
}
