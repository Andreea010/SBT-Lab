package keys;

import java.io.IOException;
import java.util.Scanner;


public class WinRegistry {


    public static final int REG_SUCCESS = 0;

    public static final int REG_FAILURE = 1;

    public enum WRKey {
        HKLM,  HKCU , HKCR , HKU , HKCC
    }

    private enum WRType {
        REG_SZ, REG_MULTI_SZ, REG_EXPAND_SZ,
        REG_DWORD, REG_QWORD, REG_BINARY, REG_NONE
    }



    public static String createRegString(String key, String valueName) throws IOException, InterruptedException {
        String keyString = key.strip();
        String valueString = " /v" + valueName;
        valueString = valueString.replace('"',' ');
        return keyString + valueString;
    }


    public boolean addKey(WRKey hkey, String key) throws IOException, InterruptedException{
        Process proc = Runtime.getRuntime().exec("REG ADD "+hkey+"\\" + key + " /f");
        proc.waitFor();

        return proc.exitValue() == REG_SUCCESS;
    }


    public static String showAllValues(String key, String valueName) throws IOException, InterruptedException {
        String regString = createRegString(key, valueName);
        Process proc = Runtime.getRuntime().exec("REG QUERY " + regString);
        proc.waitFor();


        String str = "";
        if (proc.exitValue() == REG_SUCCESS) {

            Scanner sc = new Scanner(proc.getInputStream());
            do {
                str = sc.nextLine();


            } while (sc.hasNext() && str != null);
            if (sc != null) {
                sc.close();
            }
        } else {
            System.err.println("Query failure..\n" + regString);
        }


        return str.replace(regString, "");
    }
}
