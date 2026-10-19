package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import bll.ChapterStructureBO;
import dal.ChapterComponentDAO;
import dal.ChapterDAO;
import dto.ResultStatus;
import model.Chapter;
import model.ChapterComponent;
import model.ComponentType;

public class ChapterStructureBOTest {

    // Chapter stub only chapters 1 and 2 exist
    static class ChapterDAOStub implements ChapterDAO 
    {
        public Chapter getChapterById(int id) 
        {
            if (id == 1 || id == 2) 
            {
                return new Chapter(id, "Chapter " + id);
            }
            return null;
        }
        // not needed by these tests
        public boolean addChapter(Chapter c) 
        { 
        	return true;
        	}
        public List<Chapter> getAllChapters() 
        { 
        	return new ArrayList<>(); 
        	}
        public boolean updateChapter(Chapter c) 
        { 
        	return true; 
        	}
        public boolean deleteChapter(int id) 
        { 
        	return true;
        	}
    }
    // Component stub keeps each chapter's structure in memory
    static class ComponentDAOStub implements ChapterComponentDAO
    {
        Map<Integer, List<ComponentType>> saved = new HashMap<>();
        boolean failOnSave = false;   // true = pretend the database failed
        public boolean saveStructure(int chapterId, List<ComponentType> types) 
        {
            if (failOnSave) 
            {
                return false;
            }
            saved.put(chapterId, new ArrayList<>(types));
            return true;
        }
        public List<ChapterComponent> getComponentsByChapterId(int chapterId) 
        {
            List<ChapterComponent> result = new ArrayList<>();
            List<ComponentType> types = saved.getOrDefault(chapterId, new ArrayList<>());
            for (int i = 0; i < types.size(); i++) 
            {
                result.add(new ChapterComponent(chapterId, types.get(i), i + 1, null));
            }
            return result;
        }
    }

    private ComponentDAOStub componentStub = new ComponentDAOStub();
    private ChapterStructureBO bo = new ChapterStructureBO(new ChapterDAOStub(), componentStub);
    // (helper) the component types saved for a chapter, in order
    private List<ComponentType> savedTypes(int chapterId) 
    {
        List<ComponentType> types = new ArrayList<>();
        for (ChapterComponent c : bo.getStructure(chapterId)) 
        {
            types.add(c.getType());
        }
        return types;
    }

    //selection 
    // the admin is shown every supported component
    @Test
    public void allSupportedComponentsAreShown()
    {
        assertEquals(ComponentType.values().length, bo.getSupportedComponents().size());
    }

    // a valid selection is saved
    @Test
    public void validSelectionIsSaved()
    {
        List<ComponentType> chosen = Arrays.asList(ComponentType.LEARNING_OBJECTIVES, ComponentType.EXAMPLES);

        ResultStatus result = bo.configureStructure(1, chosen);

        assertTrue(result.isSuccess());
        assertEquals(chosen, savedTypes(1));
    }

    // Ordering 
    // components get positions 1, 2, 3 in the chosen order
    @Test
    public void componentsAreSavedInChosenOrder() 
    {
        bo.configureStructure(1, Arrays.asList(ComponentType.GLOSSARY, ComponentType.EXAMPLES, ComponentType.GRAMMAR_NOTE));

        List<ChapterComponent> structure = bo.getStructure(1);
        assertEquals(ComponentType.GLOSSARY, structure.get(0).getType());
        assertEquals(1, structure.get(0).getPosition());
        assertEquals(ComponentType.GRAMMAR_NOTE, structure.get(2).getType());
        assertEquals(3, structure.get(2).getPosition());
    }

    // saving again with a new order changes the order
    @Test
    public void reorderingChangesTheOrder() 
    {
        bo.configureStructure(1, Arrays.asList(ComponentType.LEARNING_OBJECTIVES, ComponentType.GLOSSARY));
        bo.configureStructure(1, Arrays.asList(ComponentType.GLOSSARY, ComponentType.LEARNING_OBJECTIVES));

        assertEquals(Arrays.asList(ComponentType.GLOSSARY, ComponentType.LEARNING_OBJECTIVES), savedTypes(1));
    }

    //Removal 
    // a component left out of the new selection is removed
    @Test
    public void unselectedComponentIsRemoved() 
    {
        bo.configureStructure(1, Arrays.asList(ComponentType.LEARNING_OBJECTIVES, ComponentType.EXAMPLES));
        bo.configureStructure(1, Arrays.asList(ComponentType.LEARNING_OBJECTIVES));

        assertEquals(Arrays.asList(ComponentType.LEARNING_OBJECTIVES), savedTypes(1));
    }

    // Different chapters, different structures 
    @Test
    public void differentChaptersCanHaveDifferentStructures()
    {
        bo.configureStructure(1, Arrays.asList(ComponentType.LEARNING_OBJECTIVES, ComponentType.EXAMPLES));
        bo.configureStructure(2, Arrays.asList(ComponentType.PERSIAN_DIALOGUE));

        assertEquals(Arrays.asList(ComponentType.LEARNING_OBJECTIVES, ComponentType.EXAMPLES), savedTypes(1));
        assertEquals(Arrays.asList(ComponentType.PERSIAN_DIALOGUE), savedTypes(2));
    }

    //invalid configurations 

    // nothing selected
    @Test
    public void emptySelectionIsRejected() 
    {
        ResultStatus result = bo.configureStructure(1, new ArrayList<ComponentType>());

        assertFalse(result.isSuccess());
        assertEquals("Select at least one component for the chapter.", result.getMessage());
    }

    // no list at all
    @Test
    public void nullSelectionIsRejected() 
    {
        assertFalse(bo.configureStructure(1, null).isSuccess());
    }

    // same component selected twice
    @Test
    public void duplicateComponentIsRejected() 
    {
        ResultStatus result = bo.configureStructure(1, Arrays.asList(ComponentType.EXAMPLES, ComponentType.EXAMPLES));

        assertFalse(result.isSuccess());
        assertEquals("'Examples' is selected more than once.", result.getMessage());
    }

    // an empty  entry inside the selection
    @Test
    public void nullComponentIsRejected()
    {
        ResultStatus result = bo.configureStructure(1, Arrays.asList(ComponentType.EXAMPLES, null));

        assertFalse(result.isSuccess());
        assertEquals("An unsupported component was selected.", result.getMessage());
    }

    // the chapter does not exist 
    @Test
    public void missingChapterIsRejected() 
    {
        assertFalse(bo.configureStructure(0, Arrays.asList(ComponentType.EXAMPLES)).isSuccess());

        ResultStatus result = bo.configureStructure(99, Arrays.asList(ComponentType.EXAMPLES));
        assertFalse(result.isSuccess());
        assertEquals("The selected chapter does not exist.", result.getMessage());
    }

    //The last valid structure is kept 
    // an invalid selection does not replace the saved structure
    @Test
    public void invalidSelectionKeepsPreviousStructure() 
    {
        List<ComponentType> good = Arrays.asList(ComponentType.LEARNING_OBJECTIVES, ComponentType.EXAMPLES);
        bo.configureStructure(1, good);

        bo.configureStructure(1, Arrays.asList(ComponentType.GLOSSARY, ComponentType.GLOSSARY));

        assertEquals(good, savedTypes(1));
    }

    // if the database fails, the saved structure stays the same
    @Test
    public void failedSaveKeepsPreviousStructure() {
        List<ComponentType> good = Arrays.asList(ComponentType.LEARNING_OBJECTIVES);
        bo.configureStructure(1, good);

        componentStub.failOnSave = true;
        ResultStatus result = bo.configureStructure(1, Arrays.asList(ComponentType.GLOSSARY));

        assertFalse(result.isSuccess());
        assertEquals("The structure could not be saved. The previous structure was kept.", result.getMessage());
        assertEquals(good, savedTypes(1));
    }
}