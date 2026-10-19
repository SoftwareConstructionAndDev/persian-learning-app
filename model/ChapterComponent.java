package model;

public class ChapterComponent 
{
    private int chapterId;
    private ComponentType type;
    private int position;
    private String content;
    public ChapterComponent() {}
    public ChapterComponent(int chapterId, ComponentType type, int position, String content) 
    {
        this.chapterId = chapterId;
        this.type = type;
        this.position = position;
        this.content = content;
    }
    public int getChapterId() 
    {
        return chapterId;
    }
    public void setChapterId(int chapterId)
    {
        this.chapterId = chapterId;
    }
    public ComponentType getType() 
    {
        return type;
    }
    public void setType(ComponentType type) 
    {
        this.type = type;
    }
    public int getPosition()
    {
        return position;
    }
    public void setPosition(int position) 
    {
        this.position = position;
    }
    public String getContent() 
    {
        return content;
    }
    public void setContent(String content) 
    {
        this.content = content;
    }
}