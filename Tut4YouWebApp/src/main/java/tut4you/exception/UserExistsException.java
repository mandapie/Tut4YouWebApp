/*
 * Licensed under the Academic Free License (AFL 3.0).
 *     http://opensource.org/licenses/AFL-3.0
 * 
 *  This code has been developed by a group of CSULB students working on their 
 *  Computer Science senior project called Tutors4You.
 *  
 *  Tutors4You is a web application that students can utilize to find a tutor and
 *  ask them to meet at any location of their choosing. Students that struggle to understand 
 *  the courses they are taking would benefit from this peer to peer tutoring service.
 *  
 *  2017 Amanda Pan <daikiraidemodaisuki@gmail.com>
 *  2017 Andrew Kaichi <ahkaichi@gmail.com>
 *  2017 Keith Tran <keithtran25@gmail.com>
 *  2017 Syed Haider <shayder426@gmail.com>
 */
package tut4you.exception;

/**
 * UserExistsException extends the Exception class to determine if a student exists
 * @author Alvaro Monge <alvaro.monge@csulb.edu>
 */
public class UserExistsException extends Exception {

    /**
     * Creates a new instance of <code>StudentExistsException</code> without detail
     * message.
     */
    public UserExistsException() {
        super("User with the given email address already exists");
    }

    /**
     * Constructs an instance of <code>StudentExistsException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UserExistsException(String msg) {
        super(msg);
    }
}