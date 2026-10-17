package factory;

import dal.ChapterDAO;

public interface DatabaseFactory {
    ChapterDAO createChapterDAO();
}
