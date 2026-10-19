package pl;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import bll.ChapterBO;
import bll.ChapterStructureBO;
import dto.ResultStatus;
import factory.DatabaseFactory;
import factory.MysqlFactory;
import model.Chapter;
import model.ChapterComponent;
import model.ComponentType;

public class ChapterStructureMenu
{
    public static void main(String[] args)
    {
        DatabaseFactory factory = new MysqlFactory();
        ChapterFacade chapterFacade = new ChapterFacade(new ChapterBO(factory.createChapterDAO()));
        ChapterStructureFacade structureFacade = new ChapterStructureFacade(
                new ChapterStructureBO(factory.createChapterDAO(), factory.createChapterComponentDAO()));
        Scanner in = new Scanner(System.in);

        // show the chapters
        System.out.println("Chapters:");
        for (Chapter c : chapterFacade.getAllChapters())
        {
            System.out.println("  " + c.getChapterId() + " - " + c.getTitle());
        }
        System.out.print("Enter chapter id: ");
        int chapterId;
        try
        {
            chapterId = Integer.parseInt(in.nextLine().trim());
        }
        catch (NumberFormatException e)
        {
            System.out.println("Please enter a number.");
            return;
        }

        // show its current structure
        System.out.println("Current structure:");
        for (ChapterComponent c : structureFacade.getStructure(chapterId))
        {
            System.out.println("  " + c.getPosition() + ". " + c.getType().getLabel());
        }

        // show the supported components
        List<ComponentType> all = structureFacade.getSupportedComponents();
        System.out.println("Supported components:");
        for (int i = 0; i < all.size(); i++)
        {
            System.out.println("  " + (i + 1) + ". " + all.get(i).getLabel());
        }

        // admin types the numbers in the order they want
        System.out.println("Type the numbers in your order, separated by spaces (e.g. 1 4 6):");
        String line = in.nextLine().trim();
        List<ComponentType> selected = new ArrayList<>();
        if (!line.isEmpty())
        {
            for (String part : line.split("\\s+"))
            {
                try
                {
                    int n = Integer.parseInt(part);
                    // a wrong number becomes null, which the BO rejects with a message
                    selected.add(n >= 1 && n <= all.size() ? all.get(n - 1) : null);
                }
                catch (NumberFormatException e)
                {
                    selected.add(null);
                }
            }
        }

        // validate and save
        ResultStatus result = structureFacade.configureStructure(chapterId, selected);
        System.out.println(result.getMessage());
    }
}