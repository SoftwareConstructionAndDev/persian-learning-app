package dal;

import java.util.List;
import model.Chapter;

public interface ChapterDAO {
    boolean addChapter(Chapter chapter);

    List<Chapter> getAllChapters();

    Chapter getChapterById(int chapterId);

    boolean updateChapter(Chapter chapter);

    boolean deleteChapter(int chapterId);
}
