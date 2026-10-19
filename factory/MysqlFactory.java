package factory;

import dal.ChapterDAO;
import dal.ChapterDAOimplementation;
import dal.ChapterComponentDAOimplementation;
import dal.ChapterComponentDAO;

public class MysqlFactory implements DatabaseFactory {

    @Override
    public ChapterDAO createChapterDAO() {
        return new ChapterDAOimplementation();
    }
    
    //usecase2
    @Override
    public ChapterComponentDAO createChapterComponentDAO() 
    {
        return new ChapterComponentDAOimplementation();
    }
}
