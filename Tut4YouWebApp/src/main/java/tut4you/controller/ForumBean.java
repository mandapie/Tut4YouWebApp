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
package tut4you.controller;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import tut4you.model.*;


/**
 *
 * @author Andrew Kaichi <ahkaichi@gmail.com>
 */
@Named
@ViewScoped
public class ForumBean implements Serializable{

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger("ForumBean");
    @EJB
    private Tut4YouApp tut4youApp;
    private Question question;
    private Responses responses;
    private Subject subject;
    private Course course;
    private User student;
    private Tutor tutor;
    private List<Subject> subjectList = new ArrayList();
    private List<Course> courseList = new ArrayList();
    private List<Question> questionList = new ArrayList();
    private List<Responses> responseList = new ArrayList();
    
    public Subject getSubject(){
        return subject;
    }
    
    public void setSubject(Subject subject){
        this.subject = subject;
    }
    public List<Subject> getSubjectList(){
        if (subjectList.isEmpty()){
            subjectList = tut4youApp.getSubjects();
        }
        return subjectList;
    }
    
    public void setSubjectList(List<Subject> subjectList){
        this.subjectList = subjectList;
    }
    
    public List<Course> getCourseList(){
        return courseList;
    }
    
    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }
    
    public List<Responses> getResponseList(){
        return responseList;
    }
    
    public void setResponseList(List<Responses> responseList) {
        this.responseList = responseList;
    }
    
    
    public List<Question> getQuestionList(){
        return questionList;
    }
    
    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }
    
    public void changeSubject(String name) {
        courseList = tut4youApp.getCourses(name);
    }
    
    public void changeCourse(String name) {
        questionList = tut4youApp.getQuestions(name);
    }
    
    public Question getQuestion(){
        return question;
    }
    
    public void setQuestion(Question question){
        this.question = question;
    }
    
    public Responses getResponses(){
        return responses;
    }
    
    public void setResponse(Responses responses){
        this.responses = responses;
    }
    
    public void getResponses(String questionTitle) {
        responseList = tut4youApp.getResponses(questionTitle);
    }
    
    public String getQuestionTitle(String title){
        question = tut4youApp.findQuestionTitle(title);
        return question.getStudent().getUsername();
    }
    
    public Question getQuestionInfo(String courseName, String title) {
        questionList = tut4youApp.getQuestions(courseName);
        for (int i = 0; i < questionList.size(); i++) {
            if (questionList.get(i).getTitle().equals(title)) {
                this.question = questionList.get(i);
            }
        }
        return question;

    }
    
    public String submitResponses(){
        System.out.println("Inside submitResponse");
        this.responses.setQuestion(question);
        tut4youApp.responses(responses);
        return "newResponse";
    }
}
