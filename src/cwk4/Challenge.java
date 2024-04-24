package cwk4;


/**
 * Write a description of class Challenge here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Challenge
{
    // instance variables - replace the example below with your own
    private int challengeNo;
    private ChallengeType type;
    private String enemy;
    private int skillRequired;
    private int reward;
    
    public Challenge(int challengeNo, ChallengeType type, String enemy,
                    int skillRequired, int reward){
        this.challengeNo = challengeNo;
        this.type = type;
        this.enemy = enemy;
        this.skillRequired = skillRequired;
        this.reward = reward;
    }
    // hh
    public int getChallengeNo(){
        return challengeNo;
    }
    public ChallengeType getType(){return type;}
    public String getEnemy(){
        return enemy;
    }
    public int getSkillRequired(){
        return skillRequired;
    }
    public int getReward(){
        return reward;
    }
}
