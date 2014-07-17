package com.derbysoft.common.util;

public final class BytesToStringBuilder {

    private static final int HEXADECIMAL36 = 36;

    private StringBuilder stringBuilder = new StringBuilder();

    private boolean isFirst = true;

    public static String toString(byte[] bytes) {
        BytesToStringBuilder result = new BytesToStringBuilder();
        for (byte b : bytes) {
            result.append(byteTo36(b));
        }
        return result.toString();
    }

    public static byte[] toBytes(String value) {
        String[] byteValues = value.split(Constants.COMMA);
        byte[] bytes = new byte[byteValues.length];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = toByte(byteValues[i].trim());
        }
        return bytes;
    }

    private void append(String b) {
        if (!isFirst) {
            stringBuilder.append(Constants.COMMA);
        }
        stringBuilder.append(b);
        isFirst = false;
    }

    private static String byteTo36(byte b) {
        return Long.toString(b, HEXADECIMAL36);
    }

    private static byte toByte(String s36) {
        return (byte) Integer.parseInt(s36, HEXADECIMAL36);
    }

    @Override
    public String toString() {
        return stringBuilder.toString();
    }
}
