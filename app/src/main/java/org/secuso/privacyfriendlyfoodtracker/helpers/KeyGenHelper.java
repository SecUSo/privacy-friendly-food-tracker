package org.secuso.privacyfriendlyfoodtracker.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.x500.X500Principal;


/**
 * Helper class to generate, retrieve and storing encrypted passphrase into shared memory.
 */
public class KeyGenHelper {
    private static final String AndroidKeyStore = "AndroidKeyStore";
    private static final String KEY_ALIAS = "FoodTrackerKeyAlias";
    private static final String SHARED_PREFERENCE_NAME = "FoodTrackerSharedPreferenceName";
    private static final String ENCRYPTED_KEY = "FoodTrackerKey";
    private static final String RSA_MODE = "RSA/ECB/PKCS1Padding";

    /**
     * Checks if a generated key already exist
     *
     * @return true if a key exists
     */
    public static boolean isKeyGenerated() {
        KeyStore keyStore;
        try {
            keyStore = KeyStore.getInstance(AndroidKeyStore);
            keyStore.load(null);
            return keyStore.containsAlias(KEY_ALIAS);
        } catch (KeyStoreException e) {
            Log.e("LoggingKeyGenHelper: ", "Can not check if key exists. Default return false.");
        } catch (Exception e) {
            Log.e("LoggingKeyGenHelper: ", "Can not check if key exists. Default return false.");
        }
        return false;
    }

    /**
     * Generates a rsa key pair if it not exists.
     *
     * @param context the application context
     */
    public static void generateKey(Context context) throws Exception {
        KeyStore keyStore;
        keyStore = KeyStore.getInstance(AndroidKeyStore);
        keyStore.load(null, null);

        // Generate the RSA key pairs for encryption
        if (!keyStore.containsAlias(KEY_ALIAS)) {
            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            end.add(Calendar.YEAR, 30);

            KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(context)
                    .setAlias(KEY_ALIAS)
                    .setSubject(new X500Principal("CN=" + KEY_ALIAS))
                    .setSerialNumber(BigInteger.TEN)
                    .setStartDate(start.getTime())
                    .setEndDate(end.getTime())
                    .build();
            KeyPairGenerator kpg = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, AndroidKeyStore);
            kpg.initialize(spec);
            kpg.generateKeyPair();
        }
    }


    /**
     * Generates a random passphrase, encrypt and stores it into SharedPreferences if it not exist.
     * Requires rsa key (call generateKey first).
     *
     * @param context the context
     */
    public static void generatePassphrase(Context context) throws Exception {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        String enryptedKeyB64 = pref.getString(ENCRYPTED_KEY, null);
        if (enryptedKeyB64 == null) {
            byte[] encryptedKey = rsaEncrypt(generateKeyPassphrase());
            enryptedKeyB64 = Base64.encodeToString(encryptedKey, Base64.DEFAULT);
            SharedPreferences.Editor edit = pref.edit();
            edit.putString(ENCRYPTED_KEY, enryptedKeyB64);
            edit.apply();
        }
    }

    public static Key getSecretKey(Context context) throws Exception {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        String enryptedKeyB64 = pref.getString(ENCRYPTED_KEY, null);

        // need to check null, omitted here
        byte[] encryptedKey = Base64.decode(enryptedKeyB64, Base64.DEFAULT);
        byte[] key = rsaDecrypt(encryptedKey);

        return new SecretKeySpec(key, "AES");
    }

    public static char[] getSecretKeyAsChar(Context context) throws Exception {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        String enryptedKeyB64 = pref.getString(ENCRYPTED_KEY, null);

        // need to check null, omitted here
        byte[] encryptedKey = Base64.decode(enryptedKeyB64, Base64.DEFAULT);
        byte[] key = rsaDecrypt(encryptedKey);

        return charFromBytes(key);
    }
    public static char[] charFromBytes(byte byteData[]) {
        char charData[] = new char[byteData.length];
        for(int i = 0; i < charData.length; i++) {
            charData[i] = (char) (((int) byteData[i]) & 0xFF);
        }
        return charData;
    }

    private static byte[] rsaEncrypt(byte[] secret) throws Exception {
        KeyStore keyStore = KeyStore.getInstance(AndroidKeyStore);
        keyStore.load(null);
        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(KEY_ALIAS, null);
        // Encrypt the text
        Cipher inputCipher = Cipher.getInstance(RSA_MODE);
        inputCipher.init(Cipher.ENCRYPT_MODE, privateKeyEntry.getCertificate().getPublicKey());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, inputCipher);
        cipherOutputStream.write(secret);
        cipherOutputStream.close();

        byte[] vals = outputStream.toByteArray();
        return vals;
    }

    private static byte[] rsaDecrypt(byte[] encrypted) throws Exception {
        KeyStore keyStore;
        keyStore = KeyStore.getInstance(AndroidKeyStore);
        keyStore.load(null);
        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(KEY_ALIAS, null);
        Cipher output = Cipher.getInstance(RSA_MODE);
        output.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey());
        CipherInputStream cipherInputStream = new CipherInputStream(
                new ByteArrayInputStream(encrypted), output);
        ArrayList<Byte> values = new ArrayList<>();
        int nextByte;
        while ((nextByte = cipherInputStream.read()) != -1) {
            values.add((byte) nextByte);
        }

        byte[] bytes = new byte[values.size()];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = values.get(i).byteValue();
        }
        return bytes;
    }

    /**
     * Generates a secure random passphrase.
     *
     * @return random bytes
     */
    private static byte[] generateKeyPassphrase() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[16];
        random.nextBytes(bytes);
        return bytes;
    }

}
