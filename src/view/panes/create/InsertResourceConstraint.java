package view.panes.create;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;
import model.constraint.Constraint;
import model.constraint.Constraints;
import model.constraint.types.AssignResourceConstraint;
import model.constraint.types.AssignTimeConstraint;
import model.event.Event;
import model.event.Events;
import view.panes.CreatePane;

import java.util.*;

/**
 * Created by Anca on 5/1/2017.
 */
public class InsertResourceConstraint extends InsertPaneWithTable {
    private Constraint currentConstraint;
    private VBox finalVBox;
    private String resourceType;
    private String optionName;

    public InsertResourceConstraint(Stage primaryStage, String resourceType, Constraint c, String optionName){
        this.currentConstraint = c;
        this.primaryStage = primaryStage;
        this.resourceType = resourceType;
        this.optionName = optionName;

        if(CreatePane.timetable.getEventConstraints() == null) {
            Constraints constraints = new Constraints();
            constraints.setConstraints(new ArrayList<>());
            CreatePane.timetable.setEventConstraints(constraints);
        }

        if(CreatePane.timetable.getEventConstraints().getConstraints() == null) {
            CreatePane.timetable.getEventConstraints().setConstraints(new ArrayList<>());
        }
    }

    private ArrayList<Pair<String, Boolean>> getTextFieldValues(){
        ArrayList<Pair<String, Boolean>> textFieldValues = new ArrayList<>();

        textFieldValues.add(new Pair("Adaugă greutate", Boolean.TRUE));

        if(currentConstraint.getWeight() != 0){
            textFieldValues.remove(0);
            textFieldValues.add(0, new Pair(currentConstraint.getWeight(), Boolean.FALSE));
        }

        return textFieldValues;
    }


    public VBox addRightPane(){
        ArrayList<Pair<String, Boolean>> textFieldValues = getTextFieldValues();

        ObservableList<VBox> vbList = FXCollections.observableArrayList();

        HBox weightLabel = makeLabel("GREUTATE", true);
        TextField weightTextField = makeTextField(textFieldValues.get(0));
        vbList.add(new VBox(weightLabel, weightTextField));

        HBox descriptionLabel = makeLabel("CONSTRÂNGERE NECESARĂ", false);
        CheckBox descriptionCheckBox = new CheckBox();
        vbList.add(new VBox(descriptionLabel, descriptionCheckBox));

        FlowPane fp = getFlowPane(vbList);

        Map<String, Event> idEventMap = new HashMap<>();

        //Events that are added in the timetable object
        ObservableList<String> eventIds = FXCollections.observableArrayList();
        if(CreatePane.timetable.getEvents()!=null && CreatePane.timetable.getEvents().getEvents() != null) {
            for (Event r : CreatePane.timetable.getEvents().getEvents()) {
                eventIds.add(r.getId());
                idEventMap.put(r.getId(), r);
            }
        }

        //Events linked to the current constraint
        //We also remove the events that are already linked to the current constraint from the possible choices of events
        ObservableList<HBox> constraintEventIdLabels = FXCollections.observableArrayList();
        if(currentConstraint.getAppliesToEvents() != null && currentConstraint.getAppliesToEvents().getEvents() != null) {
            for (Event r : currentConstraint.getAppliesToEvents().getEvents()) {
                constraintEventIdLabels.add(createConstraintEventLabel(r.getId()));
                eventIds.remove(r.getId());
            }
        }

        HBox eventsLabel = makeLabel("LISTA EVENIMENTE", true);

        FlowPane eventsFP = new FlowPane();
        eventsFP.getChildren().addAll(constraintEventIdLabels);

        ComboBox<String> eventsCB = new ComboBox<>(eventIds);
        new ComboBoxAutoComplete<>(eventsCB);
        eventsCB.getStyleClass().add("specialComboBox");

        ImageView imageView = new ImageView(new Image("\\view\\icons\\addIcon.png"));
        imageView.setFitHeight(12);
        imageView.setFitWidth(12);
        imageView.setPreserveRatio(true);

        Button addButton = new Button();
        addButton.setGraphic(imageView);
        addButton.getStyleClass().add("addButton");

        addButton.setOnAction((ActionEvent event) ->{
            if(eventsCB.getValue()!=null){
                HBox hb = createConstraintEventLabel(eventsCB.getValue());

                Button remove = new Button();
                ImageView imageView2 = new ImageView(new Image("\\view\\icons\\deleteIcon.png"));
                imageView2.setFitHeight(6);
                imageView2.setFitWidth(6);
                imageView2.setPreserveRatio(true);

                remove.setGraphic(imageView2);
                remove.getStyleClass().add("removeEventResource");
                remove.setMaxSize(10, 10);

                remove.setOnAction((ActionEvent e) ->{
                    eventsFP.getChildren().remove(hb);

                    eventIds.add(hb.getId());
                    eventsCB.setItems(null);
                    eventsCB.setItems(eventIds);

                });

                hb.getChildren().add(remove);
                eventsFP.getChildren().add(hb);

                eventIds.remove(eventsCB.getValue());
                eventsCB.setItems(null);
                eventsCB.setItems(eventIds);
            }
        });

        HBox addConstraintHBox = new HBox(eventsCB, addButton);

        VBox constraintEventVBox = new VBox(eventsLabel, eventsFP, addConstraintHBox);
        constraintEventVBox.setPadding(new Insets(20, 0, 0 , 0));

        Button saveButton = new Button("SALVEAZĂ");
        saveButton.getStyleClass().add("rightSaveButton");
        saveButton.setOnAction((ActionEvent event) ->{
            //Clearing all errors
            List<HBox> labels = new ArrayList<>(Arrays.asList(weightLabel, eventsLabel));
            List<TextField> textFields =  new ArrayList<>(Arrays.asList(weightTextField));
            List<ComboBox> comboBoxes = new ArrayList<>(Arrays.asList(eventsCB));

            //Clearing all previous errors
            clearErrors(labels, textFields);

            for(ComboBox cb: comboBoxes) {
                for (String s : cb.getStyleClass()) {
                    if (s.equals("addRedMargin")) {
                        cb.getStyleClass().remove(s);
                        break;
                    }
                }
            }

            boolean empty = false;

            if(weightTextField.getText().isEmpty()){
                showErrorMessage(weightLabel, "Greutatea este necesară.", weightTextField);
                empty = true;
            }
            else {
                currentConstraint.setWeight(Integer.valueOf(weightTextField.getText()));
            }

            if(eventsFP.getChildren().size() < 1){
                showErrorMessage(eventsLabel, "Lista de evenimente este necesară.", eventsCB);
                empty = true;
            }
            else{
                List<Event> eventList = new ArrayList<>();
                for(Node n: eventsFP.getChildren()){
                    eventList.add(idEventMap.get(n.getId()));
                }
                currentConstraint.setAppliesToEvents(new Events(eventList));
            }

            if(!empty) {
                currentConstraint.setId(resourceType+(CreatePane.timetable.getEventConstraints().getConstraints().size()+1));
                if(descriptionCheckBox.isSelected()){
                    currentConstraint.setRequired(true);
                }
                else{
                    currentConstraint.setRequired(false);
                }

                if(saveIntoFile()){ //The save button was pressed, the file to save into was chosen
                    CreatePane.timetable.getEventConstraints().getConstraints().add(currentConstraint);
                    if(currentConstraint instanceof AssignResourceConstraint) {
                        currentConstraint = new AssignResourceConstraint();
                    }
                    else{
                        if(currentConstraint instanceof AssignTimeConstraint) {
                            currentConstraint = new AssignTimeConstraint();
                        }
                    }
                }
            }

            //Adding the table with the existing constraints
            if(CreatePane.timetable.getEventConstraints() != null && CreatePane.timetable.getEventConstraints().getConstraints() != null && CreatePane.timetable.getEventConstraints().getConstraints().size()>0) {
                dealWithTable(finalVBox);
            }

        } );

        addSaveButtonIntoHBox(saveButton);

        finalVBox = new VBox(getTitleLabel("Adaugă eveniment"), fp, constraintEventVBox, createExplanatory(), saveButtonHB);
        finalVBox.getStyleClass().add("rightVBox");

        //Adding the table with the existing times
        if(CreatePane.timetable.getEventConstraints()!= null && CreatePane.timetable.getEventConstraints().getConstraints()!=null && CreatePane.timetable.getEventConstraints().getConstraints().size()>0) {
            addTable(finalVBox);
        }

        return finalVBox;
    }

    private HBox createConstraintEventLabel(String text){
        Label label = new Label(text.toUpperCase());
        label.getStyleClass().add("resourceLabelForEvent");

        HBox hb = new HBox(label);
        hb.setPadding(new Insets(3, 5, 10, 0));
        hb.setId(text);

        return hb;
    }

    protected VBox createTable(){
        Label existingLabel = new Label("Constrângeri tip " + optionName);
        existingLabel.getStyleClass().add("resourceText");
        Label listLabel = new Label("Listă");
        listLabel.getStyleClass().add("resourceTypeText");

        HBox nameBox = new HBox();
        nameBox.getStyleClass().add("tableTitle");

        nameBox.getChildren().addAll(existingLabel, listLabel);
        nameBox.getStyleClass().add("titleNameBox");
        nameBox.setAlignment(Pos.BOTTOM_LEFT);

        //Creating the table for the already saved times
        table = new TableView();
        TableColumn idColumn = new TableColumn("Id");
        idColumn.setCellValueFactory(new PropertyValueFactory<Constraint, String>("id"));
        idColumn.setMaxWidth(100);
        idColumn.setPrefWidth(100);
        idColumn.setMinWidth(100);
        idColumn.getStyleClass().add("firstColumn");

        TableColumn descriptionColumn = new TableColumn("Necesar");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<Constraint, Boolean>("required"));
        descriptionColumn.setMaxWidth(100);
        descriptionColumn.setPrefWidth(100);
        descriptionColumn.setMinWidth(100);

        TableColumn eventsColumn = new TableColumn("Evenimente");
        eventsColumn.setCellValueFactory(new PropertyValueFactory<Constraint, Events>("appliesToEvents"));
        eventsColumn.setCellFactory(new Callback<TableColumn<Constraint, Events>, TableCell<Constraint, Events>>(){

            @Override
            public TableCell<Constraint, Events> call(TableColumn<Constraint, Events> param) {

                TableCell<Constraint, Events> cityCell = new TableCell<Constraint, Events>(){

                    @Override
                    protected void updateItem(Events item, boolean empty) {
                        if (item != null) {
                            FlowPane fp = new FlowPane();
                            for(Event r: item.getEvents()){
                                Label l = new Label();
                                l.setText(r.getId());
                                l.getStyleClass().add("resourceLabelForEvent");

                                fp.getChildren().add(l);
                                fp.setHgap(5);
                                fp.setVgap(5);
                                fp.setAlignment(Pos.CENTER_LEFT);
                            }
                            setGraphic(fp);
                        }
                    }
                };

                return cityCell;
            }

        });

        table.getColumns().addAll(idColumn, descriptionColumn, eventsColumn);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        updateTableData();

        table.setFixedCellSize(35);
        table.setPrefHeight((table.getFixedCellSize()+0.8) * (table.getItems().size()+1));

        VBox vb = new VBox(nameBox, table);
        vb.setId("table");

        return vb;
    }

    protected void updateTableData(){
        ObservableList<Constraint> data = FXCollections.observableArrayList();

        String constraintType = null;

        if(currentConstraint instanceof AssignResourceConstraint){
            constraintType = "assignResourceConstraint";
        }
        else{
            if(currentConstraint instanceof AssignTimeConstraint){
                constraintType = "assignTimeConstraint";
            }
        }

        for(Constraint c: CreatePane.timetable.getEventConstraints().getConstraints()){
            if(c.getId().startsWith(constraintType)){
                data.add(c);
            }
        }

        table.setItems(data);
    }
}

