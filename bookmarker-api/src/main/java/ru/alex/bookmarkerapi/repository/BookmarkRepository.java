package ru.alex.bookmarkerapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.alex.bookmarkerapi.dto.BookmarkDto;
import ru.alex.bookmarkerapi.dto.BookmarkVM;
import ru.alex.bookmarkerapi.entity.Bookmark;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    @Query("select new ru.alex.bookmarkerapi.dto.BookmarkDto(b.id, b.title, b.url, b.createdAt) from Bookmark b")
    Page<BookmarkDto> findBookmarks(Pageable pageable);

    @Query("select new ru.alex.bookmarkerapi.dto.BookmarkDto(b.id, b.title, b.url, b.createdAt) from Bookmark b " +
            "where lower(b.title) like lower(concat('%', :query, '%'))")
    Page<BookmarkDto> searchBookmarks(String query, Pageable pageable);

    Page<BookmarkVM> findByTitleContainsIgnoreCase(String query, Pageable pageable);

}
