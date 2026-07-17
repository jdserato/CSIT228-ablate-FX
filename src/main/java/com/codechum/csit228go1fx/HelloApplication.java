package com.codechum.csit228go1fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import static com.codechum.csit228go1fx.LoginController.user_login;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream("user.ser")
        )) {
            user_login = (User) ois.readObject();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("landing-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 450);
            stage.setTitle("Hi!");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 450);
            stage.setTitle("Hi!");
            stage.setScene(scene);
            stage.show();
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found or is changed to a newer version");
        }
    }
}
