// Class which contains the information about a match

package sample;

public class Match {
    private Team one;
    private Team two;
    private String nameOne;
    private String nameTwo;
    private int table;
    private String pointsOne = "0";
    private String pointsTwo = "0";
    private int winsOne;
    private int winsTwo;
    private boolean checked = false;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Team getOne() {
        return one;
    }

    public void setOne(Team one) {
        this.one = one;
    }

    public Team getTwo() {
        return two;
    }

    public void setTwo(Team two) {
        this.two = two;
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

    public int getWinsOne() {
        return winsOne;
    }

    public void setWinsOne(int winsOne) {
        this.winsOne = winsOne;
    }

    public int getWinsTwo() {
        return winsTwo;
    }

    public void setWinsTwo(int winsTwo) {
        this.winsTwo = winsTwo;
    }

    public String getNameOne() {
        return nameOne;
    }

    public void setNameOne(String nameOne) {
        this.nameOne = nameOne;
    }

    public String getNameTwo() {
        return nameTwo;
    }

    public void setNameTwo(String nameTwo) {
        this.nameTwo = nameTwo;
    }

    public int getTable() {
        return table;
    }

    public void setTable(int table) {
        this.table = table;
    }

    public Match(int tableNummer, Team one, Team two) {
        this.one = one;
        this.two = two;
        this.table = tableNummer;
        this.nameOne = one.getName();
        this.nameTwo = two.getName();
    }

    public Match(int tableNummer, String nameOne, String nameTwo) {
        this.one = one;
        this.two = two;
        this.table = tableNummer;
        this.nameOne = one.getName();
        this.nameTwo = two.getName();
    }
}
