package com.brentcodes.util;

import com.nimbusds.jose.jwk.RSAKey;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;

public class RSAKeyGenerator {
    private static final Path KeyDirectory = Path.of("..", "keys");
    private static final Path PublicKeyFilePath = KeyDirectory.resolve("rsa.pub");
    private static final Path PrivateKeyFilePath = KeyDirectory.resolve("rsa.key");

    private static final OpenOption[] FileOpenOptions =
            {StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING};

    public static void writeRsaKey() throws IOException, NoSuchAlgorithmException {
        Files.createDirectory(KeyDirectory);

        Base64.Encoder encoder = Base64.getEncoder();
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        Files.write(PublicKeyFilePath, publicKey.getEncoded(), FileOpenOptions);
        Files.write(Path.of(PublicKeyFilePath + ".txt"), List.of(
                "-----BEGIN PUBLIC KEY-----",
                encoder.encodeToString(publicKey.getEncoded()),
                "-----END PUBLIC KEY-----"), FileOpenOptions);
        System.out.println("Public key written " + PublicKeyFilePath + " in format " + publicKey.getFormat());

        PrivateKey privateKey = keyPair.getPrivate();
        Files.write(PrivateKeyFilePath, privateKey.getEncoded(), FileOpenOptions);
        Files.write(Path.of(PrivateKeyFilePath + ".txt"), List.of(
                "-----BEGIN PRIVATE KEY-----",
                encoder.encodeToString(privateKey.getEncoded()),
                "-----END PRIVATE KEY-----"), FileOpenOptions);
        System.out.println("Private key written " + PrivateKeyFilePath + " in format " + privateKey.getFormat());
    }

    public static RSAKey readRsaKey() {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            byte[] publicKeyBytes = Files.readAllBytes(PublicKeyFilePath);
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKeyBytes);
            RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(x509EncodedKeySpec);

            byte[] privateKeyBytes = Files.readAllBytes(PrivateKeyFilePath);
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

            return new RSAKey.Builder(publicKey).privateKey(privateKey)
                    .keyID("development_v1")
                    .build();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    /**
     * ./gradlew createKeys
     */
    public static void main(String[] args) throws Exception {
        RSAKeyGenerator.writeRsaKey();
    }
}
