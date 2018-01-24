/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tut4you.exception;

/**
 *
 * @author Amanda
 */
public class CourseExistsException extends Exception{
    /**
     * Creates a new instance of <code>StudentExistsException</code> without detail
     * message.
     */
    public CourseExistsException() {
        super("Course with the given email address already exists");
    }

    /**
     * Constructs an instance of <code>StudentExistsException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CourseExistsException(String msg) {
        super(msg);
    }
}
