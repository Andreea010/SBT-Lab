package sample;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AppliactionServices {

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
            mappedList.add(expression.replace(" ", ""));
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
                if (hashMap.containsKey("reg_item")){
                    finalHashMap.put(hashMap.get("reg_item"),hashMap); }
                hashMap = new HashMap<>();
                hashMap.put(array[0],array[1]);
            }
        }
        return finalHashMap;
    }

    public static  void main(String[] args) throws IOException {
        //List<String> list = ApplicationServices.parselist(ApplicationServices.parseType());
        //List<String[]> arrayList = ApplicationServices.convertToStringArray(list);
        //System.out.println(ApplicationServices.convertToHashMap(arrayList));
       // Map<String, HashMap> map = ApplicationServices.convertToHashMap(arrayList);
//        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
//        String jsonString = gson.toJson(map);
//        System.out.println(jsonString);
//        FileWriter file = new FileWriter("/Users/buciladinara/Desktop/file1.json");
//        file.write(jsonString);
    }
}

