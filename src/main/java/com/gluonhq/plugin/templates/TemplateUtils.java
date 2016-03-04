package com.gluonhq.plugin.templates;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateUtils {
    
    public static boolean isValidNameView(String nameView) {
        return (nameView != null && !nameView.isEmpty() 
                && nameView.substring(0, 1).matches("[a-zA-Z]") 
                && !nameView.toLowerCase(Locale.ROOT).equals("view"));
    }
    
    public static String getCorrectNameView(String nameView, String defaultName) {
        if (isValidNameView(nameView)) {
            String name = "";
            for (String s : nameView.split("\\s+")) {
                name = name.concat(upperCaseWord(s));
            }
            if (name.endsWith("View")) {
                return name.substring(0, name.lastIndexOf("View"));
            }
            return name;
        }
        return defaultName;
    }

    public static String upperCaseWord(String str) {
        if (!str.isEmpty()) {
            String firstChar = str.substring(0,1).toUpperCase(Locale.ROOT);
            if (str.length() > 1) {
                str = firstChar.concat(str.substring(1));
            } else {
                return firstChar;
            }
        }
        return str;
    }
    
    public static boolean isValidEmail(String email) {
        Pattern emailPattern = Pattern.compile("[a-zA-Z0-9[!#$%&'()*+,/\\-_\\.\"]]+@[a-zA-Z0-9[!#$%&'()*+,/\\-_\"]]+\\.[a-zA-Z0-9[!#$%&'()*+,/\\-_\"\\.]]+");
        Matcher matcher = emailPattern.matcher(email);
        return email != null && !email.isEmpty() && matcher.matches();
    }
    
    public static String getMacAddress(){
        String macAddress = "";
        try {
            InetAddress address = InetAddress.getLocalHost();
            NetworkInterface ni = NetworkInterface.getByInetAddress(address);
            if (ni != null) {
                macAddress = computeHash(ni.getHardwareAddress());
            } else {
                System.out.println("Network Interface for the specified address is not found.");
            }
        } catch (UnknownHostException e) {
            System.out.println("Error host: " + e);
        } catch (SocketException e) {
            System.out.println("Error socket: " + e);
        }
        return macAddress;
    }
    
    private static String computeHash(byte[] buffer) {
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            
            messageDigest.reset();
            messageDigest.update(buffer);
            byte[] digest = messageDigest.digest();
    
            // Convert the byte to hex format
            String hexStr = "";
            for (int i = 0; i < digest.length; i++) {
                hexStr +=  Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1);
            }
    
            return hexStr;
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error " + e);
        }
        
        return "";
    }
}
