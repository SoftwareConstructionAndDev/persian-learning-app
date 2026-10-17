package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import bll.ChapterBO;
import dal.ChapterDAO;
import dto.ChapterDTO;
import model.Chapter;

import java.util.ArrayList;
import java.util.List;

class ChapterBOTest {

    static class ChapterDAOStub implements ChapterDAO {

        @Override
        public boolean addChapter(Chapter chapter) {
            return true;
        }

        @Override
        public List<Chapter> getAllChapters() {
            List<Chapter> chapters = new ArrayList<>();
            chapters.add(new Chapter(1, "Introduction"));
            chapters.add(new Chapter(2, "Persian Grammar"));
            return chapters;
        }

        @Override
        public Chapter getChapterById(int chapterId) {
            if (chapterId == 1) {
                return new Chapter(1, "Introduction");
            }

            if (chapterId == 2) {
                return new Chapter(2, "Persian Grammar");
            }

            return null;
        }

        @Override
        public boolean updateChapter(Chapter chapter) {
            return true;
        }

        @Override
        public boolean deleteChapter(int chapterId) {
            return chapterId == 1 || chapterId == 2;
        }
    }

    @Test
    void validTitleShouldBeAccepted() {
        ChapterBO bo = new ChapterBO(new ChapterDAOStub());
        ChapterDTO dto = new ChapterDTO(0, "Introduction");

        assertTrue(bo.addChapter(dto));
    }

    @Test
    void emptyTitleShouldBeRejected() {
        ChapterBO bo = new ChapterBO(new ChapterDAOStub());
        ChapterDTO dto = new ChapterDTO(0, "");

        assertFalse(bo.addChapter(dto));
    }

    @Test
    void spacesOnlyTitleShouldBeRejected() {
        ChapterBO bo = new ChapterBO(new ChapterDAOStub());
        ChapterDTO dto = new ChapterDTO(0, "   ");

        assertFalse(bo.addChapter(dto));
    }

    @Test
    void nullTitleShouldBeRejected() {
        ChapterBO bo = new ChapterBO(new ChapterDAOStub());
        ChapterDTO dto = new ChapterDTO(0, null);

        assertFalse(bo.addChapter(dto));
    }

    @Test
    void nullDTOShouldBeRejected() {
        ChapterBO bo = new ChapterBO(new ChapterDAOStub());

        assertFalse(bo.addChapter(null));
    }

    @Test
    void nullTitleShouldBeRejectedForUpdate() {
        ChapterBO bo = new ChapterBO(new ChapterDAOStub());
        ChapterDTO dto = new ChapterDTO(1, null);

        assertFalse(bo.updateChapter(dto));
    }

    @Test
    void validTitleShouldBeAcceptedForUpdate() {
        ChapterBO bo = new ChapterBO(new ChapterDAOStub());
        ChapterDTO dto = new ChapterDTO(1, "Updated Introduction");

        assertTrue(bo.updateChapter(dto));
    }

    @Test
    void getAllChaptersShouldReturnTwoChapters() {
        ChapterBO bo = new ChapterBO(new ChapterDAOStub());

        List<Chapter> chapters = bo.getAllChapters();

        assertEquals(2, chapters.size());
        assertEquals("Introduction", chapters.get(0).getTitle());
        assertEquals("Persian Grammar", chapters.get(1).getTitle());
    }

    @Test
    void validChapterIdShouldReturnChapter() {
        ChapterBO bo = new ChapterBO(new ChapterDAOStub());

        Chapter chapter = bo.getChapterById(1);

        assertNotNull(chapter);
        assertEquals("Introduction", chapter.getTitle());
    }

    @Test
    void invalidChapterIdShouldReturnNull() {
        ChapterBO bo = new ChapterBO(new ChapterDAOStub());

        assertNull(bo.getChapterById(0));
        assertNull(bo.getChapterById(5));
    }

    @Test
    void validChapterIdShouldBeDeleted() {
        ChapterBO bo = new ChapterBO(new ChapterDAOStub());

        assertTrue(bo.deleteChapter(1));
    }

    @Test
    void invalidChapterIdShouldNotBeDeleted() {
        ChapterBO bo = new ChapterBO(new ChapterDAOStub());

        assertFalse(bo.deleteChapter(0));
        assertFalse(bo.deleteChapter(-1));
    }
}
