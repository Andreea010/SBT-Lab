package data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PolicyParsing {

    static  List<String> listConvetion(FileInputStream inputStream) throws FileNotFoundException {
        Scanner s = new Scanner(inputStream).useDelimiter("\\A");
        String content = s.hasNext() ? s.next() : "";
        String TYPE_PATTERN = "[a-zA-Z]+ ?+_?[a-zA-Z]+ +: +.++";
        Matcher matcher = Pattern.compile(TYPE_PATTERN).matcher(content);
        List<String> result = null;
        if (matcher.find()) {
            result = new ArrayList<>();
            result.add(matcher.group());
            while (matcher.find()) {
                result.add(matcher.group());
            }
        }
        return result;
    }

    static List<String> parselist(List<String> list){
        List<String> mappedList = new ArrayList<String>();
        for (String expression:list

        ) {
            String[] s = expression.split(":");
            String s1 = s[0].strip();
            String s2 = s[1];
            expression = s1 + ":" + s2;
            mappedList.add(expression);
        }
        return mappedList;

    }

    static List<String[]> convertToStringArray(List<String>list){
        List<String[]> arrayList = new ArrayList<>();
        for (String expression:list
        ) {
            arrayList.add(expression.split(":"));
        }
        return arrayList;

    }
    static  HashMap<String, HashMap> convertToHashMapAndParse(List<String[]> list){
        HashMap<String, String> hashMap = new HashMap<>();
        HashMap<String, HashMap> finalHashMap = new HashMap<>();
        for (String[] array:list
        ) {
            if(!hashMap.containsKey(array[0]))
                hashMap.put(array[0],array[1]);

            else {
                if (hashMap.containsKey("description")){
                    finalHashMap.put(hashMap.get("description"),hashMap); }
                hashMap = new HashMap<>();
                hashMap.put(array[0],array[1]);
            }
        }
        return finalHashMap;
    }
    static String convertToJson(Map<String, HashMap> map){
         Gson gson = new GsonBuilder().disableHtmlEscaping().create();
         String jsonString = gson.toJson(map);
         return jsonString;

    }

    public static  void main(String[] args) throws IOException {

    }
}

