package bll;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import dal.ChapterComponentDAO;
import dal.ChapterDAO;
import dto.ResultStatus;
import model.ChapterComponent;
import model.ComponentType;

public class ChapterStructureBO 
{
    private ChapterDAO chapterDAO;
    private ChapterComponentDAO componentDAO;
    public ChapterStructureBO(ChapterDAO chapterDAO, ChapterComponentDAO componentDAO) 
    {
        this.chapterDAO = chapterDAO;
        this.componentDAO = componentDAO;
    }
    //components the admin can choose from
    public List<ComponentType> getSupportedComponents() 
    {
        return Arrays.asList(ComponentType.values());
    }
    //saved structure of a chapter, in order
    public List<ChapterComponent> getStructure(int chapterId)
    {
        if (chapterId <= 0) 
        {
            return new ArrayList<>();
        }
        return componentDAO.getComponentsByChapterId(chapterId);
    }
    // check the selection and then save it
    public ResultStatus configureStructure(int chapterId, List<ComponentType> selected) 
    {
        // the chapter must exist
        if (chapterId <= 0 || chapterDAO.getChapterById(chapterId) == null)
        {
            return new ResultStatus(false, "The selected chapter does not exist.");
        }
        // at least one component must be selected
        if (selected == null || selected.isEmpty())
        {
            return new ResultStatus(false, "Select at least one component for the chapter.");
        }
        // no empty entries and no component selected twice
        List<ComponentType> alreadySeen = new ArrayList<>();
        for (ComponentType type : selected) 
        {
            if (type == null) 
            {
                return new ResultStatus(false, "An unsupported component was selected.");
            }
            if (alreadySeen.contains(type)) 
            {
                return new ResultStatus(false, "'" + type.getLabel() + "' is selected more than once.");
            }
            alreadySeen.add(type);
        }
        // if the selection is valid, so save it
        boolean saved = componentDAO.saveStructure(chapterId, new ArrayList<>(selected));
        if (!saved)
        {
            return new ResultStatus(false, "The structure could not be saved. The previous structure was kept.");
        }
        return new ResultStatus(true, "Chapter structure saved.");
    }
}