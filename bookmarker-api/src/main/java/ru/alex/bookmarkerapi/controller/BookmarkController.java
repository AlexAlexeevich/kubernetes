package ru.alex.bookmarkerapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.alex.bookmarkerapi.dto.BookmarkDto;
import ru.alex.bookmarkerapi.dto.BookmarksDto;
import ru.alex.bookmarkerapi.dto.CreateBookmarkRequest;
import ru.alex.bookmarkerapi.service.BookmarkService;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @GetMapping
    public BookmarksDto getBookmarks(@RequestParam(name = "page", defaultValue = "1") Integer page,
                                     @RequestParam(name = "query", defaultValue = "") String query) {
        if (query == null || query.trim().length() == 0) {
            return bookmarkService.getBookmarks(page);
        }
        return bookmarkService.searchBookmarks(query, page);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookmarkDto createBookmark(@RequestBody @Valid CreateBookmarkRequest request) {
        return bookmarkService.createBookmark(request);
    }

}
