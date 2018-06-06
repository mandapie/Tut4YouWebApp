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
 
 *  2017 Amanda Pan <daikiraidemodaisuki@gmail.com>
 *  2017 Andrew Kaichi <ahkaichi@gmail.com>
 *  2017 Keith Tran <keithtran25@gmail.com>
 *  2017 Syed Haider <shayder426@gmail.com>
 */
package json;


/**
 * Contains parsed JSON data in POJO class of a course
 * @author Syed Haider <shayder426@gmail.com>
 */
public class CourseList {
    private String courseName;

    /**
     * default constructor
     */
    public CourseList() {
        
    }
    
    /**
     * overloaded constructor
     * @param courseName 
     */
    public CourseList(String courseName) {
        this.courseName = courseName;
    }
    
    /**
     * get course name
     * @return 
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * set course name
     * @param courseName 
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    
}
