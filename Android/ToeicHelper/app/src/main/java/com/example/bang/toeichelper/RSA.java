package com.example.bang.toeichelper;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by BANG on 2015-02-11.
 */
public class RSA {

    private KEYSET keyset;

    private Cipher cipher;


    public RSA(){
        //키생성
        keyset = new KEYSET();
    }

    public String encryptMSG(String strPlainMSG, int index){

        //php에서 온 잡동사니 제거
        String publicKeyPEM = keyset.getPublicKey(index).replace("-----BEGIN PUBLIC KEY-----\n", "");
        publicKeyPEM = publicKeyPEM.replace("-----END PUBLIC KEY-----", "");

        //잡동사니 제거 후 byte로 변환
        try {

            byte[] encodedPublicKey = Base64.decode(publicKeyPEM.getBytes("utf-8"), Base64.DEFAULT);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encodedPublicKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PublicKey publicKey = kf.generatePublic(keySpec);

            //"RSA/None/PKCS1Padding" php와 Base64가 호환이 되기위함
            this.cipher = Cipher.getInstance("RSA/None/PKCS1Padding");
            this.cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        String strEncryptMSG = "";

        try {

            byte[] bPlainMSG = strPlainMSG.getBytes("utf-8");
            byte[] bEncryptMsg = this.cipher.doFinal(bPlainMSG);
            strEncryptMSG = Base64.encodeToString(bEncryptMsg, Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return strEncryptMSG;
    }

    public String decryptMSG(String strEncryptMSG, int index){

        //php에서 온 잡동사니 제거
        String privateKeyPEM = keyset.getPrivateKey(index).replace("-----BEGIN PRIVATE KEY-----\n", "");
        privateKeyPEM = privateKeyPEM.replace("-----END PRIVATE KEY-----", "");
        //Log.w("privateKeyPEM", " = " + privateKeyPEM);
        //잡동사니 제거 후 byte로 변환
        try {

            byte[] encodedPrivateKey = Base64.decode(privateKeyPEM.getBytes("utf-8"), Base64.DEFAULT);
            //X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encodedPublicKey);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = kf.generatePrivate(keySpec);

            //"RSA/None/PKCS1Padding" php와 Base64가 호환이 되기위함
            this.cipher = Cipher.getInstance("RSA/None/PKCS1Padding");
            this.cipher.init(Cipher.DECRYPT_MODE, privateKey);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        String strPlainMSG = "";

        try {

            byte[] bEncryptMSG = Base64.decode(strEncryptMSG, Base64.DEFAULT);
            byte[] bPlainMsg = this.cipher.doFinal(bEncryptMSG);
            //Log.w("bPlainMsg", new String(bPlainMsg, "utf-8"));
            strPlainMSG =  new String(bPlainMsg, "utf-8");
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
           e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return strPlainMSG;
    }
}


    //    String plainText = "하이방윤환이다";

//        String privKeyPEM = RSA_PRIVATE_KEY.replace("-----BEGIN PRIVATE KEY-----\n", "");
//        privKeyPEM = privKeyPEM.replace("-----END PRIVATE KEY-----", "");
//        byte [] encoded = Base64.decode(privKeyPEM, Base64.DEFAULT);
//
//
//        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
//        KeyFactory kf = KeyFactory.getInstance("RSA");
//        PrivateKey privKey = kf.generatePrivate(keySpec);
//
//        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
//        cipher.init(Cipher.DECRYPT_MODE, privKey);
//
//        byte[] decodedStr           = Base64.decode(key, Base64.DEFAULT);
//        byte[] plainText            = cipher.doFinal(decodedStr);
//
//        return plainText;


//    String RSA_PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----\n" +
//            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuoF/kSvu+Xe95/SqMYVp\n" +
//            "CXtRhV9EpkNrsjKOZXF9alVSiJMqjDJXpzzkoQwql8rAGCLHNkORFJm85Grn9Y60\n" +
//            "sLs+51srF/hSI5qRASgrpGjvnev3zRMjOKuh6n1zXo/mysVaMc13/d5QO1HOodqH\n" +
//            "LsYhmFIHpwnOrcTncBw9wFV5daJ+wQ0dfoeQlEvNLXYZ3yFPhx0c1QsbnSSZ8Ofh\n" +
//            "IAuX2ky6+Pe/F4ImiikRo+bT77nuSpMoaEVuMS9rwHnQ7lxhgAP1mJk0Mu8qdu27\n" +
//            "PHPc7IJs2o2e/UHWeKFbdjAQiy3nn+i5SidlkPtgOpJYNq/pen8H0qQpOOrWEGEd\n" +
//            "XwIDAQAB\n" +
//            "-----END PUBLIC KEY-----";
//
//    //키가 스트링으로 오기 때문에 바이트로 바꿔줘야함
//    String publicKeyPEM = RSA_PUBLIC_KEY.replace("-----BEGIN PUBLIC KEY-----\n", "");
//    publicKeyPEM = publicKeyPEM.replace("-----END PUBLIC KEY-----", "");
//    //Log.w("publicKeyPEM", publicKeyPEM);
//
//    try {
//        byte [] encoded = Base64.decode(publicKeyPEM.getBytes("utf-8"), Base64.DEFAULT);
//
//        //PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
//        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
//        KeyFactory kf = KeyFactory.getInstance("RSA");
//        PublicKey publicKey = kf.generatePublic(keySpec);
//        Log.w("PublicKey", publicKey.toString());
//
//        Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding");
//        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
//
//        //byte로 바꿔준다
//        byte[] encodedStr = plainText.getBytes("utf-8");
//        byte[] cipherStr = cipher.doFinal(encodedStr);
//
//        Log.w("man", "msg = " + Base64.encodeToString(cipherStr, Base64.DEFAULT));
//        //Log.w("man", "msg = " + cipherStr);
//
//    } catch (NoSuchAlgorithmException e) {
//        e.printStackTrace();
//    } catch (InvalidKeySpecException e) {
//        e.printStackTrace();
//    } catch (NoSuchPaddingException e) {
//        e.printStackTrace();
//    } catch (InvalidKeyException e) {
//        e.printStackTrace();
//    } catch (BadPaddingException e) {
//        e.printStackTrace();
//    } catch (IllegalBlockSizeException e) {
//        e.printStackTrace();
//    } catch (UnsupportedEncodingException e) {
//        e.printStackTrace();
//    }

