import com.mongodb.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class ProtectedConfigFile {

    public static MongoClient mongoClient;
    public static DB database;
    public static DBCollection test;
    public static String[] names;
    public static String[] ids;
    public static String[] emails;
    public static String[] passwords;

    public static void main(String[] args) throws Exception, UnknownHostException {
        /* It can be added different values
        names = new String[]{};
        ids = new String[]{};
        emails = new String[]{};
        passwords = new String[]{};*/

        System.setProperty("password", "Dpassword");
        String password = System.getProperty("password");
        if (password == null) {
            throw new IllegalArgumentException("Run with -Dpassword=<password>");
        }

        byte[] salt = new String("12345678").getBytes();

        int iterationCount = 40000;

        int keyLength = 128;
        SecretKeySpec key = createSecretKey(password.toCharArray(),
                salt, iterationCount, keyLength);


        mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        database = mongoClient.getDB("SomeData");
        test = database.getCollection("test");
        DatabaseObject db = new DatabaseObject();

        /* For adding data
        for(int i = 0; i < names.length; i++){
            db.setName(names[i]);
            db.setMemberID(ids[i]);
            db.setEmail(emails[i]);
            String encryptedPassword = encrypt(passwords[i], key);
            db.setPassword(encryptedPassword);
            test.insert(convert(db));
        }*/

        // Encrypted Data Output
        DBCursor cursor = test.find();
        while (cursor.hasNext()){
            DBObject next = cursor.next();
            System.out.println(next);
        }

        // Decrypted Data Output
        DBCursor cursor1 = test.find();
        while (cursor1.hasNext()){
            DBObject next = cursor1.next();
            Map map = next.toMap();
            String encrpass = (String) map.get("password");
            String decryptpass = decrypt(encrpass, key);
            map.replace("password", decryptpass);
            System.out.println(map);
        }
    }

    private static SecretKeySpec createSecretKey(char[] password, byte[] salt, int iterationCount, int keyLength) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        PBEKeySpec keySpec = new PBEKeySpec(password, salt, iterationCount, keyLength);
        SecretKey keyTmp = keyFactory.generateSecret(keySpec);
        return new SecretKeySpec(keyTmp.getEncoded(), "AES");
    }

    private static String encrypt(String property, SecretKeySpec key) throws GeneralSecurityException, UnsupportedEncodingException {
        Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        pbeCipher.init(Cipher.ENCRYPT_MODE, key);
        AlgorithmParameters parameters = pbeCipher.getParameters();
        IvParameterSpec ivParameterSpec = parameters.getParameterSpec(IvParameterSpec.class);
        byte[] cryptoText = pbeCipher.doFinal(property.getBytes(StandardCharsets.UTF_8));
        byte[] iv = ivParameterSpec.getIV();
        return base64Encode(iv) + ":" + base64Encode(cryptoText);
    }

    private static String base64Encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    private static String decrypt(String string, SecretKeySpec key) throws GeneralSecurityException, IOException {
        String iv = string.split(":")[0];
        String property = string.split(":")[1];
        Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        pbeCipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(base64Decode(iv)));
        return new String(pbeCipher.doFinal(base64Decode(property)), StandardCharsets.UTF_8);
    }

    private static byte[] base64Decode(String property) throws IOException {
        return Base64.getDecoder().decode(property);
    }

    public static DBObject convert(DatabaseObject db){
        return new BasicDBObject("name", db.getName()).append("email", db.getEmail()).append("memberID", db.getMemberID()).append("password", db.getPassword());
    }
}
