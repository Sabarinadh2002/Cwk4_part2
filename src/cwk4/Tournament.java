package cwk4;
import java.util.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.stream.Collectors;
/**
 * This interface specifies the behaviour expected from CARE
 * as required for 5COM2007 Cwk 4
 * 
 * @author 
 * @version 
 */

public class Tournament implements CARE {

    private String vizier;
    private HashMap<String, Champion> champions;
    private HashMap<Integer, Challenge> challenges;
    private int treasury;

//**************** CARE ************************** 

    /**
     * Constructor requires the name of the vizier
     *
     * @param viz the name of the vizier
     */
    public Tournament(String viz) {
        this.treasury = 1000;
        this.vizier = viz;
        this.champions = new HashMap<>();
        this.challenges = new HashMap<>();
        readChallenges("challengesAM.txt");
        setupChampions();
        setupChallenges();
    }

    /**
     * Constructor requires the name of the vizier and the
     * name of the file storing challenges
     *
     * @param viz      the name of the vizier
     * @param filename name of file storing challenges
     */
    public Tournament(String viz, String filename)  //Task 3.5
    {



        setupChampions();
        readChallenges(filename);
    }


    /**
     * Returns a String representation of the state of the game,
     * including the name of the vizier, state of the treasury,
     * whether defeated or not, and the champions currently in the
     * team,(or, "No champions" if team is empty)
     *
     * @return a String representation of the state of the game,
     * including the name of the vizier, state of the treasury,
     * whether defeated or not, and the champions currently in the
     * team,(or, "No champions" if team is empty)
     **/
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nVizier: ").append(vizier);
        sb.append("\nTreasury: ").append(treasury);
        sb.append("\nDefeated: ").append(isDefeated() ? "Yes" : "No");

        String teamMembers = champions.values().stream()
                .filter(champion -> champion.getState() == ChampionState.ENTERED)
                .map(Champion::getName)
                .collect(Collectors.joining(", "));

        if (teamMembers.isEmpty()) {
            sb.append("\nTeam: No champions entered");
        } else {
            sb.append("\nTeam: ").append(teamMembers);
        }

        return sb.toString();


    }


    /**
     * returns true if Treasury <=0 and the vizier's team has no
     * champions which can be retired.
     *
     * @returns true if Treasury <=0 and the vizier's team has no
     * champions which can be retired.
     */
    public boolean isDefeated() {
        if (treasury <= 0) {
            // Check if there are no champions or no champions that can be retired
            return champions.isEmpty() || champions.values().stream()
                    .noneMatch(champion -> champion.isAvailable() &&
                            champion.getState() != ChampionState.DISQUALIFIED); }

        return false;
    }

    /**
     * returns the amount of money in the Treasury
     *
     * @returns the amount of money in the Treasury
     */
    public int getMoney() {


        return treasury;
    }


    /**
     * Returns a String representation of all champions in the reserves
     *
     * @return a String representation of all champions in the reserves
     **/
    public String getReserve() {
        StringBuilder sb = new StringBuilder("************ Champions available in reserves ********\n");
        String reserveChampions = champions.values().stream()
                .filter(Champion::isAvailable)
                .map(champion -> champion.getName() + " - " + champion.getType())
                .collect(Collectors.joining("\n"));

        if (reserveChampions.isEmpty()) {
            sb.append("No champions in reserve");
        } else {
            sb.append(reserveChampions);
        }

        return sb.toString();
    }


    /**
     * Returns details of the champion with the given name.
     * Champion names are unique.
     *
     * @return details of the champion with the given name
     **/
    public String getChampionDetails(String nme) {

        if (champions.containsKey(nme)) {
            Champion champion = champions.get(nme);
            return formatChampionDetails(champion);
        } else {
            return "\nNo such champion";
        }
    }

    private String formatChampionDetails(Champion champion) {
        // Assuming Champion has methods to get its details
        return "Name: " + champion.getName() +
                "\nType: " + champion.getType() +
                "\nSkill Level: " + champion.getSkillLevel() +
                "\nEntry Fee: " + champion.getEntryFee() +
                "\nState: " + champion.getState() +
                "\nAvailable: " + (champion.isAvailable() ? "Yes" : "No");
    }
    /**
     * returns whether champion is in reserve
     *
     * @param nme champion's name
     * @return true if champion in reserve, false otherwise
     */
    public boolean isInReserve(String nme) {
        Champion champion = champions.get(nme);
        return champion != null && champion.isAvailable();
    }

    // ***************** Team champions ************************   

    /**
     * Allows a champion to be entered for the vizier's team, if there
     * is enough money in the Treasury for the entry fee.The champion's
     * state is set to "active"
     * 0 if champion is entered in the vizier's team,
     * 1 if champion is not in reserve,
     * 2 if not enough money in the treasury,
     * -1 if there is no such champion
     *
     * @param nme represents the name of the champion
     * @return as shown above
     **/
    public int enterChampion(String nme) {
        Champion champ = champions.get(nme);
        if (champ == null) {
            return -1; // No such champion
        }
        if (!champ.isAvailable() || champ.getState() != ChampionState.WAITING) {
            return 1; // Champion not in reserve or already in team
        }
        if (treasury < champ.getEntryFee()) {
            return 2; // Not enough money in the treasury
        }
        treasury -= champ.getEntryFee();
        champ.setState(ChampionState.ENTERED);
        return 0;

    }

    /**
     * Returns true if the champion with the name is in
     * the vizier's team, false otherwise.
     *
     * @param nme is the name of the champion
     * @return returns true if the champion with the name
     * is in the vizier's team, false otherwise.
     **/
    public boolean isInViziersTeam(String nme) {
        Champion champion = champions.get(nme);
        return champion != null && champion.getState() == ChampionState.ENTERED;

    }

    /**
     * Removes a champion from the team back to the reserves (if they are in the team)
     * Pre-condition: isChampion()
     * 0 - if champion is retired to reserves
     * 1 - if champion not retired because disqualified
     * 2 - if champion not retired because not in team
     * -1 - if no such champion
     *
     * @param nme is the name of the champion
     * @return as shown above
     **/
    public int retireChampion(String nme) {
        Champion champ = champions.get(nme);
        if (champ == null) {
            return -1; // No such champion
        }
        if (champ.getState() != ChampionState.ENTERED) {
            return 2; // Champion not found or already retired
        }
        if (champ.getState() == ChampionState.DISQUALIFIED) {
            return 1; // Cannot retire because champion is disqualified
        }

        // Assume champion is retiring successfully
        int refundAmount = champ.getEntryFee() / 2;
        treasury += refundAmount;
        champ.setState(ChampionState.WAITING); // Set champion state back to waiting, assuming it means they are in reserve.
        return 0;  // Champion not found or already retired
    }
     // Champion is retired to reserves




    /**
     * Returns a String representation of the champions in the vizier's team
     * or the message "No champions entered"
     *
     * @return a String representation of the champions in the vizier's team
     **/
    public String getTeam() {
        String teamMembers = champions.values().stream()
                .filter(champion -> champion.getState() == ChampionState.ENTERED)
                .map(Champion::getName)
                .collect(Collectors.joining(", "));

        return teamMembers.isEmpty() ? "No champions entered." : "Team members: " + teamMembers;

    }

    /**
     * Returns a String representation of the disqualified champions in the vizier's team
     * or the message "No disqualified champions "
     *
     * @return a String representation of the disqualified champions in the vizier's team
     **/
    public String getDisqualified() {
        StringBuilder sb = new StringBuilder("************ Vizier's Disqualified Champions ********\n");

        // Filter and collect names of disqualified champions
        String disqualifiedChampions = champions.values().stream()
                .filter(champion -> champion.getState() == ChampionState.DISQUALIFIED)
                .map(Champion::getName)
                .collect(Collectors.joining(", "));

        if (disqualifiedChampions.isEmpty()) {
            sb.append("No disqualified champions");
        } else {
            sb.append(disqualifiedChampions);
        }

        return sb.toString();



    }

//**********************Challenges************************* 

    /**
     * returns true if the number represents a challenge
     *
     * @param num is the  number of the challenge
     * @returns true if the  number represents a challenge
     **/
    public boolean isChallenge(int num) {
        return challenges.containsKey(num);
    }

    /**
     * Provides a String representation of an challenge given by
     * the challenge number
     *
     * @param num the number of the challenge
     * @return returns a String representation of a challenge given by
     * the challenge number
     **/
    public String getChallenge(int num) {


        if (challenges.containsKey(num)) {
            Challenge challenge = challenges.get(num);
            return formatChallengeDetails(challenge);
        } else {
            return "\nNo such challenge";
        }
    }
    private String formatChallengeDetails(Challenge challenge) {
        // Assuming Challenge class has methods to get its details
        return "Challenge Number: " + challenge.getChallengeNo() +
                "\nType: " + challenge.getType() +
                "\nEnemy: " + challenge.getEnemy() +
                "\nSkill Required: " + challenge.getSkillRequired() +
                "\nReward: " + challenge.getReward();
    }
    /**
     * Provides a String representation of all challenges
     *
     * @return returns a String representation of all challenges
     **/
    public String getAllChallenges() {
        StringBuilder sb = new StringBuilder("\n************ All Challenges ************\n");

        if (challenges.isEmpty()) {
            sb.append("No challenges available.");
        } else {
            String allChallenges = challenges.values().stream()
                    .map(this::formatChallengeDetails)
                    .collect(Collectors.joining("\n\n"));
            sb.append(allChallenges);
        }

        return sb.toString();
    }


    /**
     * Retrieves the challenge represented by the challenge
     * number.Finds a champion from the team who can meet the
     * challenge. The results of meeting a challenge will be
     * one of the following:
     * 0 - challenge won by champion, add reward to the treasury,
     * 1 - challenge lost on skills  - deduct reward from
     * treasury and record champion as "disqualified"
     * 2 - challenge lost as no suitable champion is  available, deduct
     * the reward from treasury
     * 3 - If a challenge is lost and vizier completely defeated (no money and
     * no champions to withdraw)
     * -1 - no such challenge
     *
     * @param chalNo is the number of the challenge
     * @return an int showing the result(as above) of fighting the challenge
     */
    public int meetChallenge(int chalNo) {
        //Nothing said about accepting challenges when bust
        if (!challenges.containsKey(chalNo)) {
            return -1;
        }

        Challenge challenge = challenges.get(chalNo);
        Champion championToFight = null;

        for (Champion champ : champions.values()) {
            if (champ.isSuitableForChallenge(challenge) && champ.getState() == ChampionState.ENTERED) {
                championToFight = champ;
                break;
            }
        }
        if (championToFight == null) {
            treasury -= challenge.getReward(); // Deduct reward from treasury if no suitable champion is available
            return 2;
        }
        if (championToFight.getSkillLevel() >= challenge.getSkillRequired()) {
            treasury += challenge.getReward(); // Challenge won, add reward to treasury
            return 0;
        } else {
            treasury -= challenge.getReward();
            championToFight.setState(ChampionState.DISQUALIFIED);
            if (treasury <= 0 && !hasChampionsToWithdraw()) {
                return 3;
            }

            return 1;
        }

    }

    //****************** private methods for Task 3 functionality*******************
    //*******************************************************************************
    private void setupChampions() {

        champions.put("Ganfrank", new Wizard("Ganfrank", 7, 400, true, "Transmutation"));
        champions.put("Rudolf", new Wizard("Rudolf", 6, 400, true, "Invisibility"));
        champions.put("Neon", new Wizard("Neon", 2, 300, false, "Translocation"));
        champions.put("Krypton", new Wizard("Krypton", 8, 300, false, "Fireballs"));
        champions.put("Hedwig", new Wizard("Hedwig", 1, 400, true, "Flying"));

        champions.put("Elblond", new Warrior("Elblond", 1, 150, "Sword"));
        champions.put("Flimsi", new Warrior("Flimsi", 2, 200, "Bow"));
        champions.put("Argon", new Warrior("Argon", 9, 900, "Mace"));
        champions.put("Atlanta", new Warrior("Atlanta", 5, 500, "Sword"));

        champions.put("Darbina", new Dragon("Darbina", 7, 500, false));
        champions.put("Golum", new Dragon("Golum", 7, 500, true));
        champions.put("Xenon", new Dragon("Xenon", 7, 500, true));

    }


    private void setupChallenges() {
        challenges.put(1, new Challenge(1, ChallengeType.MAGIC, "Borg", 3, 100));
        challenges.put(2, new Challenge(2, ChallengeType.FIGHT, "Huns", 3, 120));
        challenges.put(3, new Challenge(3, ChallengeType.MYSTERY, "Ferengi", 3, 150));
        challenges.put(4, new Challenge(4, ChallengeType.MAGIC, "Vandal", 9, 200));
        challenges.put(5, new Challenge(5, ChallengeType.MYSTERY, "Borg", 7, 90));
        challenges.put(6, new Challenge(6, ChallengeType.FIGHT, "Goth", 8, 45));
        challenges.put(7, new Challenge(7, ChallengeType.MAGIC, "Frank", 10, 200));
        challenges.put(8, new Challenge(8, ChallengeType.FIGHT, "Sith", 10, 170));
        challenges.put(9, new Challenge(9, ChallengeType.MYSTERY, "Cardashian", 9, 300));
        challenges.put(10, new Challenge(10, ChallengeType.FIGHT, "Jute", 2, 300));
        challenges.put(11, new Challenge(11, ChallengeType.MAGIC, "Celt", 2, 250));
        challenges.put(12, new Challenge(12, ChallengeType.MYSTERY, "Celt", 1, 250));
    }


    private boolean hasChampionsToWithdraw() {
        return champions.values().stream().anyMatch(champ -> champ.getState() == ChampionState.ENTERED);

    }

    // Possible useful private methods
//     private Challenge getAChallenge(int no)
//     {
//         
//         return null;
//     }
//    
//     private Champion getChampionForChallenge(Challenge chal)
//     {
//         
//         return null;
//     }

    //*******************************************************************************
    //*******************************************************************************

    /************************ Task 3.5 ************************************************/

    // ***************   file write/read  *********************

    /**
     * reads challenges from a comma-separated textfile and stores in the game
     *
     * @param filename of the comma-separated textfile storing information about challenges
     */
    public void readChallenges(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Challenge challenge = parseLineToChallenge(line);
                if (challenge != null) {
                    this.challenges.put(challenge.getChallengeNo(), challenge);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading challenges file: " + e.getMessage());
        }
    }

    private Challenge parseLineToChallenge(String line) {
        try {
            String[] parts = line.split(",");
            int challengeNo = Integer.parseInt(parts[0].trim());
            ChallengeType type = ChallengeType.valueOf(parts[1].trim().toUpperCase());
            String enemy = parts[2].trim();
            int skillRequired = Integer.parseInt(parts[3].trim());
            int reward = Integer.parseInt(parts[4].trim());
            // Assuming Challenge constructor matches this order
            return new Challenge(challengeNo,type, enemy, skillRequired, reward);
        } catch (Exception e) {
            System.err.println("Error parsing challenge line: " + line);
            return null;
        }
    }

    /**
     * reads all information about the game from the specified file
     * and returns a CARE reference to a Tournament object, or null
     *
     * @param fname name of file storing the game
     * @return the game (as a Tournament object)
     */
    public Tournament loadGame(String fname) {   // uses object serialisation
        Tournament loadedTournament = null;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fname))) {
            loadedTournament = (Tournament) in.readObject();
        } catch (IOException e) {
            System.err.println("Error occurred while loading game: " + e.getMessage());
            // Handle IOException - log the error or take further actions
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found during loading game: " + e.getMessage());
            // This exception is thrown if the class of a serialized object cannot be found
        }
        return loadedTournament;
    }

    /**
     * Writes whole game to the specified file
     *
     * @param fname name of file storing requests
     */
    public void saveGame(String fname) {
        // uses object serialisation 
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fname))) {
            out.writeObject(this);
        } catch (IOException e) {
            // Handle the IOException
            System.err.println("Error occurred while saving game: " + e.getMessage());
            // You can log the exception or take further actions as needed
        }
    }



}






