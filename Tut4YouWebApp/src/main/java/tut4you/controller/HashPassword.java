/*
 * Licensed under the Academic Free License (AFL 3.0).
 *     http://opensource.org/licenses/AFL-3.0
 * 
 *  This code is distributed to CSULB students in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE, other than educational.
 * 
 *  2013-2017 Alvaro Monge <alvaro.monge@csulb.edu>
 * 
 */

package tut4you.controller;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alvaro Monge (amonge at csulb dot edu)
 */
public class HashPassword {
   /**
    * Generates and returns a SHA512 Digest of the clear text password
    * @param clearTextPassword the password in clear text
    * @return a SHA512 Digest of the password
    * 
    * @see https://stackoverflow.com/questions/33085493/hash-a-password-with-sha-512-in-java
    */
   public static String getSHA512Digest(String clearTextPassword) {
      String generatedPassword;
      try {
         MessageDigest md = MessageDigest.getInstance("SHA-512");

         byte[] bytes = md.digest(clearTextPassword.getBytes("UTF-8"));
         StringBuilder sb = new StringBuilder();
         for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
         }
         generatedPassword = sb.toString();
      } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
         Logger.getLogger(HashPassword.class.getName()).log(Level.SEVERE, null, e);
         generatedPassword = null;
      } 
      return generatedPassword;
   }
}
