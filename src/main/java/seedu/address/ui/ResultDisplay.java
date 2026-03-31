package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.logic.commands.CommandResult.PersonIndexPair;

/**
 * A ui for the status bar that is displayed at the header of the application.
 */
public class ResultDisplay extends UiPart<Region> {

    private static final String FXML = "ResultDisplay.fxml";

    @FXML
    private TextArea resultDisplay;

    @FXML
    private ListView<PersonIndexPair> resultListView;

    @FXML
    private Label resultDescriptionDisplay;

    @FXML
    private VBox resultListContainer;

    /**
     * Creates a {@code ResultDisplay} with the given {@code FXML} file.
     */
    public ResultDisplay() {
        super(FXML);
        resultListView.setCellFactory(listView -> new ResultListViewCell());
        if (resultDisplay != null) {
            resultDisplay.setWrapText(true);
        }
    }

    public void setFeedbackToUser(String feedbackToUser) {
        requireNonNull(feedbackToUser);
        resultDisplay.setText(feedbackToUser);
        resultDisplay.setVisible(true);
        resultListContainer.setVisible(false);
        resultListView.getItems().clear();
    }

    /**
     * Sets the list of persons to display in the result box.
     */
    public void setPersonList(List<PersonIndexPair> persons, String description) {
        requireNonNull(persons);
        resultDisplay.setVisible(false);
        updateDescriptionDisplay(description);
        
        resultListView.setItems(FXCollections.observableArrayList(persons));
        
        if (persons.isEmpty()) {
            Label placeholder = new Label("No tutors found.");
            placeholder.setStyle("-fx-text-fill: grey; -fx-alignment: center; -fx-padding: 20;");
            resultListView.setPlaceholder(placeholder);
        } else {
            resultListView.setPlaceholder(null);
        }
        
        resultListContainer.setVisible(true);
    }

    private void updateDescriptionDisplay(String description) {
        if (resultDescriptionDisplay == null) {
            return;
        }
        boolean hasDescription = description != null && !description.isEmpty();
        resultDescriptionDisplay.setText(hasDescription ? description : "");
        resultDescriptionDisplay.setVisible(hasDescription);
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Person} using a {@code PersonCard}.
     */
    class ResultListViewCell extends ListCell<PersonIndexPair> {
        @Override
        protected void updateItem(PersonIndexPair pair, boolean empty) {
            super.updateItem(pair, empty);

            if (empty || pair == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new PersonCard(pair.person, pair.index).getRoot());
            }
        }
    }

}
