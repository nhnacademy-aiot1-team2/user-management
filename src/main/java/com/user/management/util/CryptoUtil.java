package com.user.management.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 이 클래스는 암호화 관련 기능을 제공하는 유틸리티 클래스입니다.
 */
public class CryptoUtil {

    private CryptoUtil()
    {
        throw new IllegalStateException("Util 클래스는 인스턴스화 할 수 없습니다.");
    }

    /**
     * SecureRandom 객체를 사용하여 20 바이트의 무작위 salt 값을 생성합니다.
     *
     * @return 20 바이트 무작위 Salt 값으로 변환된 문자열
     */
    public static String getSalt() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        return new String(bytes);
    }

    /**
     * 입력된 메시지와 솔트 값을 합친 후, 이를 SHA-256 알고리즘으로 해시합니다.
     *
     * @param msg 해시 할 메시지
     * @param salt 메시지에 추가할 솔트 값
     * @return 해시된 메시지. 알고리즘이 없는 경우 null을 반환
     */
    public static String sha256(String msg, String salt)  {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String input = salt + msg;
            byte[] hashInBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : hashInBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}