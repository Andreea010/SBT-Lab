package data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    String jsonContent;
    FileChooser fileChooser = new FileChooser();

    @FXML
    private TextArea textArea;
    @FXML
    private TextArea textAreaP;
    @FXML
    private TextArea textAreaS;

    public Controller() throws FileNotFoundException {
    }

    @FXML
    void importFile(MouseEvent event) throws FileNotFoundException {
     File file = fileChooser.showOpenDialog(new Stage());
     textArea.appendText(file.getAbsolutePath());

    }
    @FXML
    void parseFile(MouseEvent event) throws FileNotFoundException {
    String path = textArea.getText();
    FileInputStream fileInputStream = new FileInputStream(path);
    List<String> list = PolicyParsing.listConvetion(fileInputStream);
    List<String> parsedList = PolicyParsing.parselist(list);
    List<String[]>convertedList = PolicyParsing.convertToStringArray(parsedList);
    HashMap<String, HashMap> parsedFile = PolicyParsing.convertToHashMapAndParse(convertedList);
    Map<String, HashMap> map = parsedFile;
    jsonContent = PolicyParsing.convertToJson(map);
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    String jsonOutput = gson.toJson(jsonContent);
    System.out.println(jsonOutput);
    textAreaP.appendText("File was parced");
    }

    @FXML
    void saveFile(MouseEvent event) throws IOException {
        File file = fileChooser.showSaveDialog(new Stage());
        if(file!= null){
            saveSystem(file,jsonContent);
        }
    }

    public void saveSystem(File file, String content) throws FileNotFoundException {
        PrintWriter printWriter = new PrintWriter(file);
        printWriter.write(content);
        printWriter.close();
        textAreaS.appendText("File was saved");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileChooser.setInitialDirectory(new File("C:\\Users\\User\\OneDrive\\Desktop\\SBT\\src\\audit"));
    }

    @FXML
    void openKeyWindow(MouseEvent event) throws IOException {
      try{
          FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/options.fxml"));
          Parent root1 = fxmlLoader.load();
          Stage stage = new Stage();
          stage.setScene(new Scene(root1));
          stage.show();

      }catch (Exception e){
          System.out.println("Unable to load");
      }
    }
}
