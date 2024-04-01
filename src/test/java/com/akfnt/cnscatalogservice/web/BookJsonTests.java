package com.akfnt.cnscatalogservice.web;

import com.akfnt.cnscatalogservice.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookJsonTests {
    @Autowired
    private JacksonTester<Book> json;

    @Test
    void testSerialize() throws Exception {
        var book = new Book("1234567890", "Title", "Author", 9.90);
        var jsonContext = json.write(book);
        assertThat(jsonContext).extractingJsonPathStringValue("@.isbn")
                .isEqualTo(book.isbn());
        assertThat(jsonContext).extractingJsonPathStringValue("@.title")
                .isEqualTo(book.title());
        assertThat(jsonContext).extractingJsonPathStringValue("@.author")
                .isEqualTo(book.author());
        assertThat(jsonContext).extractingJsonPathNumberValue("@.price")
                .isEqualTo(book.price());
    }

    @Test
    void testDeserialize() throws Exception {
        var content = """
                {
                    "isbn":"1234567890",
                    "title":"Title",
                    "author":"Author",
                    "price":9.90
                }
                """;
        assertThat(json.parse(content))
                .usingRecursiveComparison()
                .isEqualTo(new Book("1234567890", "Title", "Author", 9.90));
    }
}
