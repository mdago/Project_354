package iteration2;

import org.controlsfx.control.Notifications;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ExceptionHandler {
	/**
	 * Displays exception if connection error
	 */
	protected static void displayException() {
		Alert exception = new Alert(AlertType.ERROR);
		exception.setTitle("Exception Dialog");
		exception.setHeaderText("A Connection Error was Detected");
		exception.setContentText("Could not establish a connection with Yahoo Finance. Please check that you are properly connected to the Internet.");
		
		exception.showAndWait();
	}
	
	/**
	 * Displays warning if a stock isn't selected while making choices
	 * @param warningTitle Title of the notification
	 * @param warningMessage Accompanying warning message for notification
	 */
	protected static void displayWarning(String warningTitle, String warningMessage) {
		//	create new warning
		Notifications.create().position(Pos.TOP_CENTER).title(warningTitle).text(warningMessage).darkStyle().showWarning();
	}
}
