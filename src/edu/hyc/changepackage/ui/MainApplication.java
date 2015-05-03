package edu.hyc.changepackage.ui;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import edu.hyc.changepackage.core.FileManager;

public class MainApplication extends Application implements Initializable {

	private Stage primaryStage;

	private Parent root;
	@FXML
	private Button btnPath;
	@FXML
	private Button btnAddSrcFolder;
	@FXML
	private Button btnStart;
	@FXML
	private Label labPath;
	@FXML
	private ListView<String> lvSrcFolder;
	@FXML
	private TextField txtOldPackage;
	@FXML
	private TextField txtNewPackage;
	@FXML
	private TextArea txtAreaMonitor;
	@FXML
	private MenuItem menuAbout;

	private File path;

	private FileManager fileManager;

	private List<File> srcFolders;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		initScene();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initButton();
		initFileManager();
		initSrcFolderList();
		initMenu();
	}

	private void initScene() {
		try {
			root = FXMLLoader.load(getClass().getResource("Main.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.setFullScreen(false);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void initFileManager() {
		UIMonitor monitor = new UIMonitor(txtAreaMonitor);
		fileManager = new FileManager(path, srcFolders, monitor);
	}

	private void initButton() {
		btnPath.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				DirectoryChooser dirChooser = new DirectoryChooser();
				dirChooser.setTitle("Select Project path");
				path = dirChooser.showDialog(primaryStage);
				if (path != null) {
					labPath.setText(path.getName());
					srcFolders.clear();
					lvSrcFolder.getItems().clear();
					btnAddSrcFolder.setDisable(false);
					btnStart.setDisable(true);
					txtOldPackage.setDisable(true);
					txtNewPackage.setDisable(true);
				}
			}
		});
		btnStart.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (path != null) {
					txtAreaMonitor.setText("");
					fileManager.setPath(path, srcFolders);
					String oldPackage = txtOldPackage.getText();
					String newPackage = txtNewPackage.getText();
					fileManager.replacePackage(oldPackage, newPackage);
				}
			}
		});
		btnAddSrcFolder.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (path != null) {
					DirectoryChooser dirChooser = new DirectoryChooser();
					dirChooser.setTitle("Select Src Folder");
					dirChooser.setInitialDirectory(path);
					File srcFolder = dirChooser.showDialog(primaryStage);
					if (srcFolder != null) {
						srcFolders.add(srcFolder);
						lvSrcFolder.getItems()
								.add(srcFolder.getPath().replace(
										path.getPath(), ""));
						btnStart.setDisable(false);
						txtOldPackage.setDisable(false);
						txtNewPackage.setDisable(false);
					}
				}
			}
		});
	}

	private void initSrcFolderList() {
		srcFolders = new ArrayList<File>();
		lvSrcFolder.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				int index = lvSrcFolder.getSelectionModel().getSelectedIndex();
				if (index > -1) {
					lvSrcFolder.getItems().remove(index);
					srcFolders.remove(index);
				}
				if(srcFolders.size()==0){
					btnStart.setDisable(true);
					txtOldPackage.setDisable(true);
					txtNewPackage.setDisable(true);
				}
			}
		});
	}

	private void initMenu() {
		menuAbout.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Dialog");
				alert.setHeaderText(null);
				StringBuffer contentText = new StringBuffer();
				contentText.append("This is Change Package Tool, Release by Leo Chen.\n");
				contentText.append("Open source on Github.\n");
				contentText.append("https://github.com/leo200149/ChangeProjectPackage\n");
				alert.setContentText(contentText.toString());
				alert.showAndWait();
			}
		});
	}

	public void launchApp(String[] args) {
		launch(args);
	}

}
