package com.progetto;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptionPass {
    private static String input = new String();

    public EncryptionPass(){
        this.input = input;
    }
    public String setEncrypt(String input) throws NoSuchAlgorithmException {

        MessageDigest en_alg = MessageDigest.getInstance("SHA-512");
        byte[] messageDigest = en_alg.digest(input.getBytes());
        BigInteger bigInt = new BigInteger(1, messageDigest);

        return bigInt.toString(16);
    }
}
