/*
 * Licensed under the Academic Free License (AFL 3.0).
 *     http://opensource.org/licenses/AFL-3.0
 * 
 *  This code has been developed by a group of CSULB students working on their 
 *  Computer Science senior project called Tutors4You.
 *  
 *  Tutors4You is a web application that students can utilize to find a tutor and
 *  ask them to meet at any location of their choosing. Students that struggle to understand 
 *  the availabilitys they are taking would benefit from this peer to peer tutoring service.
 *  
 *  2017 Amanda Pan <daikiraidemodaisuki@gmail.com>
 *  2017 Andrew Kaichi <ahkaichi@gmail.com>
 *  2017 Keith Tran <keithtran25@gmail.com>
 *  2017 Syed Haider <shayder426@gmail.com>
 */
package tut4you.exception;

/**
 * AvailabilityExistsException extends the Exception class to determine if a availability exists
 * @author Alvaro Monge <alvaro.monge@csulb.edu>
 * Modified by Amanda Pan <daikiraidemodaisuki@gmail.com>
 */
public class AvailabilityExistsException extends Exception{
    
    /**
     * Creates a new instance of <code>AvailabilityExistsException</code> without detail
     * message.
     */
    public AvailabilityExistsException() {
        super("Availability with the given email address already exists");
    }

    /**
     * Constructs an instance of <code>AvailabilityExistsException</code> with the
     * specified detail message.
     * @param msg the detail message.
     */
    public AvailabilityExistsException(String msg) {
        super(msg);
    }
}