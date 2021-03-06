package Assignments.finalProject_WastsonImplementation_SpeechToText;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Popup;
import javafx.stage.Stage;
import org.slf4j.LoggerFactory;

public class sttGUI extends Application {
	public static final org.slf4j.Logger LOG = LoggerFactory.getLogger(sttGUI.class);

	public static Button onClick(String word) {

		Stage stage = new Stage();
		// set title for the stage
		stage.setTitle("definition");

		// create a button
		Button button = new Button(word);

		// create a tile pane
		TilePane tilepane = new TilePane();

		// create a label
		Label label = new Label("This is a Popup");

		// create a popup
		Popup popup = setPopup(word);

		// set background
		label.setStyle(" -fx-background-color: white;");

		// add the label
		popup.getContent().add(label);

		// set size of label
		label.setMinWidth(80);
		label.setMinHeight(50);

		// set auto hide
		popup.setAutoHide(true);

		// action event
		EventHandler<ActionEvent> event =
				e -> {
					if (!popup.isShowing())
						popup.show(stage);
				};

		// when button is pressed
		button.setOnAction(event);

		// add button
		tilepane.getChildren().add(button);

		// create a scene
		Scene scene = new Scene(tilepane, 200, 200);

		// set the scene
		stage.setScene(scene);

		stage.show();

		return button;
	}


	public static Popup setPopup (String word) {
		Label popupLabel = new Label(sttDataManager.DataManager.getDefinition(word));
		popupLabel.setStyle(" -fx-background-color: white;");
		popupLabel.setMinWidth(80);
		popupLabel.setMinHeight(50);
		Popup definitionPopup = new Popup();
		definitionPopup.getContent().add(popupLabel);
		definitionPopup.setAutoHide(true);
		return definitionPopup;
	}


	@Override
	public void start(Stage primaryStage) {
		int columnIndex = 0;
		int rowIndex = 0;

		GridPane transcriptPane = new GridPane();
		transcriptPane.setAlignment(Pos.CENTER);
		transcriptPane.setPadding(new Insets(5, 5, 5, 5));
		transcriptPane.setHgap(5);
		transcriptPane.setVgap(5);

		try {
			String[] wordsList = sttTinker.getText(finalProject_sstTinker_constants.STT_TEST_FILE); // get transcript from file
			//String[] wordsList = streamingStt_Tinker.getText(); //get transcript from stream

			for (String word: wordsList) {
				if (rowIndex < finalProject_sstTinker_constants.MAX_LINE_LENGTH) {
					rowIndex++;
				} else {
					columnIndex++;
					rowIndex = 0;
				}
				transcriptPane.add(onClick(word), columnIndex, rowIndex);
			}

			Scene transcriptPaneScene = new Scene(transcriptPane,600, 200);
			primaryStage.setTitle("Subtitles Title"); // Title shown in dialog box menu bar
			primaryStage.setScene(transcriptPaneScene);
			primaryStage.show();

			//@TODO add logic for pane placement
			//@TODO add logic to remove grid pane and create a new one after a certain interval of time

		} catch(Exception e) {
			LOG.info(e.getClass().getName() + e.getMessage());
			sttTinker.printException(e);
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
