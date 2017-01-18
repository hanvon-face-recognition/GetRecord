/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Splash;

import java.util.Locale;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Splash
 */
public class GetRecord extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Locale.setDefault(Locale.CHINESE);// 让Dialogs按钮显示中文
        
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        
        // 设置窗体标题
        stage.setTitle("获取考勤记录"); 
        
        // 设置窗体图标
        stage.getIcons().add(new Image(getClass().getResourceAsStream("FireEyes.png")));
        
        // 设置到屏幕中心
        stage.centerOnScreen();      
       
        // 显示窗体
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
