package com.akfnt.cnscatalogservice.demo;

import com.akfnt.cnscatalogservice.domain.Book;
import com.akfnt.cnscatalogservice.domain.BookRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
//@Profile("testdata")
@ConditionalOnProperty(name="polar.testdata.enabled", havingValue = "true")
public class BookDataLoader {
    private final BookRepository bookRepository;

    public BookDataLoader(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // ApplicationReadyEvent 가 발생하면 테스트 데이터 생성이 시작된다
    // 이 이벤트는 애플리케이션 시작 단계가 완료되면 트리거 된다
    @EventListener(ApplicationReadyEvent.class)
    public void loadBookTestData() {
        // 빈 데이터베이스로 시작하기 위해 기존 책이 있다면 모두 삭제한다.
        bookRepository.deleteAll();

        // 프레임워크가 내부적으로 식별자와 버전에 대한 할당값을 처리한다
        var book1 = Book.of("1234567891", "Northern Lights", "Lyra Silverstar", 9.90, "Polarsophia");
        var book2 = Book.of("1234567892", "Polar Journey", "Iorek Polarson", 12.90, "Polarsophia");

        bookRepository.saveAll(List.of(book1, book2));
    }
}
