package gomarketapi.utils;

import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;

public class CryptoUtils {

  @Value("${crypto-secret-key}")
  private static String SECRET_KEY = "VYLjx8KVa1HqDhasMjIfGc2GiJDBpjBX";

  private static final byte[] SALT = new byte[16]; // = salt do frontend

  public static String decrypt(String encryptedData) throws Exception {
    // 1. Extrair IV e ciphertext
    String[] parts = encryptedData.split(":");
    byte[] iv = Base64.getDecoder().decode(parts[0]);
    byte[] ciphertext = Base64.getDecoder().decode(parts[1]);

    // 2. Derivar chave (PBKDF2)
    SecretKey key = deriveKey(SECRET_KEY, SALT);

    // 3. Descriptografar
    Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
    cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(128, iv));

    byte[] decrypted = cipher.doFinal(ciphertext);
    return new String(decrypted, StandardCharsets.UTF_8);
  }

  private static SecretKey deriveKey(String password, byte[] salt) throws Exception {
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
    KeySpec spec = new PBEKeySpec(
        password.toCharArray(),
        salt,
        100000, // Iterações (igual ao frontend)
        256 // Tamanho da chave
    );
    return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
  }

  public static String randomUUID() {
    return UUID.randomUUID().toString();
  }
}
