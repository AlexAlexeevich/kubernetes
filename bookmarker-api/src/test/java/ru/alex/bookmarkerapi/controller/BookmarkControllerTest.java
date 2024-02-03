package ru.alex.bookmarkerapi.controller;

import lombok.SneakyThrows;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.alex.bookmarkerapi.entity.Bookmark;
import ru.alex.bookmarkerapi.repository.BookmarkRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:tc:postgresql:14-alpine:///bookmarks"
})
class BookmarkControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    BookmarkRepository bookmarkRepository;

    private List<Bookmark> bookmarks;

    @BeforeEach
    void setUp() {
        bookmarkRepository.deleteAllInBatch();
        bookmarks = new ArrayList<>();

        bookmarks.add(new Bookmark(null, "mail1", "https://mail.ru", Instant.now()));
        bookmarks.add(new Bookmark(null, "mail2", "https://mail.ru", Instant.now()));
        bookmarks.add(new Bookmark(null, "mail3", "https://mail.ru", Instant.now()));
        bookmarks.add(new Bookmark(null, "mail4", "https://mail.ru", Instant.now()));
        bookmarks.add(new Bookmark(null, "mail5", "https://mail.ru", Instant.now()));
        bookmarks.add(new Bookmark(null, "mail6", "https://mail.ru", Instant.now()));
        bookmarks.add(new Bookmark(null, "mail7", "https://mail.ru", Instant.now()));
        bookmarks.add(new Bookmark(null, "mail8", "https://mail.ru", Instant.now()));
        bookmarks.add(new Bookmark(null, "mail9", "https://mail.ru", Instant.now()));
        bookmarks.add(new Bookmark(null, "mail10", "https://mail.ru", Instant.now()));
        bookmarks.add(new Bookmark(null, "mail11", "https://mail.ru", Instant.now()));
        bookmarks.add(new Bookmark(null, "mail12", "https://mail.ru", Instant.now()));

        bookmarkRepository.saveAll(bookmarks);
    }

    @ParameterizedTest
    @CsvSource({
            "1, 12, 3, 1, true, false, true, false",
            "2, 12, 3, 2, false, false, true, true"
    })
    @SneakyThrows
    void shouldGetBookmarks(int pageNo, int totalElements, int totalPages, int currentPage, boolean isFirst,
                            boolean isLast, boolean hasNext, boolean hasPrevious) {
        mvc.perform(get("/api/bookmarks?page=" + pageNo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", CoreMatchers.equalTo(totalElements)))
                .andExpect(jsonPath("$.totalPages", CoreMatchers.equalTo(totalPages)))
                .andExpect(jsonPath("$.currentPage", CoreMatchers.equalTo(currentPage)))
                .andExpect(jsonPath("$.isFirst", CoreMatchers.equalTo(isFirst)))
                .andExpect(jsonPath("$.isLast", CoreMatchers.equalTo(isLast)))
                .andExpect(jsonPath("$.hasNext", CoreMatchers.equalTo(hasNext)))
                .andExpect(jsonPath("$.hasPrevious", CoreMatchers.equalTo(hasPrevious)));
    }

    @Test
    @SneakyThrows
    void shouldCreateBookmarkSuccessfully() {
        this.mvc.perform(post("/api/bookmarks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "SivaLabs Blog",
                                    "url": "https://sivalabs.in"
                                }
                                """)
                )
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.title", is("SivaLabs Blog")))
                .andExpect(jsonPath("$.url", is("https://sivalabs.in")));
    }

//    @Test
//    @SneakyThrows
//    void shouldFailToCreateBookmarkWhenUrlIsNotPresent() {
//        this.mvc.perform(
//                        post("/api/bookmarks")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content("""
//                                        {
//                                            "title": "SivaLabs Blog"
//                                        }
//                                        """)
//                )
//                .andExpect(status().isBadRequest())
////                .andExpect(header().string("Content-Type", is("application/problem+json")))
//                .andExpect(jsonPath("$.type", is("https://zalando.github.io/problem/constraint-violation")))
//                .andExpect(jsonPath("$.title", is("Constraint Violation")))
//                .andExpect(jsonPath("$.status", is(400)))
//                .andExpect(jsonPath("$.violations", hasSize(1)))
//                .andExpect(jsonPath("$.violations[0].field", is("url")))
//                .andExpect(jsonPath("$.violations[0].message", is("Url should not be empty")))
//                .andReturn();
//    }
}