package cwk4;


/**
 * Write a description of class Champion here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public abstract class Champion
{
    // instance variables - replace the example below with your own
    private String name;
    private int skillLevel;
    private int entryFee;
    private String type;
    private boolean isAvailable;
    private ChampionState state;
   
    
    public Champion( String name, int skillLevel, int entryFee, String type){
        this.name = name;
        this.skillLevel = skillLevel;
        this.entryFee = entryFee;
        this.type = type;
        this.isAvailable = true;
        this.state = ChampionState.WAITING;
    }
    
    public String getName(){ 
        return name;
    }
    public int getSkillLevel(){
        return skillLevel;
    }
    public int getEntryFee(){
        return entryFee;
    }
    public String getType(){
        return type;
    }
    public ChampionState getState(){
        return state;
    }
    public boolean isAvailable(){
        return isAvailable;
    }
    
    public void setState (ChampionState state){
        this.state = state;
    }
    
    
    public boolean isSuitableForChallenge(Challenge challenge){
    
        if(this.type.equals("Wizard")){
            return true;
        }else if (this.type.equals("Warrior")){
            return challenge.getType() == ChallengeType.FIGHT;
        }else if (this.type.equals("Dragon")){
            return challenge.getType() == ChallengeType.FIGHT ||
             (challenge.getType() == ChallengeType.MYSTERY && this.canDargonTalk());
             
        }return false;
    }
    
    private boolean canDargonTalk(){
        if(this instanceof Dragon){
            return ((Dragon)this).canTalk();
        }
        return false;
    }
    
}
