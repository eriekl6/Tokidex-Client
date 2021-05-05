package ca.tokidex.client.ui;

import ca.tokidex.client.control.Controller;
import ca.tokidex.client.model.Tokimon;
import ca.tokidex.client.model.TokimonType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that has methods to manage the different windows needed.
 * Stages included:
 * Main window
     * has a dropdown to select which action to do.
 * Add/Configuring Tokimons window
     * radio button to switch scene from adding vs configuring
 * Display window
     * displays either all tokimons or one tokimon
 * Delete tokimon window
 */
public class WindowManager {

    private final Stage primaryStage;
    private final Controller controller;
    private final int SCENE_WIDTH = 600;
    private final int SCENE_HEIGHT = 600;
    private final int SUBSCENE_WIDTH = 500;
    private final int SUBSCENE_HEIGHT = 500;

    public WindowManager(Stage primaryStage, Controller controller) {
        this.primaryStage = primaryStage;
        this.controller = controller;
    }

    public Scene getHomeScene() {
        Label title = new Label("Tokidex Menu");
        title.getStyleClass().clear();
        title.getStyleClass().add("title-label");

        Label prompt = new Label("Welcome to your Tokidex!\n" +
                "We can help you manage info on your Tokimons!\n" +
                "What would you like to do?");
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFont(Font.font("Monospaced", 18));

        HBox hBox = new HBox(getAllTokiButton(), getNewTokiButton());
        hBox.setSpacing(25);
        hBox.setAlignment(Pos.CENTER);

        VBox vbox = new VBox(title, prompt, hBox);
        Image image = getMainBackground();
        vbox.setBackground(new Background(new BackgroundImage(image, null, null, null, null)));
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(150, 50, 50, 50));

        Scene scene = new Scene(vbox,SCENE_WIDTH,SCENE_HEIGHT);
        scene.getStylesheets().add("file:css/style.css");
        return scene;
    }

    public Scene getDisplayAllTokiScene(List<Tokimon> tokimonList) {
        List<VBox> tokimons = new ArrayList<>();

        for (Tokimon tokimon : tokimonList) {
            VBox vbox = getTokimonDisplay(tokimon);
            vbox.setOnMouseClicked(e -> {
                openDisplayOneTokiSubstage(tokimon);
            });
            tokimons.add(vbox);
        }

        HBox tokimonsHBox = new HBox();
        tokimonsHBox.setSpacing(15);
        tokimonsHBox.setAlignment(Pos.CENTER);
        tokimonsHBox.getChildren().addAll(tokimons);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setMinViewportHeight(180);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setContent(tokimonsHBox);
        scrollPane.setStyle("-fx-background-color: transparent");

        scrollPane.setOnScroll(event -> {
            if(event.getDeltaX() == 0 && event.getDeltaY() != 0) {
                scrollPane.setHvalue(scrollPane.getHvalue() - event.getDeltaY() / tokimonsHBox.getWidth());
            }
        });

        Label label = new Label("Here is the list of current tokimons!\n" +
                "Scroll through and click one to know more.");
        label.setTextAlignment(TextAlignment.CENTER);
        label.setFont(Font.font("Monospaced", 18));


        ComboBox<Long> comboBox = new ComboBox<>();
        for (Tokimon tokimon : tokimonList) {
            comboBox.getItems().add(tokimon.getTokimonId());
        }

        Label label1 = new Label("Already know the id?");
        label1.setFont(Font.font("Monospaced", 12));
        Label outputLabel = new Label();
        outputLabel.setFont(Font.font("Monospaced", 12));
        outputLabel.setTextAlignment(TextAlignment.LEFT);
        comboBox.setOnAction(e-> {
            Tokimon tokimon = controller.getTokimonById(comboBox.getValue());
            if (tokimon == null) {
                outputLabel.setText("Could not get tokimon.\nCheck input or try another one!");
            } else {
                outputLabel.setText("#" + tokimon.getTokimonId() + " " + tokimon.getName());
                openDisplayOneTokiSubstage(tokimon);
            }
        });

        comboBox.getStyleClass().add("combobox");

        HBox hbox = new HBox(label1, comboBox, outputLabel);
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.CENTER);

        HBox buttonHBox = new HBox(getHomeButton(), getNewTokiButton());
        buttonHBox.setAlignment(Pos.CENTER);
        buttonHBox.setSpacing(20);

        VBox vbox = new VBox(scrollPane,label, hbox, buttonHBox);
        Image imagebg = getMainBackground();
        vbox.setBackground(new Background(new BackgroundImage(imagebg, null, null, null, null)));
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setSpacing(20);
        vbox.setPadding(new Insets(50,50,50,50));

        Scene scene = new Scene(vbox, SCENE_WIDTH,SCENE_HEIGHT);
        scene.getStylesheets().add("file:css/style.css");
        return scene;
    }

    public Scene getDisplayOneTokiScene(Tokimon tokimon) {

        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane, SUBSCENE_WIDTH,SUBSCENE_HEIGHT);

        Image bgImage = getBgImageByAbility(tokimon.getAbility());
        borderPane.setBackground(new Background(new BackgroundImage(bgImage, null, null, null, null)));

        Button editButton = new Button("Edit");
        Button delButton = new Button("Delete");
        Button confirmButton = new Button("Confirm");

        HBox botHbox = new HBox(editButton, delButton);
        botHbox.setSpacing(10);
        botHbox.setPadding(new Insets(10));

        editButton.setOnMouseClicked(e -> {
            openAddEditTokiSubstage(tokimon, (Stage) scene.getWindow());
        });

        delButton.setOnMouseClicked(e -> {
            botHbox.getChildren().remove(delButton);
            botHbox.getChildren().add(confirmButton);
        });

        confirmButton.setOnMouseClicked(e -> {
            delButton.setText("Delete");
            controller.deleteTokimonById(tokimon.getTokimonId());
            botHbox.getChildren().remove(confirmButton);
            botHbox.getChildren().add(delButton);
            controller.getAllTokimon();
            Stage stage = (Stage) scene.getWindow();
            stage.close();
        });
        confirmButton.setOnMouseExited(e -> {
            botHbox.getChildren().remove(confirmButton);
            botHbox.getChildren().add(delButton);
        });

        VBox leftVbox = getTokimonDisplay(tokimon);
        leftVbox.getChildren().remove(1);
        leftVbox.setPadding(new Insets(25));
        leftVbox.setAlignment(Pos.TOP_CENTER);

        Label idLabel = new Label("Id: " + tokimon.getTokimonId());
        Label nameLabel = new Label("Name: " + tokimon.getName());
        Label weightLabel = new Label("Weight: " + tokimon.getWeight());
        Label heightLabel = new Label("Height: " + tokimon.getHeight());
        Label abilityLabel = new Label("Ability: " + tokimon.getAbility());
        Label strengthLabel = new Label("Strength: " + tokimon.getStrength());
        Label colorLabel = new Label("Color: " + tokimon.getColor());
        Label healthLabel = new Label("Health: " + tokimon.getHealth());

        HBox healthBox = new HBox(5);
        healthBox.setAlignment(Pos.CENTER);
        double d = tokimon.getHealth()*1.0;
        int hpLv = (int) Math.round(d/10.0);
        int missingHp = 10-hpLv;

        for (int i = 0; i < hpLv; i++) {
            Image happyIcon = new Image("file:img/heart1.png");
            ImageView imageView = new ImageView(happyIcon);
            healthBox.getChildren().add(imageView);
        }
        for (int i = 0; i < missingHp; i++) {
            Image unhappyIcon = new Image("file:img/heart2.png");
            ImageView imageView = new ImageView(unhappyIcon);
            healthBox.getChildren().add(imageView);
        }

        VBox spacer = new VBox();
        VBox spacer2 = new VBox();
        spacer.setPadding(new Insets(10));
        spacer2.setPadding(new Insets(10));
        borderPane.setTop(spacer);
        borderPane.setRight(spacer2);
        VBox tokiInfoBox = new VBox(idLabel, nameLabel,weightLabel,heightLabel,abilityLabel,strengthLabel,colorLabel,healthLabel,healthBox);
        tokiInfoBox.setStyle("" +
                "-fx-background-radius: 30px;" +
                "-fx-background-color: rgba(255,255,255,0.5);");
        tokiInfoBox.setAlignment(Pos.CENTER);
        tokiInfoBox.setPadding(new Insets(10));
        tokiInfoBox.setSpacing(20);

        borderPane.setLeft(leftVbox);
        borderPane.setBottom(botHbox);
        borderPane.setCenter(tokiInfoBox);

        scene.getStylesheets().add("file:css/style.css");
        return scene;
    }

    public Scene getAddEditTokiScene(Tokimon tokimon) {

        Label nameLabel = new Label("Name: ");
        Label weightLabel = new Label("Weight: ");
        Label heightLabel = new Label("Height: ");
        Label abilityLabel = new Label("Ability: ");
        Label strengthLabel = new Label("Strength: ");
        Label colorLabel = new Label("Color: ");
        Label healthLabel = new Label("Health: ");
        Label messageLabel = new Label();
        messageLabel.getStyleClass().clear();
        messageLabel.getStyleClass().add("label-small");
        TextField idTextField = new TextField();
        TextField nameTextField = new TextField();
        TextField weightTextField = new TextField();
        TextField heightTextField = new TextField();
        ComboBox<String> abilityComboBox = new ComboBox<>();
        for (TokimonType value : TokimonType.values()) {
            abilityComboBox.getItems().add(value.name());
        }
        abilityComboBox.getStyleClass().add("dropdown");
        abilityComboBox.setPrefWidth(150);
        TextField strengthTextField = new TextField();
        TextField colorTextField = new TextField();
        TextField healthTextField = new TextField();

        colorTextField.setEditable(false);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(nameLabel, 0,0);
        gridPane.add(weightLabel, 0,1);
        gridPane.add(heightLabel, 0,2);
        gridPane.add(abilityLabel, 0,3);
        gridPane.add(strengthLabel, 0,4);
        gridPane.add(colorLabel, 0,5);
        gridPane.add(healthLabel, 0,6);

        gridPane.add(nameTextField, 1,0);
        gridPane.add(weightTextField, 1,1);
        gridPane.add(heightTextField, 1,2);
        gridPane.add(abilityComboBox, 1,3);
        gridPane.add(strengthTextField, 1,4);
        gridPane.add(colorTextField, 1,5);
        gridPane.add(healthTextField, 1,6);

        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setPrefWidth(50);
        colorPicker.setOnAction(actionEvent -> {
            Color color = colorPicker.getValue();
            colorTextField.setText(color.toString());
        });
        gridPane.add(colorPicker, 2,5);
        gridPane.setPadding(new Insets(70,0,10,30));
        if (tokimon != null) {
            // fill in the current toki info into textfields
            idTextField.setText(String.valueOf(tokimon.getTokimonId()));
            nameTextField.setText(tokimon.getName());
            weightTextField.setText(String.valueOf(tokimon.getWeight()));
            heightTextField.setText(String.valueOf(tokimon.getHeight()));
            abilityComboBox.setValue(tokimon.getAbility());
            strengthTextField.setText(String.valueOf(tokimon.getStrength()));
            colorTextField.setText(tokimon.getColor());
            healthTextField.setText(String.valueOf(tokimon.getHealth()));
        }
        Button submitButton = new Button("Submit!");
        VBox vbox = new VBox(20);
        vbox.getChildren().addAll(gridPane, submitButton, messageLabel);
        submitButton.setOnMouseClicked(e -> {
            if (tokimon == null) {
                //add new tokimon
                try {
                    if (isEmptyText(nameTextField) || isEmptyText(weightTextField) || isEmptyText(heightTextField) || abilityComboBox.getValue() == null || abilityComboBox.getValue().isEmpty()
                            || isEmptyText(strengthTextField) || isEmptyText(colorTextField) || isEmptyText(healthTextField)) {
                        throw new IllegalArgumentException("Missing fields");
                    } else {
                        Tokimon tokimon1 = new Tokimon();
                        tokimon1.setName(nameTextField.getText());
                        tokimon1.setWeight(Double.valueOf(weightTextField.getText()));
                        tokimon1.setHeight(Double.valueOf(heightTextField.getText()));
                        tokimon1.setAbility(abilityComboBox.getValue());
                        tokimon1.setStrength(Double.valueOf(strengthTextField.getText()));
                        tokimon1.setColor(colorTextField.getText());
                        tokimon1.setHealth(Integer.parseInt(healthTextField.getText()));
                        int responseCode = controller.addNewTokimon(tokimon1);
                        if (responseCode == 400) {
                            messageLabel.setText("Bad Input :( " +
                                    "Check values again.\n" +
                                    "Weight, Height, Strength and Health must be numbers");
                        } else {
                            messageLabel.setText("Done!");
                        }
                    }
                } catch (Exception ex) {
                    messageLabel.setText("Make sure everything is filled!\n" +
                            "Weight, Height, Strength and Health must be numbers");
                }
            } else {
                // edit current tokimon
                try {
                    tokimon.setName(nameTextField.getText());
                    tokimon.setWeight(Double.valueOf(weightTextField.getText()));
                    tokimon.setHeight(Double.valueOf(heightTextField.getText()));
                    tokimon.setAbility(abilityComboBox.getValue());
                    tokimon.setStrength(Double.valueOf(strengthTextField.getText()));
                    tokimon.setColor(colorTextField.getText());
                    tokimon.setHealth(Integer.parseInt(healthTextField.getText()));
                    int responseCode = controller.changeTokimon(tokimon);
                    if (responseCode == 400) {
                        messageLabel.setText("Bad Input :( " +
                                "Check values again!\n" +
                                "Weight, Height, Strength and Health must be numbers");
                    } else {
                        messageLabel.setText("Done!");
                    }
                } catch (Exception ex) {
                    messageLabel.setText("Make sure everything is filled!\n" +
                            "Weight, Height, Strength and Health must be numbers!");
                }
            }
        });
        vbox.setAlignment(Pos.TOP_CENTER);
        Image image = getMainBackground();
        vbox.setBackground(new Background(new BackgroundImage(image, null, null, null, null)));
        Scene scene = new Scene(vbox,SUBSCENE_WIDTH,SUBSCENE_HEIGHT);
        scene.getStylesheets().add("file:css/style.css");
        return scene;
    }

    public void changePrimaryScene(Scene scene) {
        primaryStage.setScene(scene);
    }

    private void openDisplayOneTokiSubstage(Tokimon tokimon) {
        Stage subStage = new Stage();
        subStage.getIcons().add(new Image("file:img/icon.png"));
        subStage.setTitle(tokimon.getName() + " Info");
        Scene subTokiScene = getDisplayOneTokiScene(tokimon);
        subStage.initModality(Modality.WINDOW_MODAL);
        subStage.initOwner(primaryStage);
        subStage.setResizable(false);
        subStage.setOnCloseRequest(windowEvent -> controller.getAllTokimon());

        subStage.setScene(subTokiScene);
        subStage.show();
    }

    private void openAddEditTokiSubstage(Tokimon tokimon, Stage parent) {
        Stage subStage = new Stage();
        subStage.getIcons().add(new Image("file:img/icon.png"));
        if (tokimon != null) {
            subStage.setTitle("Edit " + tokimon.getName());
            subStage.setOnCloseRequest(windowEvent -> {
                parent.setScene(getDisplayOneTokiScene(tokimon));
            });
        } else {
            subStage.setTitle("Add Tokimon");
            subStage.setOnCloseRequest(windowEvent -> controller.getAllTokimon());
        }
        Scene subTokiScene = getAddEditTokiScene(tokimon);
        subStage.initModality(Modality.WINDOW_MODAL);
        subStage.initOwner(parent);
        subStage.setResizable(false);
        subStage.setScene(subTokiScene);
        subStage.show();
    }

    private Button getAllTokiButton() {
        Button getAllTokiButton = new Button("Check Tokimon List");
        getAllTokiButton.setOnAction(actionEvent -> controller.getAllTokimon());
        return getAllTokiButton;
    }

    private Button getHomeButton() {
        Button homeButton = new Button("Home");
        homeButton.setOnAction(e -> changePrimaryScene(getHomeScene()));
        return homeButton;
    }

    private Button getNewTokiButton() {
        Button addNewTokimonButton = new Button("Register new Tokimon");

        addNewTokimonButton.setOnAction(actionEvent -> openAddEditTokiSubstage(null, primaryStage));
        return addNewTokimonButton;
    }

    private Image getBgImageByAbility(String ability) {
        if (TokimonType.fromString(ability) == null) {
            return new Image("file:img/bg.jpg");
        }
        return new Image("file:img/" + ability.toLowerCase() + ".png");
    }

    private Image getMainBackground() {
        return new Image("file:img/bg.jpg");
    }

    private VBox getTokimonDisplay(Tokimon tokimon) {
        Image image = new Image(String.format("file:img/%sIcon.png", tokimon.getAbility().toLowerCase()));
        ImageView tokiShape = new ImageView(image);
        tokiShape.setPreserveRatio(true);
        tokiShape.setFitWidth(100);

        Rectangle colorbg = new Rectangle(120, 120, Color.web(tokimon.getColor()));
        colorbg.setArcWidth(50);
        colorbg.setArcHeight(50);
        colorbg.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 10, 0, 0, 0);");

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(colorbg,tokiShape);

        Label nameLabel = new Label(tokimon.getName());
        nameLabel.setTextAlignment(TextAlignment.CENTER);
        nameLabel.setFont(Font.font("Monospaced", 15));

        VBox vbox = new VBox(stackPane, nameLabel);
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(15));
        vbox.setAlignment(Pos.BOTTOM_CENTER);
        return vbox;
    }

    private boolean isEmptyText(TextField textField) {
        return textField.getText() == null || textField.getText().trim().isEmpty();
    }
}
