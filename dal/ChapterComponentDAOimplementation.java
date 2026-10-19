package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.ChapterComponent;
import model.ComponentType;
public class ChapterComponentDAOimplementation implements ChapterComponentDAO 
{
	// get all components of a chapter, in the saved order
    @Override
    public List<ChapterComponent> getComponentsByChapterId(int chapterId)
    {
    	List<ChapterComponent> list = new ArrayList<>();
        String sql = "SELECT chapter_id, component_type, sort_order, content " + "FROM chapter_components WHERE chapter_id = ? ORDER BY sort_order";
        // open the connection and it  closes by itself at the end
        try (Connection con = DBconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql))
        {
            ps.setInt(1, chapterId);
            try (ResultSet rs = ps.executeQuery()) 
            {
                // each row becomes one ChapterComponent object
                while (rs.next()) 
                {
                    ChapterComponent component = new ChapterComponent();
                    component.setChapterId(rs.getInt("chapter_id"));
                    component.setType(ComponentType.valueOf(rs.getString("component_type")));
                    component.setPosition(rs.getInt("sort_order"));
                    component.setContent(rs.getString("content"));
                    list.add(component);
                }
            }

        } 
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return list;
    }
 // Save the new structure of a chapter
    @Override
    public boolean saveStructure(int chapterId, List<ComponentType> orderedTypes)
    {
        // components the chapter has right now
        List<ChapterComponent> oldComponents = getComponentsByChapterId(chapterId);
        try (Connection con = DBconnection.getConnection())
        {
            // remove the old components that were not selected again
            for (ChapterComponent old : oldComponents) 
            {
                if (!orderedTypes.contains(old.getType())) 
                {
                    deleteComponent(con, chapterId, old.getType());
                }
            }
            // save the selected components, numbered 1, 2, 3
            int position = 1;
            for (ComponentType type : orderedTypes) 
            {
                saveComponent(con, chapterId, type, position);
                position++;
            }
            return true;
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
            return false;
        }
    }
    // delete one component of a chapter
    private void deleteComponent(Connection con, int chapterId, ComponentType type)
            throws SQLException
    {
        String sql = "DELETE FROM chapter_components WHERE chapter_id = ? AND component_type = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) 
        {
            ps.setInt(1, chapterId);
            ps.setString(2, type.name());
            ps.executeUpdate();
        }
    }
    // add a component, or only change its position if it already exists
    private void saveComponent(Connection con, int chapterId, ComponentType type, int position)
            throws SQLException 
    {
        String sql = "INSERT INTO chapter_components (chapter_id, component_type, sort_order) " + "VALUES (?, ?, ?) " + "ON DUPLICATE KEY UPDATE sort_order = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) 
        {
            ps.setInt(1, chapterId);
            ps.setString(2, type.name());
            ps.setInt(3, position);
            ps.setInt(4, position);
            ps.executeUpdate();
        }
    }
}