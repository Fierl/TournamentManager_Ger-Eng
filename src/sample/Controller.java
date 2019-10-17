// Class which contains the controller

package sample;

import javafx.application.Application;
import javafx.event.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.awt.Robot;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.URL;
import java.sql.Time;
import java.util.*;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;

public class Controller extends Application {

    // elements for the start window
    TableView teams;
    TextField addText;
    Button add;
    Button delete;
    Button start;
    Button load;
    Button save;
    Button back;

    // elements for the tournament window
    TableView matches;
    Button gameOne;
    Button gameTwo;
    Button overall;
    Button gameThree;


    // elements for the match window
    Text nameOne;
    Text nameTwo;
    Text winsOne;
    Text winsTwo;
    Text pointsOne;
    Text pointsTwo;
    Button pointPlusOne;
    Button pointMinusOne;
    Button pointPlusTwo;
    Button pointMinusTwo;
    Button winPlusOne;
    Button winMinusOne;
    Button winPlusTwo;
    Button winMinusTwo;
    Button finish;
    String input = "";

    // elements for the league window
    TableView league;
    Button backTournament;

    //List of all teams/matches
    LinkedList<Team> teamList = new LinkedList<>();
    LinkedList<Match> matchOneList = new LinkedList<>();
    LinkedList<Match> matchTwoList = new LinkedList<>();
    LinkedList<Match> matchThreeList = new LinkedList<>();
    
    // help parameters for the different matches
    int number = 1;
    int gameNumber = 1;
    boolean gotWarned = false;
    int tableNummer = 1;

    public static void main(String[] args) {
        launch(args);
    }

    public void back(Stage primaryStage) throws Exception{
        start(primaryStage);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("StartWindow.fxml"));
        primaryStage.setTitle("Tournament Manager");
        primaryStage.setScene(new Scene(root, 1024, 768));
        Scene scene = root.getScene();
        scene.getStylesheets().add(getClass().getResource("turnier.css").toExternalForm());
        primaryStage.show();
        setupStart(scene, primaryStage);
    }
    
    //connects the elements of the startWindow.fxml to java
    public void setupStart(Scene scene, Stage primaryStage){
        teams = (TableView) scene.lookup("#teamlist");
        teams.setEditable(true);
        TableColumn name = new TableColumn("Name");
        TableColumn number = new TableColumn("Nummer");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        number.setCellValueFactory(new PropertyValueFactory<>("number"));
        name.prefWidthProperty().bind(teams.widthProperty());
        number.prefWidthProperty().bind(teams.widthProperty().divide(5));
        teams.getColumns().addAll(number, name);
        teams.getItems().addAll(teamList);
        addText = (TextField) scene.lookup("#textbox");
        add = (Button) scene.lookup("#add");
        add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addTeam();
            }
        });
        delete = (Button) scene.lookup("#delete");
        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                deleteTeam();
            }
        });
        start = (Button) scene.lookup("#start");
        start.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    saveGame();
                    startTournament(primaryStage);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });
        load = (Button) scene.lookup("#load");
        load.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadGame();
                try {
                    startTournamentWithLoad(primaryStage);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });
    }

    public void addTeam() {
        String teamname = addText.getText();
        if(!teamname.equals("")) {
            Team t = new Team(teamname, number);
            //System.out.println(t.getNumber());
            number++;
            teamList.add(t);
            teams.getItems().add(t);
            addText.setText("");
        }
    }

    public void deleteTeam(){
        Team t = (Team) teams.getSelectionModel().getSelectedItem();
        teams.getItems().remove(t);
        teamList.remove(t);
    }

    // help function for getting the team to delete
    private Team getTeam(LinkedList<Match> matchList, int matchNumber, int teamNumber){
        if (teamNumber == 1) {
            return matchList.get(matchNumber).getOne();
        } else {
            return  matchList.get(matchNumber).getTwo();
        }
    }
    
    // connects the elements of the tornamentWindow.fxml to java and fills the table with functionalities
    public void startTournament(Stage primaryStage) throws Exception{
    	
    	//checks if the number of teams is even
        if (teamList.size() % 2 != 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Fehler: Ungerade Zahl an Teams");
            alert.showAndWait();
        } else {
            Parent root = FXMLLoader.load(getClass().getResource("/sample/TournamentWindow.fxml"));
            primaryStage.getScene().setRoot(root);
            Scene scene = root.getScene();
            primaryStage.show();

            league = (TableView) scene.lookup("#teamList");
            league.setEditable(true);
            TableColumn name = new TableColumn("Name");
            TableColumn points = new TableColumn("Punkte");
            TableColumn wins = new TableColumn("Siege");
            name.setCellValueFactory(new PropertyValueFactory<>("name"));
            points.setCellValueFactory(new PropertyValueFactory<>("points"));
            wins.setCellValueFactory(new PropertyValueFactory<>("wins"));
            name.prefWidthProperty().bind(league.widthProperty().divide(3));
            points.prefWidthProperty().bind(league.widthProperty().divide(3));
            wins.prefWidthProperty().bind(league.widthProperty().divide(3));
            //points.getSortOrder(points);
            //points.setSortType(TableColumn.SortType.DESCENDING);
            league.getSortOrder().add(points);
            league.getColumns().addAll(name, points, wins);
            league.getItems().addAll(teamList);
            league.setVisible(false);

            matches = (TableView) scene.lookup("#matchList");
            matches.setEditable(true);
            matches.setOnMousePressed(
                    new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            try {
                                Robot r = new Robot();
                                int keyCode = KeyEvent.VK_ENTER;
                                r.keyPress(keyCode);
                            } catch (Exception e){
                                System.out.println("No enter pressing possible.");
                            }
                        }
                    }
            );
            TableColumn teamOne = new TableColumn("Team 1");
            TableColumn pointsOne = new TableColumn("Punkte 1");
            TableColumn pointsTwo = new TableColumn("Punkte 2");
            TableColumn teamTwo = new TableColumn("Team 2");
            TableColumn tableNummer = new TableColumn("Tisch");
            teamOne.setCellValueFactory(new PropertyValueFactory<>("nameOne"));
            teamTwo.setCellValueFactory(new PropertyValueFactory<>("nameTwo"));
            tableNummer.setCellValueFactory(new PropertyValueFactory<>("table"));
            pointsOne.setCellValueFactory(new PropertyValueFactory<>("pointsOne"));
            pointsOne.prefWidthProperty().bind(matches.widthProperty().divide(6));
            pointsTwo.prefWidthProperty().bind(matches.widthProperty().divide(6));
            pointsOne.setEditable(true);
            pointsOne.setCellFactory(TextFieldTableCell.forTableColumn()); //Makes the columns themselves editabl
            pointsOne.setOnEditCommit(
                    new EventHandler<CellEditEvent<TableView, String>>() {
                        @Override
                        public void handle(CellEditEvent<TableView, String> t) {
                            //System.out.println(gameNumber);
                            try {
                                Integer.parseInt(t.getNewValue());
                                if(t.getNewValue().length() == 4) {
                                    if (gameNumber == 1) {
                                        Team team = getTeam(matchOneList, t.getTablePosition().getRow(), 1);
                                        //System.out.println("new value" + t.getNewValue() + " old value " + t.getOldValue());
                                        team.setNewPoints(Integer.parseInt(t.getNewValue()), Integer.parseInt(t.getOldValue()));
                                        //System.out.println(team.getOverallPoints());
                                        matchOneList.get(t.getTablePosition().getRow()).setPointsOne(t.getNewValue());
                                        team.setPointsOne(t.getNewValue());
                                        //System.out.println("Teamname " + team.getName());
                                        team.computeWinsAndPoints();
                                    } else if (gameNumber == 2) {
                                        Team team = getTeam(matchTwoList, t.getTablePosition().getRow(), 1);
                                        //System.out.println("new value" + t.getNewValue() + " old value " + t.getOldValue());
                                        team.setNewPoints(Integer.parseInt(t.getNewValue()), Integer.parseInt(t.getOldValue()));
                                        //System.out.println(team.getOverallPoints());
                                        team.setPointsTwo(t.getNewValue());
                                        matchTwoList.get(t.getTablePosition().getRow()).setPointsOne(t.getNewValue());
                                        //System.out.println("Teamname " + team.getName());
                                        team.computeWinsAndPoints();
                                    } else {
                                        Team team = getTeam(matchThreeList, t.getTablePosition().getRow(), 1);
                                        //System.out.println("new value" + t.getNewValue() + " old value " + t.getOldValue());
                                        team.setNewPoints(Integer.parseInt(t.getNewValue()), Integer.parseInt(t.getOldValue()));
                                        team.setPointsThree(t.getNewValue());
                                        matchThreeList.get(t.getTablePosition().getRow()).setPointsOne(t.getNewValue());
                                        //System.out.println("Teamname " + team.getName());
                                        team.computeWinsAndPoints();
                                    }
                                    saveGame();
                                } else {
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setHeaderText("Fehler: Keine Zahl oder Zahl hat nicht gewuenschtes Format (S0PP)");
                                    alert.showAndWait();
                                    if (gameNumber == 1){
                                        matches.getItems().clear();
                                        displayMatches(matchOneList);
                                    } else if (gameNumber == 2){
                                        matches.getItems().clear();
                                        displayMatches(matchTwoList);
                                    } else{
                                        matches.getItems().clear();
                                        displayMatches(matchThreeList);
                                    }
                                }
                            } catch (Exception e){
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setHeaderText("Fehler: Keine Zahl oder Zahl hat nicht gewuenschtes Format (S0PP)");
                                alert.showAndWait();
                                if (gameNumber == 1){
                                    matches.getItems().clear();
                                    displayMatches(matchOneList);
                                } else if (gameNumber == 2){
                                    matches.getItems().clear();
                                    displayMatches(matchTwoList);
                                } else{
                                    matches.getItems().clear();
                                    displayMatches(matchThreeList);
                                }
                            }
                        }
                    }
            );
            pointsTwo.setCellValueFactory(new PropertyValueFactory<>("pointsTwo"));
            pointsTwo.setEditable(true);
            pointsTwo.setCellFactory(TextFieldTableCell.forTableColumn()); //Makes the columns themselves editable
            pointsTwo.setOnEditCommit(
                    new EventHandler<CellEditEvent<TableView, String>>() {
                        @Override
                        public void handle(CellEditEvent<TableView, String> t) {
                            //System.out.println(gameNumber);
                            try {
                                Integer.parseInt(t.getNewValue());
                                if(t.getNewValue().length() == 4) {
                                    if (gameNumber == 1) {
                                        Team team = getTeam(matchOneList, t.getTablePosition().getRow(), 2);
                                        //System.out.println("new value" + t.getNewValue() + " old value " + t.getOldValue());
                                        team.setNewPoints(Integer.parseInt(t.getNewValue()), Integer.parseInt(t.getOldValue()));
                                        //System.out.println(team.getOverallPoints());
                                        team.setPointsOne(t.getNewValue());
                                        matchOneList.get(t.getTablePosition().getRow()).setPointsTwo(t.getNewValue());
                                        //System.out.println("Teamname " + team.getName());
                                        team.computeWinsAndPoints();
                                    } else if (gameNumber == 2) {
                                        Team team = getTeam(matchTwoList, t.getTablePosition().getRow(), 2);
                                        //System.out.println("new value" + t.getNewValue() + " old value " + t.getOldValue());
                                        team.setNewPoints(Integer.parseInt(t.getNewValue()), Integer.parseInt(t.getOldValue()));
                                        //System.out.println(team.getOverallPoints());
                                        team.setPointsTwo(t.getNewValue());
                                        matchTwoList.get(t.getTablePosition().getRow()).setPointsTwo(t.getNewValue());
                                        //System.out.println("Teamname " + team.getName());
                                        team.computeWinsAndPoints();
                                    } else {
                                        Team team = getTeam(matchThreeList, t.getTablePosition().getRow(), 2);
                                        //System.out.println("new value" + t.getNewValue() + " old value " + t.getOldValue());
                                        team.setNewPoints(Integer.parseInt(t.getNewValue()), Integer.parseInt(t.getOldValue()));
                                        //System.out.println(team.getOverallPoints());
                                        team.setPointsThree(t.getNewValue());
                                        matchThreeList.get(t.getTablePosition().getRow()).setPointsTwo(t.getNewValue());
                                        //System.out.println("Teamname " + team.getName());
                                        team.computeWinsAndPoints();
                                    }
                                    saveGame();
                                } else{
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setHeaderText("Fehler: Keine Zahl oder Zahl hat nicht gewuenschtes Format (S0PP)");
                                    alert.showAndWait();
                                    if (gameNumber == 1){
                                        matches.getItems().clear();
                                        displayMatches(matchOneList);
                                    } else if (gameNumber == 2){
                                        matches.getItems().clear();
                                        displayMatches(matchTwoList);
                                    } else{
                                        matches.getItems().clear();
                                        displayMatches(matchThreeList);
                                    }
                                }
                            } catch (Exception e){
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setHeaderText("Fehler: Keine Zahl oder Zahl hat nicht gewuenschtes Format (S0PP)");
                                alert.showAndWait();
                                if (gameNumber == 1){
                                    matches.getItems().clear();
                                    displayMatches(matchOneList);
                                } else if (gameNumber == 2){
                                    matches.getItems().clear();
                                    displayMatches(matchTwoList);
                                } else{
                                    matches.getItems().clear();
                                    displayMatches(matchThreeList);
                                }
                            }
                        }
                    }
            );
            teamOne.prefWidthProperty().setValue(250);
            teamTwo.prefWidthProperty().setValue(250);
            tableNummer.prefWidthProperty().bind(teams.widthProperty().divide(6));
            matches.getColumns().addAll(tableNummer, teamOne, pointsOne, pointsTwo, teamTwo);
            pointsTwo.setEditable(true);
            matches.setEditable(true);
            shuffleMatches(matchOneList);
            shuffleMatches(matchTwoList);
            shuffleMatches(matchThreeList);
            overall = (Button) scene.lookup("#overall");
            overall.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try{
                        matches.setVisible(false);
                        league.setVisible(true);
                        league.getItems().clear();
                        for (int i = 0; i < teamList.size(); i++){
                            Team t = teamList.get(i);
                            System.out.println(t.getPointsOne() + " " + t.getPointsTwo() + " " + t.getPointsThree());
                            t.computeWinsAndPoints();
                        }
                        league.getItems().addAll(teamList);
                    } catch (Exception e){
                        System.out.println(e);
                    }
                }
            });
            gameOne = (Button) scene.lookup("#gameOne");
            gameOne.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try{
                        matches.setVisible(true);
                        league.setVisible(false);
                        matches.getItems().clear();
                        displayMatches(matchOneList);
                        gameNumber = 1;
                    } catch (Exception e){
                        System.out.println(e);
                    }
                }
            });
            //displayMatches(matchOneList);
            gameTwo = (Button) scene.lookup("#gameTwo");
            gameTwo.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try{
                        matches.setVisible(true);
                        league.setVisible(false);
                        matches.getItems().clear();
                        displayMatches(matchTwoList);
                        gameNumber = 2;
                    } catch (Exception e){
                        System.out.println(e);
                    }
                }
            });
            gameThree = (Button) scene.lookup("#gameThree");
            gameThree.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try{
                        matches.setVisible(true);
                        league.setVisible(false);
                        matches.getItems().clear();
                        displayMatches(matchThreeList);
                        gameNumber = 3;
                    } catch (Exception e){
                        System.out.println(e);
                    }
                }
            });
            save = (Button) scene.lookup("#save");
            save.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    loadGame();
                    try {
                        save();
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
            });
            back = (Button) scene.lookup("#back");
            back.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        back(primaryStage);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
            });
            gameOne.fire();
        }
    }

    public void startTournamentWithLoad(Stage primaryStage) throws Exception{
            Parent root = FXMLLoader.load(getClass().getResource("/sample/TournamentWindow.fxml"));
            primaryStage.getScene().setRoot(root);
            Scene scene = root.getScene();
            primaryStage.show();

            league = (TableView) scene.lookup("#teamList");
            league.setEditable(true);
            TableColumn name = new TableColumn("Name");
            TableColumn points = new TableColumn("Punkte");
            TableColumn wins = new TableColumn("Siege");
            name.setCellValueFactory(new PropertyValueFactory<>("name"));
            points.setCellValueFactory(new PropertyValueFactory<>("points"));
            wins.setCellValueFactory(new PropertyValueFactory<>("wins"));
            name.prefWidthProperty().bind(league.widthProperty().divide(3));
            points.prefWidthProperty().bind(league.widthProperty().divide(3));
            wins.prefWidthProperty().bind(league.widthProperty().divide(3));

            league.getSortOrder().add(points);
            league.getColumns().addAll(name, points, wins);
            league.getItems().addAll(teamList);
            league.setVisible(false);

            matches = (TableView) scene.lookup("#matchList");
            matches.setEditable(true);
            matches.setOnMousePressed(
                    new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            try {
                                Robot r = new Robot();
                                int keyCode = KeyEvent.VK_ENTER;
                                r.keyPress(keyCode);
                            } catch (Exception e){
                                System.out.println("No enter pressing possible.");
                            }
                        }
                    }
            );
            TableColumn teamOne = new TableColumn("Team 1");
            TableColumn pointsOne = new TableColumn("Punkte 1");
            TableColumn pointsTwo = new TableColumn("Punkte 2");
            TableColumn teamTwo = new TableColumn("Team 2");
            TableColumn tableNummer = new TableColumn("Tisch");
            teamOne.setCellValueFactory(new PropertyValueFactory<>("nameOne"));
            teamTwo.setCellValueFactory(new PropertyValueFactory<>("nameTwo"));
            tableNummer.setCellValueFactory(new PropertyValueFactory<>("table"));
            pointsOne.setCellValueFactory(new PropertyValueFactory<>("pointsOne"));
            pointsOne.prefWidthProperty().bind(matches.widthProperty().divide(6));
            pointsTwo.prefWidthProperty().bind(matches.widthProperty().divide(6));
            pointsOne.setEditable(true);
            pointsOne.setCellFactory(TextFieldTableCell.forTableColumn()); //Makes the columns themselves editabl
            pointsOne.setOnEditCommit(
                    new EventHandler<CellEditEvent<TableView, String>>() {
                        @Override
                        public void handle(CellEditEvent<TableView, String> t) {
                            //System.out.println(gameNumber);
                            try {
                                Integer.parseInt(t.getNewValue());
                                if(t.getNewValue().length() == 4) {
                                    if (gameNumber == 1) {
                                        Team team = getTeam(matchOneList, t.getTablePosition().getRow(), 1);
                                        //System.out.println("new value" + t.getNewValue() + " old value " + t.getOldValue());
                                        team.setNewPoints(Integer.parseInt(t.getNewValue()), Integer.parseInt(t.getOldValue()));
                                        //System.out.println(team.getOverallPoints());
                                        matchOneList.get(t.getTablePosition().getRow()).setPointsOne(t.getNewValue());
                                        team.setPointsOne(t.getNewValue());
                                        //System.out.println("Teamname " + team.getName());
                                        team.computeWinsAndPoints();
                                    } else if (gameNumber == 2) {
                                        Team team = getTeam(matchTwoList, t.getTablePosition().getRow(), 1);
                                        //System.out.println("new value" + t.getNewValue() + " old value " + t.getOldValue());
                                        team.setNewPoints(Integer.parseInt(t.getNewValue()), Integer.parseInt(t.getOldValue()));
                                        //System.out.println(team.getOverallPoints());
                                        team.setPointsTwo(t.getNewValue());
                                        matchTwoList.get(t.getTablePosition().getRow()).setPointsOne(t.getNewValue());
                                        //System.out.println("Teamname " + team.getName());
                                        team.computeWinsAndPoints();
                                    } else {
                                        Team team = getTeam(matchThreeList, t.getTablePosition().getRow(), 1);
                                        //System.out.println("new value" + t.getNewValue() + " old value " + t.getOldValue());
                                        team.setNewPoints(Integer.parseInt(t.getNewValue()), Integer.parseInt(t.getOldValue()));
                                        team.setPointsThree(t.getNewValue());
                                        matchThreeList.get(t.getTablePosition().getRow()).setPointsOne(t.getNewValue());
                                        //System.out.println("Teamname " + team.getName());
                                        team.computeWinsAndPoints();
                                    }
                                    saveGame();
                                } else {
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setHeaderText("Fehler: Keine Zahl oder Zahl hat nicht gewuenschtes Format (S0PP)");
                                    alert.showAndWait();
                                    if (gameNumber == 1){
                                        matches.getItems().clear();
                                        displayMatches(matchOneList);
                                    } else if (gameNumber == 2){
                                        matches.getItems().clear();
                                        displayMatches(matchTwoList);
                                    } else{
                                        matches.getItems().clear();
                                        displayMatches(matchThreeList);
                                    }
                                }
                            } catch (Exception e){
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setHeaderText("Fehler: Keine Zahl oder Zahl hat nicht gewuenschtes Format (S0PP)");
                                alert.showAndWait();
                                if (gameNumber == 1){
                                    matches.getItems().clear();
                                    displayMatches(matchOneList);
                                } else if (gameNumber == 2){
                                    matches.getItems().clear();
                                    displayMatches(matchTwoList);
                                } else{
                                    matches.getItems().clear();
                                    displayMatches(matchThreeList);
                                }
                            }
                        }
                    }
            );
            pointsTwo.setCellValueFactory(new PropertyValueFactory<>("pointsTwo"));
            pointsTwo.setEditable(true);
            pointsTwo.setCellFactory(TextFieldTableCell.forTableColumn()); //Makes the columns themselves editable
            pointsTwo.setOnEditCommit(
                    new EventHandler<CellEditEvent<TableView, String>>() {
                        @Override
                        public void handle(CellEditEvent<TableView, String> t) {
                            //System.out.println(gameNumber);
                            try {
                                Integer.parseInt(t.getNewValue());
                                if(t.getNewValue().length() == 4) {
                                    if (gameNumber == 1) {
                                        Team team = getTeam(matchOneList, t.getTablePosition().getRow(), 2);
                                        //System.out.println("new value" + t.getNewValue() + " old value " + t.getOldValue());
                                        team.setNewPoints(Integer.parseInt(t.getNewValue()), Integer.parseInt(t.getOldValue()));
                                        //System.out.println(team.getOverallPoints());
                                        team.setPointsOne(t.getNewValue());
                                        matchOneList.get(t.getTablePosition().getRow()).setPointsTwo(t.getNewValue());
                                        //System.out.println("Teamname " + team.getName());
                                        team.computeWinsAndPoints();
                                    } else if (gameNumber == 2) {
                                        Team team = getTeam(matchTwoList, t.getTablePosition().getRow(), 2);
                                        //System.out.println("new value" + t.getNewValue() + " old value " + t.getOldValue());
                                        team.setNewPoints(Integer.parseInt(t.getNewValue()), Integer.parseInt(t.getOldValue()));
                                        //System.out.println(team.getOverallPoints());
                                        team.setPointsTwo(t.getNewValue());
                                        matchTwoList.get(t.getTablePosition().getRow()).setPointsTwo(t.getNewValue());
                                        //System.out.println("Teamname " + team.getName());
                                        team.computeWinsAndPoints();
                                    } else {
                                        Team team = getTeam(matchThreeList, t.getTablePosition().getRow(), 2);
                                        //System.out.println("new value" + t.getNewValue() + " old value " + t.getOldValue());
                                        team.setNewPoints(Integer.parseInt(t.getNewValue()), Integer.parseInt(t.getOldValue()));
                                        //System.out.println(team.getOverallPoints());
                                        team.setPointsThree(t.getNewValue());
                                        matchThreeList.get(t.getTablePosition().getRow()).setPointsTwo(t.getNewValue());
                                        //System.out.println("Teamname " + team.getName());
                                        team.computeWinsAndPoints();
                                    }
                                    saveGame();
                                } else{
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setHeaderText("Fehler: Keine Zahl oder Zahl hat nicht gewuenschtes Format (S0PP)");
                                    alert.showAndWait();
                                    if (gameNumber == 1){
                                        matches.getItems().clear();
                                        displayMatches(matchOneList);
                                    } else if (gameNumber == 2){
                                        matches.getItems().clear();
                                        displayMatches(matchTwoList);
                                    } else{
                                        matches.getItems().clear();
                                        displayMatches(matchThreeList);
                                    }
                                }
                            } catch (Exception e){
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setHeaderText("Fehler: Keine Zahl oder Zahl hat nicht gewuenschtes Format (S0PP)");
                                alert.showAndWait();
                                if (gameNumber == 1){
                                    matches.getItems().clear();
                                    displayMatches(matchOneList);
                                } else if (gameNumber == 2){
                                    matches.getItems().clear();
                                    displayMatches(matchTwoList);
                                } else{
                                    matches.getItems().clear();
                                    displayMatches(matchThreeList);
                                }
                            }
                        }
                    }
            );
            teamOne.prefWidthProperty().setValue(250);
            teamTwo.prefWidthProperty().setValue(250);
            tableNummer.prefWidthProperty().bind(teams.widthProperty().divide(6));
            matches.getColumns().addAll(tableNummer, teamOne, pointsOne, pointsTwo, teamTwo);
            pointsTwo.setEditable(true);
            matches.setEditable(true);
            overall = (Button) scene.lookup("#overall");
            overall.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try{
                        matches.setVisible(false);
                        league.setVisible(true);
                        league.getItems().clear();
                        for (int i = 0; i < teamList.size(); i++){
                            Team t = teamList.get(i);
                            System.out.println(t.getPointsOne() + " " + t.getPointsTwo() + " " + t.getPointsThree());
                            t.computeWinsAndPoints();
                        }
                        league.getItems().addAll(teamList);
                    } catch (Exception e){
                        System.out.println(e);
                    }
                }
            });
            gameOne = (Button) scene.lookup("#gameOne");
            gameOne.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try{
                        matches.setVisible(true);
                        league.setVisible(false);
                        matches.getItems().clear();
                        displayMatches(matchOneList);
                        gameNumber = 1;
                    } catch (Exception e){
                        System.out.println(e);
                    }
                }
            });
            //displayMatches(matchOneList);
            gameTwo = (Button) scene.lookup("#gameTwo");
            gameTwo.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try{
                        matches.setVisible(true);
                        league.setVisible(false);
                        matches.getItems().clear();
                        displayMatches(matchTwoList);
                        gameNumber = 2;
                    } catch (Exception e){
                        System.out.println(e);
                    }
                }
            });
            gameThree = (Button) scene.lookup("#gameThree");
            gameThree.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try{
                        matches.setVisible(true);
                        league.setVisible(false);
                        matches.getItems().clear();
                        displayMatches(matchThreeList);
                        gameNumber = 3;
                    } catch (Exception e){
                        System.out.println(e);
                    }
                }
            });
        save = (Button) scene.lookup("#save");
        save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadGame();
                try {
                    save();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });
        back = (Button) scene.lookup("#back");
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    back(primaryStage);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });
        gameOne.fire();
    }

    // saves the current teams/matches into text files into a new folder
    private void save(){
        long millis = System.currentTimeMillis();
        java.util.Date date = new java.util.Date(millis);
        String time = date.toString();
        String[] timeArray = time.split(" ");
        String timeName = "";
        for(int i = 0; i < timeArray.length; i++){
            timeName += timeArray[i];
        }
        String dirName = timeName.substring(0,10);
        try {
            File dir = new File(dirName);
            dir.mkdir();
            PrintWriter writer = new PrintWriter(dirName + "/teams.txt", "UTF-8");
            LinkedList<Team> helpList = (LinkedList<Team>) teamList.clone();
            while (!helpList.isEmpty()){
                writer.println(helpList.pop().getName());
            }
            writer.close();

            if(!matchOneList.isEmpty()){
                PrintWriter writer5 = new PrintWriter(dirName + "/table.txt", "UTF-8");
                LinkedList<Team> helpList5 = (LinkedList<Team>) teamList.clone();
                Collections.sort(helpList5, new Sortbyroll() );
                while (!helpList5.isEmpty()){
                    Team t = helpList5.pop();
                    String teamName = t.getName();
                    int points = t.getPoints();
                    int wins = t.getWins();
                    String[] teamArray = teamName.split(" ");
                    String newTeamName = "";
                    for(int i = 0; i < teamArray.length; i++){
                        if(i != 0){
                            newTeamName += "_";
                        }
                        newTeamName += teamArray[i];
                    }
                    writer5.println(newTeamName + "\t" + "\t"+ wins + "\t" + "\t" + points);
                }
                writer5.close();

                PrintWriter writer2 = new PrintWriter(dirName + "/matchesOne.txt", "UTF-8");
                LinkedList<Match> helpList2 = (LinkedList<Match>) matchOneList.clone();
                while (!helpList2.isEmpty()){
                    Match match = helpList2.pop();
                    String teamOneName = match.getNameOne();
                    String teamOnePoints = match.getPointsOne();
                    String teamTwoName = match.getNameTwo();
                    String teamTwoPoints = match.getPointsTwo();
                    writer2.println(teamOneName + " " + teamOnePoints + " " + teamTwoName + " " + teamTwoPoints);
                }
                writer2.close();
            }

            if(!matchTwoList.isEmpty()){
                PrintWriter writer3 = new PrintWriter(dirName + "/matchesTwo.txt", "UTF-8");
                LinkedList<Match> helpList3 = (LinkedList<Match>) matchTwoList.clone();
                while (!helpList3.isEmpty()){
                    Match match = helpList3.pop();
                    String teamOneName = match.getNameOne();
                    String teamOnePoints = match.getPointsOne();
                    String teamTwoName = match.getNameTwo();
                    String teamTwoPoints = match.getPointsTwo();
                    writer3.println(teamOneName + " " + teamOnePoints + " " + teamTwoName + " " + teamTwoPoints);
                }
                writer3.close();
            }

            if(!matchThreeList.isEmpty()){
                PrintWriter writer4 = new PrintWriter(dirName + "/matchesThree.txt", "UTF-8");
                LinkedList<Match> helpList4 = (LinkedList<Match>) matchThreeList.clone();
                while (!helpList4.isEmpty()){
                    Match match = helpList4.pop();
                    String teamOneName = match.getNameOne();
                    String teamOnePoints = match.getPointsOne();
                    String teamTwoName = match.getNameTwo();
                    String teamTwoPoints = match.getPointsTwo();
                    System.out.println(teamOneName + " " + teamOnePoints + " " + teamTwoName + " " + teamTwoPoints);
                    writer4.println(teamOneName + " " + teamOnePoints + " " + teamTwoName + " " + teamTwoPoints);
                }
                writer4.close();
            }
        } catch (Exception e){
            System.out.println("Not saved" + e);
        }
    }

    public void displayMatches(LinkedList<Match> matchList){
        matches.getItems().addAll(matchList);
    }
    
 // saves the current teams/matches into text files
    public void saveGame(){
        try {
            PrintWriter writer = new PrintWriter("teams.txt", "UTF-8");
            LinkedList<Team> helpList = (LinkedList<Team>) teamList.clone();
            while (!helpList.isEmpty()){
                String teamName = helpList.pop().getName();
                String[] teamArray = teamName.split(" ");
                String newTeamName = "";
                for(int i = 0; i < teamArray.length; i++){
                    if(i != 0){
                        newTeamName += "_";
                    }
                    newTeamName += teamArray[i];
                }
                writer.println(newTeamName);
            }
            writer.close();

            if(!matchOneList.isEmpty()){
                PrintWriter writer5 = new PrintWriter("table.txt", "UTF-8");
                LinkedList<Team> helpList5 = (LinkedList<Team>) teamList.clone();
                Collections.sort(helpList5, new Sortbyroll() );
                while (!helpList5.isEmpty()){
                    Team t = helpList5.pop();
                    String teamName = t.getName();
                    int points = t.getPoints();
                    int wins = t.getWins();
                    String[] teamArray = teamName.split(" ");
                    String newTeamName = "";
                    for(int i = 0; i < teamArray.length; i++){
                        if(i != 0){
                            newTeamName += "_";
                        }
                        newTeamName += teamArray[i];
                    }
                    writer5.println(newTeamName + "\t" + "\t" + wins + "\t" + "\t" + points);
                }
                writer5.close();

                PrintWriter writer2 = new PrintWriter("matchesOne.txt", "UTF-8");
                LinkedList<Match> helpList2 = (LinkedList<Match>) matchOneList.clone();
                while (!helpList2.isEmpty()){
                    Match match = helpList2.pop();
                    String teamOneName = match.getNameOne();
                    String[] teamOneArray = teamOneName.split(" ");
                    String newTeamOneName = "";
                    for(int i = 0; i < teamOneArray.length; i++){
                        if(i != 0){
                            newTeamOneName += "_";
                        }
                        newTeamOneName += teamOneArray[i];
                    }
                    String teamOnePoints = match.getPointsOne();
                    String teamTwoName = match.getNameTwo();
                    String[] teamTwoArray = teamTwoName.split(" ");
                    String newTeamTwoName = "";
                    for(int i = 0; i < teamTwoArray.length; i++){
                        if(i != 0){
                            newTeamTwoName += "_";
                        }
                        newTeamTwoName += teamTwoArray[i];
                    }
                    String teamTwoPoints = match.getPointsTwo();
                    writer2.println(newTeamOneName + " " + teamOnePoints + " " + newTeamTwoName + " " + teamTwoPoints);
                }
                writer2.close();
            }

            if(!matchTwoList.isEmpty()){
                PrintWriter writer3 = new PrintWriter("matchesTwo.txt", "UTF-8");
                LinkedList<Match> helpList3 = (LinkedList<Match>) matchTwoList.clone();
                while (!helpList3.isEmpty()){
                    Match match = helpList3.pop();
                    String teamOneName = match.getNameOne();
                    String[] teamOneArray = teamOneName.split(" ");
                    String newTeamOneName = "";
                    for(int i = 0; i < teamOneArray.length; i++){
                        if(i != 0){
                            newTeamOneName += "_";
                        }
                        newTeamOneName += teamOneArray[i];
                    }
                    String teamOnePoints = match.getPointsOne();
                    String teamTwoName = match.getNameTwo();
                    String[] teamTwoArray = teamTwoName.split(" ");
                    String newTeamTwoName = "";
                    for(int i = 0; i < teamTwoArray.length; i++){
                        if(i != 0){
                            newTeamTwoName += "_";
                        }
                        newTeamTwoName += teamTwoArray[i];
                    }
                    String teamTwoPoints = match.getPointsTwo();
                    writer3.println(newTeamOneName + " " + teamOnePoints + " " + newTeamTwoName + " " + teamTwoPoints);
                }
                writer3.close();
            }

            if(!matchThreeList.isEmpty()){
                PrintWriter writer4 = new PrintWriter("matchesThree.txt", "UTF-8");
                LinkedList<Match> helpList4 = (LinkedList<Match>) matchThreeList.clone();
                while (!helpList4.isEmpty()){
                    Match match = helpList4.pop();
                    String teamOneName = match.getNameOne();
                    String[] teamOneArray = teamOneName.split(" ");
                    String newTeamOneName = "";
                    for(int i = 0; i < teamOneArray.length; i++){
                        if(i != 0){
                            newTeamOneName += "_";
                        }
                        newTeamOneName += teamOneArray[i];
                    }
                    String teamOnePoints = match.getPointsOne();
                    String teamTwoName = match.getNameTwo();
                    String[] teamTwoArray = teamTwoName.split(" ");
                    String newTeamTwoName = "";
                    for(int i = 0; i < teamTwoArray.length; i++){
                        if(i != 0){
                            newTeamTwoName += "_";
                        }
                        newTeamTwoName += teamTwoArray[i];
                    }
                    String teamTwoPoints = match.getPointsTwo();
                    writer4.println(newTeamOneName + " " + teamOnePoints + " " + newTeamTwoName + " " + teamTwoPoints);
                }
                writer4.close();
            }
        } catch (Exception e){
            System.out.println("Not saved" + e);
        }
    }
    
    // loads the teams/matches from text files inside the folder
    public void loadGame(){
        try {
            teamList.clear();
            matchOneList.clear();
            matchTwoList.clear();
            matchThreeList.clear();
            BufferedReader fr = new BufferedReader(new InputStreamReader(new FileInputStream(new File("teams.txt"))));
            String line = "";
            int count = 1;
            while ((line = fr.readLine()) != null) {
                String[] teamArray = line.split("_");
                String name = "";
                for(int i = 0; i< teamArray.length;i++){
                    if(i != 0){
                        name += " ";
                    }
                    name += teamArray[i];
                }
                Team t = new Team(name, count);
                count++;
                teamList.add(t);
            }
        }catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Keine Save-Datei gefunden! Füge teams.txt in ordner ein");
            alert.showAndWait();
        }
        try{
            BufferedReader matchOne = new BufferedReader(new InputStreamReader(new FileInputStream(new File("matchesOne.txt"))));
            BufferedReader matchTwo = new BufferedReader(new InputStreamReader(new FileInputStream(new File("matchesTwo.txt"))));
            BufferedReader matchThree = new BufferedReader(new InputStreamReader(new FileInputStream(new File("matchesThree.txt"))));
            int count = 1;

                String line = "";
                LinkedList<String> matchList = new LinkedList<>();
                while((line = matchOne.readLine()) != null){
                    matchList.add(line);
                }
                for(int i = 0; i < matchList.size(); i++){
                    System.out.println(i + " " + matchList.get(i));
                }
                while (!matchList.isEmpty()){
                    String matchString = matchList.pop();
                    String[] matchArray = matchString.split(" ");

                    String[] teamOneArray = matchArray[0].split("_");
                    String nameOne = "";
                    for(int i = 0; i< teamOneArray.length;i++){
                        if(i != 0){
                            nameOne += " ";
                        }
                        nameOne += teamOneArray[i];
                    }

                    String[] teamTwoArray = matchArray[2].split("_");
                    String nameTwo = "";
                    for(int i = 0; i< teamTwoArray.length;i++){
                        if(i != 0){
                            nameTwo += " ";
                        }
                        nameTwo += teamTwoArray[i];
                    }

                    Team teamOne = findTeam(nameOne);
                    Team teamTwo = findTeam(nameTwo);
                    Match match = new Match(count, teamOne, teamTwo);
                    match.setPointsOne(matchArray[1]);
                    match.setPointsTwo(matchArray[3]);
                    teamOne.setPointsOne(matchArray[1]);
                    teamTwo.setPointsOne(matchArray[3]);
                    matchOneList.add(match);
                    count++;
                }
            count = 1;
            line = "";
            matchList = new LinkedList<>();
            while((line = matchTwo.readLine()) != null){
                matchList.add(line);
            }

            while (!matchList.isEmpty()){
                String matchString = matchList.pop();
                String[] matchArray = matchString.split(" ");
                String[] teamOneArray = matchArray[0].split("_");
                String nameOne = "";
                for(int i = 0; i< teamOneArray.length;i++){
                    if(i != 0){
                        nameOne += " ";
                    }
                    nameOne += teamOneArray[i];
                }

                String[] teamTwoArray = matchArray[2].split("_");
                String nameTwo = "";
                for(int i = 0; i< teamTwoArray.length;i++){
                    if(i != 0){
                        nameTwo += " ";
                    }
                    nameTwo += teamTwoArray[i];
                }

                Team teamOne = findTeam(nameOne);
                Team teamTwo = findTeam(nameTwo);
                Match match = new Match(count, teamOne, teamTwo);
                match.setPointsOne(matchArray[1]);
                match.setPointsTwo(matchArray[3]);
                teamOne.setPointsTwo(matchArray[1]);
                teamTwo.setPointsTwo(matchArray[3]);
                matchTwoList.add(match);
                count++;
            }
            count = 1;
            line = "";
            matchList = new LinkedList<>();
            while((line = matchThree.readLine()) != null){
                matchList.add(line);
            }

            while (!matchList.isEmpty()){
                String matchString = matchList.pop();
                String[] matchArray = matchString.split(" ");
                String[] teamOneArray = matchArray[0].split("_");
                String nameOne = "";
                for(int i = 0; i< teamOneArray.length;i++){
                    if(i != 0){
                        nameOne += " ";
                    }
                    nameOne += teamOneArray[i];
                }

                String[] teamTwoArray = matchArray[2].split("_");
                String nameTwo = "";
                for(int i = 0; i< teamTwoArray.length;i++){
                    if(i != 0){
                        nameTwo += " ";
                    }
                    nameTwo += teamTwoArray[i];
                }

                Team teamOne = findTeam(nameOne);
                Team teamTwo = findTeam(nameTwo);
                Match match = new Match(count, teamOne, teamTwo);
                match.setPointsOne(matchArray[1]);
                match.setPointsTwo(matchArray[3]);
                teamOne.setPointsThree(matchArray[1]);
                teamTwo.setPointsThree(matchArray[3]);
                matchThreeList.add(match);
                count++;
            }
            for (int i = 0; i < teamList.size(); i++) {
                teamList.get(i).computeWinsAndPoints();
            }
        } catch (Exception e){
            System.out.println("Not loaded" + e);
        }
    }

    // find teams based on their name
    private Team findTeam(String name){
        for (int i = 0; i < teamList.size(); i++) {
            Team t = teamList.get(i);
            if (t.getName().equals(name)) {
                return t;
            }
        }
        return null;
    }

    // shuffle the team randomly for the matches, no two matches are equal
    public void shuffleMatches(LinkedList<Match> matchList){
        Collections.shuffle(teamList);
        matchList.clear();
        matches.getItems().clear();
        for (int i = 0; i < teamList.size(); i = i + 2) {
            boolean newEnemy = true;
            LinkedList enemies = teamList.get(i).getPlayedAgainst();
            Team enemy = teamList.get(i + 1);
            for (int j = 0; j < enemies.size(); j++){
                if (enemies.get(j).equals(enemy)){
                    newEnemy = false;
                }
            }
            if (newEnemy) {
                Match m = new Match(tableNummer, teamList.get(i), teamList.get(i + 1));
                matchList.add(m);
                matches.getItems().add(m);
                tableNummer++;
            } else {
                //shuffleMatches(matchList);
            }
        }
        tableNummer = 1;
        //System.out.println(matchList.size() + " " + (teamList.size() / 2));
        if (matchList.isEmpty() || teamList.size() / 2 != matchList.size()){
            shuffleMatches(matchList);
        }
        for (int i = 0; i < teamList.size(); i = i + 2){
            teamList.get(i).addEnemy(teamList.get(i + 1));
            teamList.get(i + 1).addEnemy(teamList.get(i));
        }
    }

    // changes the window back from tournamentWindow.fxml to startWindow.fxml
    public void backToStart(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/sample/startWindow.fxml"));
        primaryStage.getScene().setRoot(root);
        Scene scene = root.getScene();
        primaryStage.show();
        setupStart(scene, primaryStage);
    }

    // sorting method for the table
    class Sortbyroll implements Comparator<Team>
    {
        // Used for sorting in ascending order of
        // roll number
        public int compare(Team a, Team b)
        {
            return b.getPoints() - a.getPoints();
        }
    }
}
