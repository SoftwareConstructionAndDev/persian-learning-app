package model;

public enum ComponentType 
{
    LEARNING_OBJECTIVES("Learning objectives"),
    PERSIAN_DIALOGUE("Persian dialogue"),
    PERSIAN_READING_PASSAGE("Persian reading passage"),
    URDU_EXPLANATION("Urdu translation or explanation"),
    GRAMMAR_NOTE("Grammar or language note"),
    EXAMPLES("Examples"),
    EXERCISE("Exercise (MCQs and short questions)"),
    GLOSSARY("Glossary");
    private final String label;
    
    ComponentType(String label) 
    {
        this.label = label;
    }
    public String getLabel()
    {
        return label;
    }
}