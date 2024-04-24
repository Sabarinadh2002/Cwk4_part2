package cwk4;

import java.io.Serializable;

public class Dragon extends Champion implements Serializable {
    private static final long serialVersionUID = 1L;
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

