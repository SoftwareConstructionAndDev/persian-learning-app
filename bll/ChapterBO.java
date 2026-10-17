package bll;

import java.util.List;
import dal.ChapterDAO;
import dto.ChapterDTO;
import model.Chapter;

public class ChapterBO {

    private ChapterDAO chapterDAO;

    public ChapterBO(ChapterDAO chapterDAO) {
        this.chapterDAO = chapterDAO;
    }

    public boolean addChapter(ChapterDTO dto) {
        if (dto == null || dto.getTitle() == null|| dto.getTitle().trim().isEmpty()) {
            return false;
        }

        Chapter chapter = new Chapter();
        chapter.setTitle(dto.getTitle());

        return chapterDAO.addChapter(chapter);
    }

    public List<Chapter> getAllChapters() {
        return chapterDAO.getAllChapters();
    }

    public boolean updateChapter(ChapterDTO dto) {
        if (dto == null || dto.getTitle() == null || dto.getTitle().trim().isEmpty()) {
            return false;
        }

        Chapter chapter = new Chapter(
                dto.getChapterId(), dto.getTitle());

        return chapterDAO.updateChapter(chapter);
    }

    public boolean deleteChapter(int chapterId) {
        if (chapterId <= 0) {
            return false;
        }

        return chapterDAO.deleteChapter(chapterId);
    }
}
