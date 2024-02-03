package ru.alex.bookmarkerapi.mapper;

import org.springframework.stereotype.Component;
import ru.alex.bookmarkerapi.dto.BookmarkDto;
import ru.alex.bookmarkerapi.entity.Bookmark;

@Component
public class BookmarkMapper {

    public BookmarkDto toDto(Bookmark bookmark) {
        return new BookmarkDto(
                bookmark.getId(),
                bookmark.getTitle(),
                bookmark.getUrl(),
                bookmark.getCreatedAt()
        );
    }
}
