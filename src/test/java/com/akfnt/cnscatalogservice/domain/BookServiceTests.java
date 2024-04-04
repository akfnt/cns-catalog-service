package com.akfnt.cnscatalogservice.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

/*
단위 테스트에 공통적으로 사용할 확장 기능을 선언해주는 역할을 한다.
인자로 확장할 Extension을 명시하면 된다.
SpringExtension.class 또는 MockitoExtension.class를 많이 사용한다.
Spring Test Context 프레임워크와 Junit5와 통합해 사용할 때는 SpringExtension.class를 사용한다.
JUniit5와 Mockito를 연동해 테스트를 진행할 경우에는 MockitoExtension.class를 사용한다.
 */
@ExtendWith(MockitoExtension.class)
public class BookServiceTests {
    // Mock 객체를 생성한다. 실제로 메서드는 갖고 있지만 내부 구현이 없는 상태이다.
    @Mock
    private BookRepository bookRepository;

    /* @Mock 또는 @Spy로 생성된 가짜 객체를 자동으로 주입시켜주는 객체에 지정.
    @InjectMocks 객체에서 사용할 객체를 @Mock으로 만들어 쓰면 된다.
    만약 Service를 테스트하는 클래스를 생성했다면, Service 객체를 @InjectMocks 어노테이션을 사용해 생성하고,
    Service단에서 사용할 Repository와 같은 객체들은 @Mock 어노테이션을 사용해 생성하면 된다.
    */
    @InjectMocks
    private BookService bookService;

    @Test
    void whenBookToCreateAlreadyExistsThenThrows() {
        var bookIsbn = "1234561232";
        var bookToCreate = Book.of(bookIsbn, "Title", "Author", 9.90, "Polarsophia");
        when(bookRepository.existsByIsbn(bookIsbn)).thenReturn(true);
        assertThatThrownBy(() -> bookService.addBookToCatalog(bookToCreate))
                .isInstanceOf(BookAlreadyExistsException.class)
                .hasMessage("A book with ISBN " + bookIsbn + " already exists.");
    }

    @Test
    void whenBookToReadDoesNotExistThenThrows() {
        var bookIsbn = "1234561232";
        when(bookRepository.findByIsbn(bookIsbn)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> bookService.viewBookDetails(bookIsbn))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessage("The book with ISBN " + bookIsbn + " was not found.");
    }
}
