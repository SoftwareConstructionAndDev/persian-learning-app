package pl;

import java.util.Arrays;
import java.util.List;

import bll.ChapterStructureBO;
import dal.ChapterComponentDAO;
import dal.ChapterDAO;
import dto.ResultStatus;
import factory.DatabaseFactory;
import factory.MysqlFactory;
import model.Chapter;
import model.ComponentType;

// sets the structure of each chapter and prints the result
public class ConfigureChapterStructures 
{	
    public static void main(String[] args) 
    {

        // build the layers
        DatabaseFactory factory = new MysqlFactory();
        ChapterDAO chapterDAO = factory.createChapterDAO();
        ChapterComponentDAO componentDAO = factory.createChapterComponentDAO();
        ChapterStructureBO bo = new ChapterStructureBO(chapterDAO, componentDAO);
        ChapterStructureFacade facade = new ChapterStructureFacade(bo);

        // the structure of each chapter in order
        List<ComponentType> introduction = Arrays.asList(
                ComponentType.LEARNING_OBJECTIVES, ComponentType.URDU_EXPLANATION, ComponentType.EXAMPLES);
        List<ComponentType> alphabetAndNumbers = Arrays.asList(
                ComponentType.LEARNING_OBJECTIVES, ComponentType.URDU_EXPLANATION,
                ComponentType.EXAMPLES, ComponentType.EXERCISE, ComponentType.GLOSSARY);
        List<ComponentType> basicWords = Arrays.asList(
                ComponentType.LEARNING_OBJECTIVES, ComponentType.PERSIAN_DIALOGUE,
                ComponentType.URDU_EXPLANATION, ComponentType.EXAMPLES,
                ComponentType.EXERCISE, ComponentType.GLOSSARY);

        configure(facade, chapterDAO, "Introduction to Persian", introduction);
        configure(facade, chapterDAO, "Persian Alphabets", alphabetAndNumbers);
        configure(facade, chapterDAO, "Persian Numbers", alphabetAndNumbers);
        configure(facade, chapterDAO, "Basic Persian Words", basicWords);
    }

    // finds the chapter by its title, saves its structure and prints the message
    private static void configure(ChapterStructureFacade facade, ChapterDAO chapterDAO,
            String title, List<ComponentType> structure) 
    {

        for (Chapter chapter : chapterDAO.getAllChapters()) 
        {
            if (chapter.getTitle().equals(title)) 
            {
                ResultStatus result = facade.configureStructure(chapter.getChapterId(), structure);
                System.out.println(title + ": " + result.getMessage());
                return;
            }
        }
        System.out.println(title + ": chapter not found in the database.");
    }
}