package dal;

import java.util.List;
import model.ChapterComponent;
import model.ComponentType;
public interface ChapterComponentDAO
{
    List<ChapterComponent> getComponentsByChapterId(int chapterId);
    boolean saveStructure(int chapterId, List<ComponentType> orderedTypes);
}