package cwk4;


/**
 * Write a description of class Wizard here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Wizard extends Champion {
    private String spellSpeciality;
    private boolean isNecromancer;
    
    public Wizard(String name, int skillLevel, int entryFee, boolean isNecromancer, String spellSpeciality) {
        super(name, skillLevel, entryFee, "Wizard");
        this.spellSpeciality = spellSpeciality;
        this.isNecromancer = isNecromancer;
    }

    public String getSpellSpeciality() {
        return spellSpeciality;
    }

    // Add any other wizard-specific methods here
}

