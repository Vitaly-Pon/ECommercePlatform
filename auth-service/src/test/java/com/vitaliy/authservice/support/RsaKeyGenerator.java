package com.vitaliy.authservice.support;


import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

public class RsaKeyGenerator {
    public static void main(String[] args) throws Exception {

        KeyPairGenerator generator =
                KeyPairGenerator.getInstance("RSA");

        generator.initialize(2048);

        KeyPair keyPair = generator.generateKeyPair();

        String privateKey =
                Base64.getEncoder()
                        .encodeToString(
                                keyPair.getPrivate().getEncoded()
                        );

        String publicKey =
                Base64.getEncoder()
                        .encodeToString(
                                keyPair.getPublic().getEncoded()
                        );


        Files.createDirectories(
                Path.of("src/test/resources/keys")
        );


        try (FileWriter writer =
                     new FileWriter(
                             "src/test/resources/keys/private.key"
                     )) {

            writer.write(
                    "-----BEGIN PRIVATE KEY-----\n"
                            + privateKey
                            + "\n-----END PRIVATE KEY-----\n"
            );
        }


        try (FileWriter writer =
                     new FileWriter(
                             "src/test/resources/keys/public.key"
                     )) {

            writer.write(
                    "-----BEGIN PUBLIC KEY-----\n"
                            + publicKey
                            + "\n-----END PUBLIC KEY-----\n"
            );
        }


        System.out.println("RSA keys generated");
    }
}
