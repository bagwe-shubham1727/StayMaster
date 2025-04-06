package com.staymaster.config;

import java.io.IOException;
import java.util.Stack;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class NavigationManager {
	private static final Stack<Scene> sceneStack = new Stack<>();
    private static Stage mainStage; // Main stage reference

    public static void setStage(Stage stage) {
        mainStage = stage;
    }

    public static void navigateTo(String fxmlPath, String pageTitle) throws IOException {
        FXMLLoader loader = new FXMLLoader(NavigationManager.class.getResource(fxmlPath));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        sceneStack.push(scene);
        mainStage.setScene(scene);
        mainStage.setResizable(false);
        mainStage.setTitle(pageTitle);
        mainStage.setOnCloseRequest(event -> {
			
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("Close confirmation");
			alert.setContentText("Are you sure do you want to exit?");
			
			alert.showAndWait().ifPresent(type -> {
				if(type == ButtonType.CANCEL) {
					event.consume(); 
				}
			});
		});
        mainStage.show();
        System.out.println("Navigated to: " + fxmlPath + " | Stack size: " + sceneStack.size());  // Debug statement

    }

    public static void goBack() {
        System.out.println("Current stack size: " + sceneStack.size());  // Debug statement to check stack size
        if (sceneStack.size() > 1) { // There should be at least two scenes to go back
            sceneStack.pop(); // Remove the current scene
            Scene lastScene = sceneStack.peek(); // Get the previous scene
            mainStage.setScene(lastScene);
            mainStage.show();
            System.out.println("Navigated back to previous scene.");
        } else {
            System.out.println("No previous scene to return to.");
        }
    }

}
