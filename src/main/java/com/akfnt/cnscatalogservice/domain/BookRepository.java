package com.akfnt.cnscatalogservice.domain;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BookRepository extends CrudRepository<Book, Long> {
    // CrudRepository 의 기본 메서는 @Id 필드를 기반으로 하기 때문에 아래 작업을 명시적으로 선언해줘야 함
    Optional<Book> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    @Modifying  // 데이터베이스 상태를 수정할 연산임을 나타낸다
    @Query("delete from book where isbn = :isbn")
    void deleteByIsbn(String isbn);
}
