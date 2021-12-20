package keys;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class PolicyOptions implements Initializable {

    Array ar = new Array();
    String path = "C:\\Users\\User\\OneDrive\\Desktop\\SBT\\src\\audit\\audit.json";
    BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
    Gson gson = new Gson();
    HashMap<String, HashMap> json = gson.fromJson(bufferedReader, HashMap.class);
    Map<String, HashMap> map = json;
    Data data = new Data();

    @FXML
    private TableView<Data> table;

    @FXML
    private TableColumn<String, String> failed_policies;

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

    @FXML
    private Button compareButton;

    @FXML
    private Button enforce;

    @FXML
    private Button back;

    @FXML
    private Label LableText;

    @FXML
    private Label Lable22;

    @FXML
    private Label Label23;

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
            failed_policies.setCellValueFactory(new PropertyValueFactory<>("checkBox1"));
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
    void saveOptions(ActionEvent event) throws IOException, InterruptedException {
        ObservableList<Data> itemToSave = FXCollections.observableArrayList();
        ObservableList<Data> items = table.getItems();
        HashMap<String, String> map= new HashMap<>();
        Gson gson = new Gson();
        ArrayList<String> jsonObjects = new ArrayList<>();
        ArrayList<HashMap<String, String>> listOfObjects = new ArrayList<>();
        for (Data item:items) {
            if(item.getCheckBox().isSelected()){
                itemToSave.add(item);
                if (item.getValue_data().equals(null)){
                    System.out.println("null item");
                }
                else{
                    PolicyOptions.comparePolicies(item);
                }
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

        File fout = new File("fileN.json");
        try (FileOutputStream fos = new FileOutputStream(fout); BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));) {
            for (String s : jsonObjects) {
                bw.write(s);
                bw.newLine();
            }
        } catch (IOException ignored) {

        }
    }
    
    public static boolean comparePolicies(Data item) throws IOException, InterruptedException {

        boolean flag = false;

        if(item.getReg_key()==null || item.getReg_item()==null){}
        else
        {
            String command = WinRegistry.showAllValues(item.getReg_key(), item.getReg_item());
            command = command.strip();

            int i;
            ArrayList<Character> name_ch = new ArrayList<>();
            for (i = 0; i < command.length(); i++) {
                if (command.charAt(i) != ' '){
                    name_ch.add(command.charAt(i));
                }
                else{
                    break;
                }
            }
            String name = "";
            for (Character c : name_ch) {
                name += c;
            }

            command = command.replace(name, "");
            command = command.strip();

            ArrayList<Character> type_ch = new ArrayList<>();
            for (i = 0; i < command.length(); i++) {
                if (command.charAt(i) != ' '){
                    type_ch.add(command.charAt(i));
                }
                else{
                    break;
                }
            }
            String type = "";
            for (Character c : type_ch){
                type += c;
            }

            command = command.replace(type, "");
            String value = command.strip();

            if(item.getValue_data().equals(value)){}
            else{
                flag = true;
            }
        }

        return flag;
    }

    @FXML
    void compareOptions() throws IOException, InterruptedException {
        LableText.setText("Policies were compared");

        ObservableList<Data> items = table.getItems();
        for(Data item : items){
            if (item.getCheckBox().isSelected()){
                if (!PolicyOptions.comparePolicies(item)){
                    item.getCheckBox1().setSelected(true);
                }
            }
        }

    }

    @FXML
    void back() throws IOException, InterruptedException {
        Label23.setText("Rollback was executed");
        ObservableList<Data> list = ar.list;
        for (Data item:table.getItems()){
            PolicyOptions.comparePolicies(item);
        }
        for(Data item : list){
            item.getCheckBox1().setSelected(true);
        }
    }

    @FXML
    void enforceOptions(ActionEvent event) throws IOException, InterruptedException {
        Lable22.setText("Policies are enforced");
        ObservableList<Data> itemToSave = FXCollections.observableArrayList();
        HashMap<String, String> map = new HashMap<>();
        Gson gson = new Gson();
        ArrayList<String> jsonObjects = new ArrayList<>();
        ArrayList<HashMap<String, String>> listOfObjects = new ArrayList<>();
        for (Data item:table.getItems()){
            PolicyOptions.comparePolicies(item);
        }
        for (Data item : table.getItems()) {
            if (item.getCheckBox1().isSelected()) {
                itemToSave.add(item);

            }
        }
        for (Data item : itemToSave) {
            map.put("reference", item.getReference());
            map.put("value_type", item.getValue_type());
            map.put("solution", item.getSolution());
            map.put("reg_item", item.getReg_item());
            map.put("reg_option", item.getReg_option());
            map.put("description", item.getDescription());
            map.put("value_data", item.getValue_data());
            map.put("reg_key", item.getReg_key());
            map.put("type", item.getType());
            map.put("see_also", item.getSee_also());
            map.put("info", item.getInfo());

            listOfObjects.add(map);
            map = new HashMap<>();
        }

        for (HashMap obj : listOfObjects) {
            String json = gson.toJson(obj);
            gson = new Gson();
            jsonObjects.add(json);
        }

        File fout = new File("failed_policies.json");
        try (FileOutputStream fos = new FileOutputStream(fout); BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));) {
            for (String s : jsonObjects) {
                bw.write(s);
                bw.newLine();
            }
        } catch (IOException ignored) {

        }
        for (Data item : itemToSave) {
            PolicyOptions.comparePolicies(item);
            item.getCheckBox1().setSelected(false);
        }
        ar.setList(itemToSave);
    }
}

