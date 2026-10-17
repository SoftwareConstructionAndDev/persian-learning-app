package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import model.Chapter;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ChapterDAOimplementation implements ChapterDAO {

    @Override
    public boolean addChapter(Chapter chapter) {
        String sql = "INSERT INTO chapters (title) VALUES (?)";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, chapter.getTitle());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


@Override
public List<Chapter> getAllChapters() {
    List<Chapter> chapters = new ArrayList<>();
    String sql = "SELECT chapter_id, title FROM chapters";

    try (Connection con = DBconnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Chapter chapter = new Chapter();
            chapter.setChapterId(rs.getInt("chapter_id"));
            chapter.setTitle(rs.getString("title"));
            chapters.add(chapter);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return chapters;
}



@Override
public Chapter getChapterById(int chapterId) {
    String sql = "SELECT chapter_id, title FROM chapters WHERE chapter_id = ?";

    try (Connection con = DBconnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, chapterId);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                Chapter chapter = new Chapter();
                chapter.setChapterId(rs.getInt("chapter_id"));
                chapter.setTitle(rs.getString("title"));
                return chapter;
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return null;
}


@Override
public boolean updateChapter(Chapter chapter) {
    String sql = "UPDATE chapters SET title = ? WHERE chapter_id = ?";

    try (Connection con = DBconnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, chapter.getTitle());
        ps.setInt(2, chapter.getChapterId());

        return ps.executeUpdate() > 0;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

@Override
public boolean deleteChapter(int chapterId) {
    String sql = "DELETE FROM chapters WHERE chapter_id = ?";

    try (Connection con = DBconnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, chapterId);

        return ps.executeUpdate() > 0;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
}
