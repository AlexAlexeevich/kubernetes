package ru.alex.bookmarkerapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alex.bookmarkerapi.dto.BookmarkDto;
import ru.alex.bookmarkerapi.dto.BookmarkVM;
import ru.alex.bookmarkerapi.dto.BookmarksDto;
import ru.alex.bookmarkerapi.dto.CreateBookmarkRequest;
import ru.alex.bookmarkerapi.entity.Bookmark;
import ru.alex.bookmarkerapi.mapper.BookmarkMapper;
import ru.alex.bookmarkerapi.repository.BookmarkRepository;

import java.time.Instant;

@Service
@Transactional
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final BookmarkMapper bookmarkMapper;

    @Transactional(readOnly = true)
    public BookmarksDto getBookmarks(Integer page) {
        int pageNo = page < 1 ? 0 : page - 1;
        Pageable pageable = PageRequest.of(pageNo, 5, Sort.Direction.DESC, "createdAt");
        Page<BookmarkDto> bookmarkPage = bookmarkRepository.findBookmarks(pageable);
        return new BookmarksDto(bookmarkPage);
    }

    @Transactional(readOnly = true)
    public BookmarksDto searchBookmarks(String query, Integer page) {
        int pageNo = page < 1 ? 0 : page - 1;
        Pageable pageable = PageRequest.of(pageNo, 5, Sort.Direction.DESC, "createdAt");
        Page<BookmarkDto> bookmarkPage = bookmarkRepository.searchBookmarks(query, pageable);
        Page<BookmarkVM> bookmarkVMPage = bookmarkRepository.findByTitleContainsIgnoreCase(query, pageable);
        return new BookmarksDto(bookmarkPage);
    }

    public BookmarkDto createBookmark(CreateBookmarkRequest request) {
        Bookmark bookmark = new Bookmark(null, request.getTitle(), request.getUrl(), Instant.now());
        Bookmark savedBookmark = bookmarkRepository.save(bookmark);
        return bookmarkMapper.toDto(savedBookmark);
    }
}
