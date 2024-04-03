package com.akfnt.cnscatalogservice.domain;

import com.akfnt.cnscatalogservice.config.DataConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@Import(DataConfig.class)   // 데이터 설정을 임포트한다(Auditing 을 활성화하기 위해 필요)
@AutoConfigureTestDatabase( // 테스트 컨테이너를 이용해야 하기 때문에 내장 테스트 데이터베이스 사용을 비활성화 한다
        replace = AutoConfigureTestDatabase.Replace.NONE
)
@ActiveProfiles("integration")
public class BookRepositoryJdbcTests {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private JdbcAggregateTemplate jdbcAggregateTemplate;    // 데이터베이스와 상호작용하기 위한 하위 수준의 객체 (테스트 대상 Repository 대신 사용 용도)

    @Test
    void findBookByIsbnWhenExisting() {
        var bookIsbn = "1234561237";
        var book = Book.of(bookIsbn, "Title", "Author", 12.98);
        jdbcAggregateTemplate.insert(book);     // jdbcAggregateTemplate 은 테스트에 필요한 데이터를 준비하는데 사용한다
        Optional<Book> actualBook = bookRepository.findByIsbn(bookIsbn);

        assertThat(actualBook).isPresent();
        assertThat(actualBook.get().isbn()).isEqualTo(book.isbn());
    }
}
