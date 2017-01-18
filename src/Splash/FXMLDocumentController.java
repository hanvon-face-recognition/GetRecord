/* ----------------------------------------------------------
 * 文件名称：FXMLDocumentController.java
 * 
 * 作者：秦建辉
 * 
 * 微信：splashcn
 * 
 * 博客：http://www.firstsolver.com/wordpress/
 * 
 * 开发环境：
 *      NetBeans 8.1
 *      JDK 8u92
 *      
 * 版本历史：
 *      V1.2    2016年07月17日
 *              因SDK改进更新代码
 *
 *      V1.1    2014年11月04日
 *              完善考勤记录提取正则表达式，使之能完整获取带照片的考勤记录
 *
 *      V1.0    2014年09月13日
 *              获取考勤记录
------------------------------------------------------------ */

package Splash;

import Com.FirstSolver.Security.Base64;
import Com.FirstSolver.Splash.FaceId;
import Com.FirstSolver.Splash.FaceIdAnswer;
import Com.FirstSolver.Splash.FaceId_ErrorCode;
import Com.FirstSolver.Splash.FaceId_Item;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 *
 * @author 秦建辉
 */
public class FXMLDocumentController implements Initializable {
            
    private final String DeviceCharset = "GBK";
    
    @FXML
    private TextField textFieldDeviceIP;
    
    @FXML
    private TextField textFieldDevicePort;
    
    @FXML
    private TextField textFieldSecretKey;
    
    @FXML
    private ListView listViewRecord;    
    
    @FXML
    private ImageView imageViewPhoto; 

    @FXML
    private void handleButtonExport(ActionEvent event) throws IOException, Exception{
        // 获取当前应用窗体
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();       

        try(FaceId tcpClient = new FaceId(textFieldDeviceIP.getText(), Integer.parseInt(textFieldDevicePort.getText()))) {
            // 设置通信密码
            tcpClient.setSecretKey(textFieldSecretKey.getText());   // 注意：密码和设备通信密码一致 
            
            // 获取考勤记录
            String Command = "GetRecord(end_time=\"" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\")";
            FaceIdAnswer output = new FaceIdAnswer();
            FaceId_ErrorCode ErrorCode = tcpClient.Execute(Command, output, DeviceCharset);
            if (ErrorCode.equals(FaceId_ErrorCode.Success)) 
            {   
                // 提取单条考勤记录
                List<String> RecordList = new LinkedList<>();
                Pattern p = Pattern.compile("\\b(time=.+\\R(?:photo=\"[^\"]+\")*)");                
                Matcher m = p.matcher(output.answer);
                while(m.find())
                {                    
                    RecordList.add(m.group(1));
                }  
                listViewRecord.setItems(FXCollections.observableList(RecordList));
             }
            else
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, "获取考勤记录失败！", ButtonType.OK);
                alert.setTitle("GetRecord");
                alert.setHeaderText("错误");
                alert.showAndWait();
            }
        }
        catch (RuntimeException | IOException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "连接设备失败！", ButtonType.OK);
            alert.setTitle("GetRecord");
            alert.setHeaderText("错误");
            alert.showAndWait();
        }
    }
    
    @FXML
    private void handleButtonClear(ActionEvent event){
        listViewRecord.setItems(null);
        imageViewPhoto.setImage(null);
    }
    
    @FXML
    private void handleRecordClicked(MouseEvent event) throws IOException{
        String Record = (String)listViewRecord.getSelectionModel().getSelectedItem();
        if (Record != null)
        {
            if(Record.contains("photo=\""))
            {
                imageViewPhoto.setImage(new Image(new ByteArrayInputStream(Base64.Decode(FaceId_Item.GetKeyValue(Record, "photo").replace('<', '/'))), 64, 64, true, true));
            }
            else
            {
                imageViewPhoto.setImage(null);
            }            
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
}
