package dto;

public class ResultStatus 
{
    private boolean success;   // true if the operation worked
    private String message;    // show the user result to the user
    public ResultStatus(boolean success, String message) 
    {
        this.success = success;
        this.message = message;
    }
    public boolean isSuccess() 
    {
        return success;
    }
    public String getMessage() 
    {
        return message;
    }
}