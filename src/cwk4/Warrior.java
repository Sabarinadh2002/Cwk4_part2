package cwk4;


/**
 * Write a description of class Warrior here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Warrior extends Champion {
    private String weaponType;

    public Warrior(String name, int skillLevel, int entryFee, String weaponType) {
        super(name, skillLevel, entryFee, "Warrior");
        this.weaponType = weaponType;
    }

    public String getWeaponType() {
        return weaponType;
    }

    // Add any other warrior-specific methods here
}
