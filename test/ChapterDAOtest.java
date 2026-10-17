package test;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import dal.ChapterDAO;
import dal.ChapterDAOimplementation;
import model.Chapter;

class ChapterDAOTest {

    private ChapterDAO dao = new ChapterDAOimplementation();

    @Test
    void addChapterShouldReturnTrue() {
        Chapter chapter = new Chapter(0, "DAO Test Chapter");

        boolean result = dao.addChapter(chapter);

        assertTrue(result);
    }
    
    

@Test
void getAllChaptersShouldReturnList() {
    assertNotNull(dao.getAllChapters());
}


@Test
void getChapterByIdShouldReturnChapter() {
    Chapter chapter = dao.getChapterById(1);

    assertNotNull(chapter);
    assertEquals(1, chapter.getChapterId());
}


@Test
void updateChapterShouldReturnTrue() {
    Chapter chapter = dao.getChapterById(1);

    assertNotNull(chapter);

    chapter.setTitle("Updated DAO Test Chapter");

    assertTrue(dao.updateChapter(chapter));
}


@Test
void deleteChapterShouldReturnTrue() {
    Chapter chapter = new Chapter(0, "Temporary DAO Delete Test");

    assertTrue(dao.addChapter(chapter));

    List<Chapter> chapters = dao.getAllChapters();
    int chapterId = -1;

    for (Chapter item : chapters) {
        if (item.getTitle().equals("Temporary DAO Delete Test")) {
            chapterId = item.getChapterId();
        }
    }

    assertTrue(chapterId > 0);
    assertTrue(dao.deleteChapter(chapterId));
    assertNull(dao.getChapterById(chapterId));
}


}

