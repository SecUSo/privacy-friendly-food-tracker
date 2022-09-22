/*
This file is part of Privacy friendly food tracker.

Privacy friendly food tracker is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Privacy friendly food tracker is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Privacy friendly food tracker.  If not, see <https://www.gnu.org/licenses/>.
*/
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
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.security.auth.x500.X500Principal;


/**
 * Helper class to generate, retrieve and storing encrypted passphrase into shared memory.
 * Using the tutorial at https://medium.com/@ericfu/securely-storing-secrets-in-an-android-application-501f030ae5a3
 *
 * @author Andre Lutz
 */
public class KeyGenHelper {
    private static final String AndroidKeyStore = "AndroidKeyStore";
    private static final String KEY_ALIAS = "FoodTrackerKeyAlias";
    private static final String SHARED_PREFERENCE_NAME = "FoodTrackerSharedPreferenceName";
    public static final String PREFERENCE_ENCRYPTED_KEY_NAME = "FoodTrackerKey";
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

        String encryptedKeyB64 = pref.getString(PREFERENCE_ENCRYPTED_KEY_NAME, null);
        if(encryptedKeyB64 != null) return;

        byte[] encryptedKey = rsaEncrypt(generateKeyPassphrase());
        encryptedKeyB64 = Base64.encodeToString(encryptedKey, Base64.DEFAULT);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString(PREFERENCE_ENCRYPTED_KEY_NAME, encryptedKeyB64);
        edit.apply();
    }

    /**
     * Returns the encrypted passphrase from shared preferences.
     * @param context the context
     * @return encrypted passphrase as char
     * @throws Exception
     */
    public static char[] getSecretKeyAsChar(Context context) throws Exception {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        String encryptedKeyB64 = pref.getString(PREFERENCE_ENCRYPTED_KEY_NAME, null);

        // need to check null, omitted here
        byte[] encryptedKey = Base64.decode(encryptedKeyB64, Base64.DEFAULT);
        byte[] key = rsaDecrypt(encryptedKey);

        return charFromBytes(key);
    }

    /**
     * Convert byte array to char array.
     * @param byteData the source byte array
     * @return source data as char array
     */
    public static char[] charFromBytes(byte[] byteData) {
        char[] charData = new char[byteData.length];
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

        return outputStream.toByteArray();
    }

    private static byte[] rsaDecrypt(byte[] encrypted) throws Exception {
        KeyStore keyStore = KeyStore.getInstance(AndroidKeyStore);
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
        byte[] bytes = new byte[16];
        random.nextBytes(bytes);
        return bytes;
    }

}
