package cwk4;

public class Dragon extends Champion {
    private boolean canTalk;

    public Dragon(String name, int skillLevel, int entryFee, boolean canTalk) {
        super(name, skillLevel, entryFee, "Dragon");
        this.canTalk = canTalk;
    }

    public boolean canTalk() {
        return canTalk;
    }

    // Add any other dragon-specific methods here
}

