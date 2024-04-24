package cwk4;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

/**
 * Provide a GUI interface for the game
 * 
 * @author A.A.Marczyk
 * @version 20/01/24
 */
public class GameGUI 
{
    private CARE gp = new Tournament("Fred");
    private JFrame myFrame = new JFrame("Game GUI");
    private JTextArea listing = new JTextArea();
    private JLabel codeLabel = new JLabel ();
    private JButton meetBtn = new JButton("Meet Challenge");
    private JButton viewBtn = new JButton("View State");
    private JButton clearBtn = new JButton("Clear");
    private JButton quitBtn = new JButton("Quit");
    private JPanel eastPanel = new JPanel();

    public static void main(String[] args)
    {
        new GameGUI();
    }
    
    public GameGUI()
    {
        makeFrame();
        makeMenuBar();

    }
    

    /**
     * Create the Swing frame and its content.
     */
    private void makeFrame()
    {
        myFrame.setLayout(new BorderLayout());
        myFrame.add(listing, BorderLayout.CENTER);
        listing.setVisible(false);
        myFrame.add(eastPanel, BorderLayout.EAST);
        eastPanel.setLayout(new GridLayout(4, 1));
        eastPanel.add(meetBtn);
        eastPanel.add(viewBtn);
        eastPanel.add(clearBtn);
        eastPanel.add(quitBtn);

        clearBtn.addActionListener(e -> listing.setText(""));
        meetBtn.addActionListener(e -> meetChallenge());
        quitBtn.addActionListener(e -> System.exit(0));
        viewBtn.addActionListener(e -> viewState());

        myFrame.pack();
        myFrame.setVisible(true);
    }
    
    /**
     * Create the main frame's menu bar.
     */
    private void makeMenuBar()
    {
        JMenuBar menubar = new JMenuBar();
        myFrame.setJMenuBar(menubar);

        // Champions Menu
        JMenu championMenu = new JMenu("Champions");
        menubar.add(championMenu);

        JMenuItem listTeamItem = new JMenuItem("List Team");
        listTeamItem.addActionListener(e -> listTeam());
        championMenu.add(listTeamItem);

        JMenuItem viewChampionItem = new JMenuItem("View Champion");
        viewChampionItem.addActionListener(e -> viewChampion());
        championMenu.add(viewChampionItem);

        JMenuItem enterChampionItem = new JMenuItem("Enter Champion");
        enterChampionItem.addActionListener(e -> enterChampion());
        championMenu.add(enterChampionItem);

        JMenuItem listAllChampionsItem = new JMenuItem("List All Reserve Champions");
        listAllChampionsItem.addActionListener(e -> listAllReserveChampions());
        championMenu.add(listAllChampionsItem);

        // Challenges Menu
        JMenu challengesMenu = new JMenu("Challenges");
        JMenuItem listAllChallengesItem = new JMenuItem("List All Challenges");
        listAllChallengesItem.addActionListener(e -> listAllChallenges());
        challengesMenu.add(listAllChallengesItem);
        menubar.add(challengesMenu);

 
    }

    private void addChampionMenuItems() {
        JMenu championMenu = new JMenu("Champions");
        JMenuItem listTeamItem = new JMenuItem("List Team");
        listTeamItem.addActionListener(e -> listTeam());
        championMenu.add(listTeamItem);

        JMenuItem viewChampionItem = new JMenuItem("View Champion");
        viewChampionItem.addActionListener(e -> viewChampion());
        championMenu.add(viewChampionItem);

        JMenuItem enterChampionItem = new JMenuItem("Enter Champion");
        enterChampionItem.addActionListener(e -> enterChampion());
        championMenu.add(enterChampionItem);

        myFrame.getJMenuBar().add(championMenu);
    }

    private void addChallengesMenu() {
        JMenu challengesMenu = new JMenu("Challenges");
        JMenuItem listAllChallengesItem = new JMenuItem("List All Challenges");
        listAllChallengesItem.addActionListener(e -> listAllChallenges());
        challengesMenu.add(listAllChallengesItem);

        myFrame.getJMenuBar().add(challengesMenu);
    }

    private void addViewStateButton() {
        viewBtn.addActionListener(e -> viewState());
        eastPanel.add(viewBtn);
    }

    private void listTeam() {
        String teamDetails = gp.getTeam(); // Call the method to get team details
        listing.setText(teamDetails);      // Display the details in the JTextArea
        listing.setVisible(true);          // Make sure the JTextArea is visible
    }
    private void viewChampion() {
        String championName = JOptionPane.showInputDialog(myFrame, "Enter the name of the champion:");
        if (championName != null && !championName.trim().isEmpty()) {
            String championDetails = gp.getChampionDetails(championName.trim());
            JOptionPane.showMessageDialog(myFrame, championDetails, "Champion Details", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(myFrame, "No champion name entered", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void enterChampion() {
        String championName = JOptionPane.showInputDialog(myFrame, "Enter the name of the champion to enter:");
        if (championName != null && !championName.trim().isEmpty()) {
            int result = gp.enterChampion(championName.trim());
            String message;
            switch (result) {
                case 0:
                    message = "Champion entered successfully.";
                    break;
                case 1:
                    message = "Champion is not in reserve.";
                    break;
                case 2:
                    message = "Not enough money in the treasury.";
                    break;
                case -1:
                    message = "No such champion found.";
                    break;
                default:
                    message = "An unknown error occurred.";
                    break;
            }
            JOptionPane.showMessageDialog(myFrame, message);
        } else {
            JOptionPane.showMessageDialog(myFrame, "No champion name entered", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listAllChallenges() {
        String challenges = gp.getAllChallenges();
        listing.setText(challenges);
        listing.setVisible(true);
    }
    private void viewState() {
        String gameState = gp.toString(); // Call the toString method to get the state of the game
        listing.setText(gameState);       // Display the state in the JTextArea
        listing.setVisible(true);         // Make sure the JTextArea is visible
    }

    private void meetChallenge() {
        String inputValue = JOptionPane.showInputDialog("Challenge number ?: ");
        try {
            int num = Integer.parseInt(inputValue);
            int result = gp.meetChallenge(num);
            String answer;
            switch (result) {
                case 0:
                    answer = "challenge won by champion";
                    break;
                case 1:
                    answer = "challenge lost on skills, champion disqualified";
                    break;
                case 2:
                    answer = "challenge lost as no suitable champion is available";
                    break;
                case 3:
                    answer = "challenge lost and vizier completely defeated";
                    break;
                default:
                    answer = "no such challenge";
                    break;
            }
            JOptionPane.showMessageDialog(myFrame, answer);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(myFrame, "Invalid input: Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listAllReserveChampions() {
        String allReserveChampions = gp.getReserve(); // Call the method to get all reserve champions
        listing.setText(allReserveChampions);         // Display the champions in the JTextArea
        listing.setVisible(true);                     // Make sure the JTextArea is visible
    }

}
   
