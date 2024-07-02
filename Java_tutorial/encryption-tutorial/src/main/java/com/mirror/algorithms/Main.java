package com.mirror.algorithms;

import java.util.Base64;

public class Main {
    public static void main(String[] args) {
        byte[] input = new byte[] { (byte) 0xe4, (byte) 0xb8, (byte) 0xad, 0x21 };
        String b64encoded = Base64.getEncoder().encodeToString(input);
        String b64encoded2 = Base64.getEncoder().withoutPadding().encodeToString(input);
        System.out.println(b64encoded);
        System.out.println(b64encoded2);
        byte[] output = Base64.getDecoder().decode(b64encoded2);
        System.out.println(encodeHexString(output));

        System.out.println();
        byte[] input1 = new byte[] { 0x01, 0x02, 0x7f, 0x00 };
        String b64encodURL = Base64.getUrlEncoder().encodeToString(input1);
        System.out.println(b64encodURL);
        byte[] outputURL = Base64.getUrlDecoder().decode(b64encodURL);
        System.out.println(encodeHexString(outputURL));
    }
    public static String encodeHexString(byte[] data) {

        StringBuilder sb = new StringBuilder();

        for (byte b : data) {

            sb.append(String.format("%02x ", b));

        }

        return sb.toString();

    }
}
