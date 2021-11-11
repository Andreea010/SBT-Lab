package sample.keys;
import javafx.scene.control.CheckBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Data{
    private String reference;
    private String value_type;
    private String solution;
    private String reg_item;
    private String reg_option;
    private String description;
    private String value_data;
    private String reg_key;
    private String type;
    private String see_also;
    private String info;
    private CheckBox checkBox;

    public String getReference() {
        return reference;
    }

    public String getValue_type() {
        return value_type;
    }

    public String getSolution() {
        return solution;
    }

    public String getReg_item() {
        return reg_item;
    }

    public String getReg_option() {
        return reg_option;
    }

    public String getDescription() {
        return description;
    }

    public String getValue_data() {
        return value_data;
    }

    public String getReg_key() {
        return reg_key;
    }

    public String getType() {
        return type;
    }

    public String getSee_also() {
        return see_also;
    }

    public String getInfo() {
        return info;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setValue_type(String value_type) {
        this.value_type = value_type;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public void setReg_item(String reg_item) {
        this.reg_item = reg_item;
    }

    public void setReg_option(String reg_option) {
        this.reg_option = reg_option;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setValue_data(String value_data) {
        this.value_data = value_data;
    }

    public void setReg_key(String reg_key) {
        this.reg_key = reg_key;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSee_also(String see_also) {
        this.see_also = see_also;
    }

    public void setInfo(String info) {
        this.info = info;
    }



    public static ArrayList<Data> getTheList(Data data, Map<String, HashMap> map) {
        CheckBox checkBox = new CheckBox();
        Set keys = map.keySet();
        ArrayList<Data> objects = new ArrayList<>();
        for(Object key : keys) {
           Map<String, String> object = map.get(key);
            Set<String> keys1 = object.keySet();
            for (Object key1 : keys1) {
                if (key1.equals("reference")) {
                    data.setReference(object.get("reference"));
                } else if (key1.equals("value_type")) {
                    data.setValue_type(object.get("value_type"));
                } else if (key1.equals("solution")) {
                    data.setSolution(object.get("solution"));
                } else if (key1.equals("reg_item")) {
                    data.setReg_item(object.get("reg_item"));
                } else if (key1.equals("reg_option")) {
                    data.setReg_option(object.get("reg_option"));
                } else if (key1.equals("description")) {
                    data.setDescription(object.get("description"));
                } else if (key1.equals("value_data")) {
                    data.setValue_data(object.get("value_data"));
                } else if (key1.equals("reg_key")) {
                    data.setReg_key(object.get("reg_key"));
                } else if (key1.equals("type")) {
                    data.setType(object.get("type"));
                } else if (key1.equals("see_also")) {
                    data.setSee_also(object.get("see_also"));
                } else if (key1.equals("info")) {
                    data.setInfo(object.get("info"));
                }
                data.setCheckBox(checkBox);
                checkBox = new CheckBox();
            }
            objects.add(data);
            data = new Data();
        }
        return objects;
    }

}
