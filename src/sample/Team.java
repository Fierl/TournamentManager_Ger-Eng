// Class which contains the information about a team

package sample;

import java.util.LinkedList;

public class Team {
    private String name;
    private int number;
    private int matches = 0;
    private String pointsOne = "";
    private String pointsTwo = "";
    private String pointsThree = "";
    private int  overallPoints = 0;
    private int points = 0;
    private int wins = 0;
    private int loses = 0;
    private LinkedList<Team> playedAgainst = new LinkedList<>();

    public void setNewPoints(int newPoints, int oldPoints){
        overallPoints -= oldPoints;
        overallPoints += newPoints;
    }

    public void computeWinsAndPoints(){
        wins = 0;
        points = 0;

        //System.out.println(pointsOne);
        //System.out.println(pointsTwo);
        //System.out.println(pointsThree);

        if(!pointsOne.equals("") && !pointsOne.equals("0")) {
            int winNumber = Integer.parseInt(pointsOne.substring(0, 1));
            wins += winNumber;
            int pointsNumber = Integer.parseInt(pointsOne.substring(2));
            points += pointsNumber;
        }

        if(!pointsTwo.equals("") && !pointsTwo.equals("0")) {
            int winNumber = Integer.parseInt(pointsTwo.substring(0, 1));
            wins += winNumber;
            int pointsNumber = Integer.parseInt(pointsTwo.substring(2));
            points += pointsNumber;
        }

        if(!pointsThree.equals("") && !pointsThree.equals("0"))  {
            int winNumber = Integer.parseInt(pointsThree.substring(0, 1));
            wins += winNumber;
            int pointsNumber = Integer.parseInt(pointsThree.substring(2));
            points += pointsNumber;
        }
        //System.out.println("wins " + wins + " points " + points);

    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setPointsThree(String pointsThree) {
        this.pointsThree = pointsThree;
    }

    public String getPointsOne() {
        return pointsOne;
    }

    public void setPointsOne(String pointsOne) {
        this.pointsOne = pointsOne;
    }

    public String getPointsTwo() {
        return pointsTwo;
    }

    public void setPointsTwo(String pointsTwo) {
        this.pointsTwo = pointsTwo;
    }

    public String getPointsThree() {
        return pointsThree;
    }

    public int getOverallPoints() {
        return overallPoints;
    }

    public void setOverallPoints(int overallPoints) {
        this.overallPoints = overallPoints;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMatches() {
        return matches;
    }

    public void setMatches(int matches) {
        this.matches = matches;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public LinkedList<Team> getPlayedAgainst() {
        return playedAgainst;
    }

    public void setPlayedAgainst(LinkedList<Team> playedAgainst) {
        this.playedAgainst = playedAgainst;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLoses() {
        return loses;
    }

    public void setLoses(int loses) {
        this.loses = loses;
    }

    public void addEnemy(Team t){
        playedAgainst.add(t);
    }

    public Team(String name, int number) {
        this.name = name;
        this.number = number;
    }
}
