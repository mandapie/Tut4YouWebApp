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

package tut4you.exception;

/**
 *
 * @author Amanda
 */
public class StudentExistsException extends Exception {

    /**
     * Creates a new instance of <code>StudentExistsException</code> without detail
     * message.
     */
    public StudentExistsException() {
        super("User with the given email address already exists");
    }

    /**
     * Constructs an instance of <code>StudentExistsException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public StudentExistsException(String msg) {
        super(msg);
    }
}
