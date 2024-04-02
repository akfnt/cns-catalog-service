package com.akfnt.cnscatalogservice.demo;

import com.akfnt.cnscatalogservice.domain.Book;
import com.akfnt.cnscatalogservice.domain.BookRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

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
        var book1 = new Book("1234567891", "Northern Lights", "Lyra Silverstar", 9.90);
        var book2 = new Book("1234567892", "Polar Journey", "Iorek Polarson", 12.90);

        bookRepository.save(book1);
        bookRepository.save(book2);
    }
}
