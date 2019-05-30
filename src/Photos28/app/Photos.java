package Photos28.app;

import javafx.application.Application;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.User;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import Photos28.view.AdminHomeController;
import Photos28.view.LoginController;
import javafx.fxml.FXMLLoader;

/**
 * The main runner of the Photos application
 * 
 * @author Ansh Shah and Kandarp Patel
 *
 */
public class Photos extends Application {
	
	/**
	 * Creates a stock user and goes to the login screen
	 */
	@Override
	public void start(Stage primaryStage) {
		
		try {
			
			File userFile = new File(System.getProperty("user.dir") + "/users.dat");
			
			if(userFile.exists()) {
				AnchorPane root = FXMLLoader.load(getClass().getResource("/Photos28/view/LoginScreen.fxml"));
				primaryStage.setTitle("Photo Gallery");
				Scene scene = new Scene(root);
				primaryStage.setScene(scene);
				primaryStage.setResizable(false);
				primaryStage.show();
				
				
			}else {
				createStockUser();
				
				AnchorPane root = FXMLLoader.load(getClass().getResource("/Photos28/view/LoginScreen.fxml"));
				primaryStage.setTitle("Photo Gallery");
				Scene scene = new Scene(root);
				primaryStage.setScene(scene);
				primaryStage.setResizable(false);
				primaryStage.show();
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
			
	}
	

	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * Creates the stock user by creating a stock album and adding the stock photos and their information to it
	 * @throws IOException 
	 */
	public void createStockUser() throws IOException {
		
		
		User stockUser = new User("stock");
		Album stockAlbum = new Album("stock");
		ArrayList<Album> album = new ArrayList<Album>();
		album.add(stockAlbum);
		
		Calendar date = Calendar.getInstance();
		
		ArrayList<Photo> stockPhotos = new ArrayList<Photo>();
		Photo londonStock = new Photo("London", date, System.getProperty("user.dir") + "/src/London.jpg");
		stockPhotos.add(londonStock);
		Photo newyorkStock = new Photo("New York", date, System.getProperty("user.dir") + "/src/NewYork.jpg");
		stockPhotos.add(newyorkStock);
		Photo oldtraffordStock = new Photo("Old Trafford", date, System.getProperty("user.dir") + "/src/OldTrafford.jpg");
		stockPhotos.add(oldtraffordStock);
		Photo tajmahalStock = new Photo("Taj Mahal", date, System.getProperty("user.dir") + "/src/TajMahal.jpg");
		stockPhotos.add(tajmahalStock);
		Photo tokyoStock = new Photo("Tokyo", date, System.getProperty("user.dir") + "/src/Tokyo.jpg");
		stockPhotos.add(tokyoStock);
		
		stockAlbum.setPhotos(stockPhotos);
		
		stockUser.setAlbums(album);
		
		AdminHomeController.users.add(stockUser);
		AdminHomeController.write(AdminHomeController.users);
		
	}
}
