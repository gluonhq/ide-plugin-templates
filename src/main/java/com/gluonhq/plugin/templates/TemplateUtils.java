/*
 * Copyright (c) 2016, 2022, Gluon Software
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * * Neither the name of the copyright holder nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gluonhq.plugin.templates;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateUtils {
    
    private static final Logger LOG = Logger.getLogger(TemplateUtils.class.getName());

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
    
    public static boolean isValidPackageName(String str) {
        if (str == null || (str.length() > 0 && 
                (str.charAt(0) == '.' || str.charAt(str.length() - 1) == '.'))) {
            return false;
        }
        String[] tokens = str.split("\\.");
        for (String token : tokens) {
            if ("".equals(token)) {
                return false;
            }
            if (! isJavaIdentifier(token)) {
                return false;
            }
        }
        return true;
    }
    
    public static String getMacAddress(){
        String macAddress = "";
        try {
            InetAddress address = InetAddress.getLocalHost();
            NetworkInterface ni = NetworkInterface.getByInetAddress(address);
            if (ni != null) {
                byte[] ma = ni.getHardwareAddress();
                if (ma != null) {
                    macAddress = computeHash(ma);
                }            
            } else {
                LOG.log(Level.WARNING, "Network Interface for the specified address is not found.");
            }
        } catch (UnknownHostException e) {
            LOG.log(Level.SEVERE, "Error host: {0}", e);
        } catch (SocketException e) {
            LOG.log(Level.SEVERE, "Error socket: {0}", e);
        }
        if (macAddress.isEmpty()) {
            macAddress = UUID.randomUUID().toString();
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
            LOG.log(Level.SEVERE, "Error {0}", e);
        }
        
        return "";
    }
    
    private static boolean isJavaIdentifier(String id) {
        if (id == null || id.isEmpty()) {
            return false;
        }
        int cp = id.codePointAt(0);
        if (! Character.isJavaIdentifierStart(cp)) {
            return false;
        }
        for (int i = Character.charCount(cp); i < id.length(); i += Character.charCount(cp)) {
            cp = id.codePointAt(i);
            if (! Character.isJavaIdentifierPart(cp)) {
                return false;
            }
        }
        
        return ! KEYWORDS.contains(id);
    }
    
    private final static Set<String> KEYWORDS;
    static {
        Set<String> s = new HashSet<>();
        String [] kws = {
            "abstract", "continue",     "for",          "new",          "switch",
            "assert",   "default",      "if",           "package",      "synchronized",
            "boolean",  "do",           "goto",         "private",      "this",
            "break",    "double",       "implements",   "protected",    "throw",
            "byte",     "else",         "import",       "public",       "throws",
            "case",     "enum",         "instanceof",   "return",       "transient",
            "catch",    "extends",      "int",          "short",        "try",
            "char",     "final",        "interface",    "static",       "void",
            "class",    "finally",      "long",         "strictfp",     "volatile",
            "const",    "float",        "native",       "super",        "while",
            // literals
            "null",     "true",         "false"
        };
        s.addAll(Arrays.asList(kws));
        KEYWORDS = Collections.unmodifiableSet(s);
    }
}
