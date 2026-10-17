package factory;

import dal.ChapterDAO;
import dal.ChapterDAOimplementation;

public class MysqlFactory implements DatabaseFactory {

    @Override
    public ChapterDAO createChapterDAO() {
        return new ChapterDAOimplementation();
    }
}
