package factory;

import dal.ChapterDAO;
import dal.ChapterComponentDAO;
public interface DatabaseFactory {
    ChapterDAO createChapterDAO();
    
    // added for usecase2
    ChapterComponentDAO createChapterComponentDAO();
}

