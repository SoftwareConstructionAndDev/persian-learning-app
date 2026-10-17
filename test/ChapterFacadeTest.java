package test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import bll.ChapterBO;
import dal.ChapterDAO;
import dto.ChapterDTO;
import factory.DatabaseFactory;
import factory.MysqlFactory;
import model.Chapter;
import pl.ChapterFacade;

class ChapterFacadeTest {

    private ChapterFacade createFacade() {
        DatabaseFactory factory = new MysqlFactory();
        ChapterDAO dao = factory.createChapterDAO();
        ChapterBO bo = new ChapterBO(dao);
        return new ChapterFacade(bo);
    }

    @Test
    void addChapterThroughFacade() {
        ChapterFacade facade = createFacade();
        ChapterDTO dto = new ChapterDTO(0, "Facade Test Chapter");

        assertTrue(facade.addChapter(dto));
    }

    @Test
    void getAllChaptersThroughFacade() {
        ChapterFacade facade = createFacade();

        assertNotNull(facade.getAllChapters());
    }

    @Test
    void getChapterByIdThroughFacade() {
        ChapterFacade facade = createFacade();

        assertNotNull(facade.getChapterById(1));
    }

    @Test
    void updateChapterThroughFacade() {
        ChapterFacade facade = createFacade();
        Chapter chapter = facade.getChapterById(1);

        assertNotNull(chapter);

        ChapterDTO dto = new ChapterDTO(1, "Updated Through Facade");

        assertTrue(facade.updateChapter(dto));
    }

    @Test
    void deleteChapterThroughFacade() {
        ChapterFacade facade = createFacade();
        ChapterDTO dto = new ChapterDTO(0, "Temporary Delete Test");

        assertTrue(facade.addChapter(dto));

        List<Chapter> chapters = facade.getAllChapters();
        int chapterId = -1;

        for (Chapter chapter : chapters) {
            if (chapter.getTitle().equals("Temporary Delete Test")) {
                chapterId = chapter.getChapterId();
            }
        }

        assertTrue(chapterId > 0);
        assertTrue(facade.deleteChapter(chapterId));
    }
}
