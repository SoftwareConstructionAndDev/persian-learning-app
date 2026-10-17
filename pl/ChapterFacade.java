package pl;

import java.util.List;
import bll.ChapterBO;
import dto.ChapterDTO;
import model.Chapter;

public class ChapterFacade {

    private ChapterBO chapterBO;

    public ChapterFacade(ChapterBO chapterBO) {
        this.chapterBO = chapterBO;
    }

    public boolean addChapter(ChapterDTO dto) {
        return chapterBO.addChapter(dto);
    }

    public List<Chapter> getAllChapters() {
        return chapterBO.getAllChapters();
    }

    public boolean updateChapter(ChapterDTO dto) {
        return chapterBO.updateChapter(dto);
    }

    public boolean deleteChapter(int chapterId) {
        return chapterBO.deleteChapter(chapterId);
    }
}
