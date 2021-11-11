package sample.keys;
import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;


import java.io.*;
import java.net.URL;
import java.util.*;

public class PolicyOptions implements Initializable {

    String path = "C:\\Users\\User\\OneDrive\\Desktop\\SBT\\src\\audit\\file.json";
    BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
    Gson gson = new Gson();
    HashMap<String, HashMap> json = gson.fromJson(bufferedReader, HashMap.class);
    Map<String, HashMap> map = json;
    Data data = new Data();

    @FXML
    private TableView<Data> table;

    @FXML
    private TableColumn<Data, String> reference;

    @FXML
    private TableColumn<Data, String> value_type;

    @FXML
    private TableColumn<Data, String> solution;

    @FXML
    private TableColumn<Data, String> reg_item;

    @FXML
    private TableColumn<Data, String> reg_option;

    @FXML
    private TableColumn<Data, String> description;

    @FXML
    private TableColumn<Data, String> value_data;

    @FXML
    private TableColumn<Data, String> reg_key;

    @FXML
    private TableColumn<Data, String> type;

    @FXML
    private TableColumn<Data, String> see_also;

    @FXML
    private TableColumn<Data, String> info;

    @FXML
    private TextField textField;

    @FXML
    private TableColumn<Data, String> select;
    @FXML
    private CheckBox SelectAll;

    @FXML
    private Button saveButton;

    public PolicyOptions() throws IOException {
    }
    @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {

        ObservableList<Data> list = FXCollections.observableArrayList(Data.getTheList(data,map));

            reference.setCellValueFactory(new PropertyValueFactory<>("reference"));
            value_type.setCellValueFactory(new PropertyValueFactory<>("value_type"));
            solution.setCellValueFactory(new PropertyValueFactory<>("solution"));
            reg_item.setCellValueFactory(new PropertyValueFactory<>("reg_item"));
            reg_option.setCellValueFactory(new PropertyValueFactory<>("reg_option"));
            description.setCellValueFactory(new PropertyValueFactory<>("description"));
            value_data.setCellValueFactory(new PropertyValueFactory<>("value_data"));
            reg_key.setCellValueFactory(new PropertyValueFactory<>("reg_key"));
            type.setCellValueFactory(new PropertyValueFactory<>("type"));
            see_also.setCellValueFactory(new PropertyValueFactory<>("see_also"));
            info.setCellValueFactory(new PropertyValueFactory<>("info"));
            select.setCellValueFactory(new PropertyValueFactory<>("checkBox"));
            table.setItems(list);


        FilteredList<Data> filteredData = new FilteredList<>(list,b->true);
        textField.textProperty().addListener((observable, oldValue, newValue) ->{
            filteredData.setPredicate(ob ->{
                if(newValue.isEmpty()){
                    return true;
                }
                String searchKeyWord = newValue.toLowerCase();

                if(ob.getDescription().toLowerCase().contains(searchKeyWord)){
                    return true;
                }else return false;
            });
        });

        SortedList<Data> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);

        SelectAll.selectedProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<Data> items = table.getItems();
            for (Data item:items) {
                if(SelectAll.isSelected()){
                    item.getCheckBox().setSelected(true);
                }else
                    item.getCheckBox().setSelected(false);
            }

        });

    }

    @FXML
    void saveOptions(ActionEvent event) {
        ObservableList<Data> itemToSave = FXCollections.observableArrayList();
        ObservableList<Data> items = table.getItems();
        HashMap<String, String> map= new HashMap<>();
        Gson gson = new Gson();
        ArrayList<String> jsonObjects = new ArrayList<>();
        ArrayList<HashMap<String, String>> listOfObjects = new ArrayList<>();
        for (Data item:items) {
            if(item.getCheckBox().isSelected()){
                itemToSave.add(item);
            }
        }

        for (Data item: itemToSave) {
            map.put("reference", item.getReference());
            map.put("value_type", item.getValue_type());
            map.put("solution", item.getSolution());
            map.put("reg_item", item.getReg_item());
            map.put("reg_option", item.getReg_option());
            map.put("description", item.getDescription());
            map.put("value_data", item.getValue_data());
            map.put("reg_key", item.getReg_key());
            map.put("type", item.getType());
            map.put("see_also",item.getSee_also());
            map.put("info", item.getInfo());

            listOfObjects.add(map);
            map= new HashMap<>();
        }

        for (HashMap obj: listOfObjects) {
            String json = gson.toJson(obj);
            gson = new Gson();
            jsonObjects.add(json);
        }

        File fout = new File("Data2.txt");
        try (FileOutputStream fos = new FileOutputStream(fout); BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));) {
            for (String s : jsonObjects) {
                bw.write(s);
                bw.newLine();
            }
        } catch (IOException ignored) {

        }
    }
}

