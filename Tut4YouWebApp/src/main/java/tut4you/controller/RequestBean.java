/*
 * Licensed under the Academic Free License (AFL 3.0).
 *     http://opensource.org/licenses/AFL-3.0
 * 
 *  This code has been developed by a group of CSULB students working on their 
 *  Computer Science senior project called Tutors4You.
 *  
 *  Tutors4You is a web application that students can utilize to findUser a tutor and
 *  ask them to meet at any location of their choosing. Students that struggle to understand 
 *  the courses they are taking would benefit from this peer to peer tutoring service.
 
 *  2017 Amanda Pan <daikiraidemodaisuki@gmail.com>
 *  2017 Andrew Kaichi <ahkaichi@gmail.com>
 *  2017 Keith Tran <keithtran25@gmail.com>
 *  2017 Syed Haider <shayder426@gmail.com>
 */
package tut4you.controller;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import json.ZipCodeAPI;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import tut4you.model.*;

/**
 * Binds request inputs to the EJB.
 *
 * @author Amanda Pan <daikiraidemodaisuki@gmail.com>
 */
@Named
@ViewScoped
public class RequestBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger("RequestBean");
    @EJB
    private Tut4YouApp tut4youApp;
    
    private static final OkHttpClient client = new OkHttpClient();
    private Request request;
    private ZipCode zipCode;
    private ZipCodeByRadius zipCodeByRadius;
    private Subject subject;
    private Course course;
    private String time;
    @Temporal(TemporalType.TIME)
    private Date laterTime;
    private int lengthOfSession;
    private long numOfTutors; //number of tutors who teaches the course
    private List<Subject> subjectList = new ArrayList(); //list of subjects to be loaded to the request form
    private List<Course> courseList = new ArrayList(); //list of courses based on subject to load to the request form
    private List<Request> requestList = new ArrayList(); //list of pending requests
    private List<Request> cancelledList = new ArrayList(); //list of cancelled requests
    private List<Request> declinedList = new ArrayList(); //list of declined requests
    private List<Request> acceptedList = new ArrayList(); //list of accepted requests
    private List<Request> completedList = new ArrayList(); //list of completed requests

    private List<Tutor> tutorList; //list of available tutors
    private List<Tutor> temp = new ArrayList();
    private List<String> zipCodesByRadiusList = new ArrayList();
    private Tutor tutor; //the tutor who accepts the request
    private User student;
    private Date dayOfWeek;

    private double hourlyRate;
    private Session session;

    private boolean checkRequestTutorEmail;

    public Date getStartSessionTime() {
        return startSessionTime;
    }

    public void setStartSessionTime(Date startSessionTime) {
        this.startSessionTime = startSessionTime;
    }

    public Date getEndSessionTime() {
        return endSessionTime;
    }

    public void setEndSessionTime(Date endSessionTime) {
        this.endSessionTime = endSessionTime;
    }

    public double getElapsedTimeOfSession() {
        return elapsedTimeOfSession;
    }

    public void setElapsedTimeOfSession(double elapsedTimeOfSession) {
        this.elapsedTimeOfSession = elapsedTimeOfSession;
    }
    private Date startSessionTime;
    private Date endSessionTime;
    private double elapsedTimeOfSession;

    /**
     * RequestBean encapsulates all the functions/services involved in making a
     * request
     */
    @PostConstruct
    public void RequestBean() {
        request = new Request();
        zipCode = new ZipCode();
        zipCodeByRadius = new ZipCodeByRadius();
        tutorList = new ArrayList();
        time = "Immediate";
    }

    public ZipCodeByRadius getZipCodeByRadius() {
        return zipCodeByRadius;
    }

    public void setZipCodeByRadius(ZipCodeByRadius zipCodeByRadius) {
        this.zipCodeByRadius = zipCodeByRadius;
    }

    public Date getCurrentTime() throws ParseException {
        String stringCurrentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        Date currentTime = sdf.parse(stringCurrentTime);
        return currentTime;
    }

    /**
     * Gets current day of when the request is made
     *
     * @return string of the current day
     */
    public String getCurrentDayOfWeek() {
        String str = LocalDate.now().getDayOfWeek().name().toLowerCase();
        String currentDay = str.substring(0, 1).toUpperCase() + str.substring(1);
        return currentDay;
    }

    public ZipCode getZipCode() {
        return zipCode;
    }

    public void setZipCode(ZipCode zipCode) {
        this.zipCode = zipCode;
    }

    /**
     * Gets the Request entity
     *
     * @return the request entity
     */
    public Request getRequest() {
        return request;
    }

    /**
     * Sets the Request entity
     *
     * @param request the request entity
     */
    public void setRequest(Request request) {
        this.request = request;
    }

    /**
     * get Tutor from Request
     *
     * @return tutor
     */
    public Tutor getTutor() {
        return tutor;
    }

    /**
     * set tutor for request
     *
     * @param tutor
     */
    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    /**
     * get request list
     *
     * @return request list
     */
    public List<Request> getRequestList() {
        requestList = tut4youApp.getActiveRequest();
        return requestList;
    }

    /**
     * set request list
     *
     * @param requestList
     */
    public void setRequestList(List<Request> requestList) {
        this.requestList = requestList;
    }

    public List<Request> getAcceptedList() {
        acceptedList = tut4youApp.getAcceptedRequestList();
        return acceptedList;
    }

    public void setAcceptedList(List<Request> acceptedList) {
        this.acceptedList = acceptedList;
    }

    public List<Request> getCompletedList() {
        completedList = tut4youApp.getCompletedRequests();
        return completedList;
    }

    public void setCompletedList(List<Request> completedList) {
        this.completedList = completedList;
    }

    /**
     * gets the declined request
     *
     * @return
     */
    public List<Request> getDeclinedRequest() {
        declinedList = tut4youApp.getDeclinedRequest();
        return declinedList;
    }

    /**
     * sets the declined list
     *
     * @param declinedList
     */
    public void setDeclinedRequest(List<Request> declinedList) {
        this.declinedList = declinedList;
    }

    /**
     * get zip codes by Radius
     *
     * @return zipCodesByRadius List
     */
    public List<String> getZipCodesByRadiusList() {
        //zipCodesByRadius = getData(request.getMaxRadius(), request.getZipCode());
        return zipCodesByRadiusList;
    }

    public void setZipCodesByRadiusList(List<String> zipCodesByRadiusList) {
        this.zipCodesByRadiusList = zipCodesByRadiusList;
    }

    /**
     * Gets the number of tutors who fit the criteria of a request
     *
     * @return the number of tutors available
     */
    public long getNumOfTutors() {
        return numOfTutors;
    }

    /**
     * Sets the number of tutors who fit the criteria of a request
     *
     * @param numOfTutors the tutors who are available
     */
    public void setNumOfTutors(int numOfTutors) {
        this.numOfTutors = numOfTutors;
    }

    /**
     * Gets the subject of the request
     *
     * @return subject of the request
     */
    public Subject getSubject() {
        return subject;
    }

    /**
     * Sets the subject of the request
     *
     * @param s the subject of the request
     */
    public void setSubject(Subject s) {
        subject = s;
    }

    /**
     * Gets the course of the request
     *
     * @return course of the request
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Sets the course of the request
     *
     * @param course the course of the request
     */
    public void setCourse(Course course) {
        this.course = course;
    }

    /**
     * Get the time of the request
     *
     * @return the time of the request
     */
    public String getTime() {
        return time;
    }

    /**
     * Sets the time of the request
     *
     * @param time the time of the request
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * Get the time of the request if user set for later
     *
     * @return the time of the request
     */
    public Date getLaterTime() {
        return laterTime;
    }

    /**
     * Sets the time of the request if user wants a request for later
     *
     * @param laterTime the time of the request if for later
     */
    public void setLaterTime(java.util.Date laterTime) {
        this.laterTime = laterTime;
    }

    /**
     * gets length of session
     *
     * @return lengthOfSession
     */
    public int getLengthOfSession() {
        return lengthOfSession;
    }

    /**
     * sets length of session
     *
     * @param lengthOfSession
     */
    public void setLengthOfSession(int lengthOfSession) {
        this.lengthOfSession = lengthOfSession;
    }

    /**
     * Loads all the subjects from the database.
     *
     * @return a list of subjects
     */
    public List<Subject> getSubjectList() {
        if (subjectList.isEmpty()) {
            subjectList = tut4youApp.getSubjects();
        }
        return subjectList;
    }

    /**
     * Sets the list of subjects in the request
     *
     * @param s the list of subjects
     */
    public void setSubjectList(List<Subject> s) {
        subjectList = s;
    }

    /**
     * Gets the courses in the drop-down menu of request
     *
     * @return the list of courses in database
     */
    public List<Course> getCourseList() {
        return courseList;
    }

    /**
     * Sets the course in the drop-down menu of request
     *
     * @param c the list of courses in the drop-down
     */
    public void setCourseList(List<Course> c) {
        courseList = c;
    }

    public List<Tutor> getTutorList() {
        return tutorList;
    }

    public void setTutorList(List<Tutor> c) {
        tutorList = c;
    }

    /**
     * ajax calls this method to load the courses based on the selected subject
     */
    public void changeSubject() {
        courseList = tut4youApp.getCourses(subject.getSubjectName());
    }

    /**
     * Change the status of a request to canceled
     *
     * @param r
     */
    public void cancelRequest(Request r) {
        tut4youApp.cancelRequest(r);
    }

    /**
     * Change the status of a request to declined
     *
     * @param r
     */
    public void declineRequest(Request r) {
        tut4youApp.declineRequest(r);
        tut4youApp.removeRequestFromNotification(r);
    }

    /**
     * Creates a new request. If successful, get the number of tutors that
     * tutors the course.
     *
     * @return result to be redirected another page
     * @throws java.text.ParseException
     */
    public String createNewRequest() throws ParseException {
        tutorList = new ArrayList();
        zipCodeByRadius = new ZipCodeByRadius();
        String result = "failure";
        if (time.equals("Later")) {
            request.setCurrentTime(getLaterTime());
        } else {
            request.setCurrentTime(getCurrentTime());
        }
        request.setDayOfWeek(getCurrentDayOfWeek());
        request.setLengthOfSession(lengthOfSession);
        zipCode = tut4youApp.addZipCode(zipCode);
        request.setZipCode(zipCode);
        request = tut4youApp.newRequest(request);
        if (request != null) {
            numOfTutors = tut4youApp.getNumOfTutorsFromCourse(request.getCourse().getCourseName());
            result = "success";

            for (String str : getData(zipCode.getMaxRadius(), zipCode.getCurrentZipCode())) {
                System.out.println(str);
                zipCodesByRadiusList = Arrays.asList(str.substring(1, str.length() - 1).split(", "));
            }
            System.out.println("course " + ": " + request.getCourse().getCourseName());
            System.out.println("day of week " + ": " + request.getDayOfWeek());
            System.out.println("course " + ": " + request.getCurrentTime());
            for (int i = 0; i < zipCodesByRadiusList.size(); i++) {
                zipCodeByRadius = new ZipCodeByRadius(zipCodesByRadiusList.get(i));
                zipCodeByRadius = tut4youApp.addZipCodeByRadius(zipCode, zipCodeByRadius);
                temp = new ArrayList();
                temp = tut4youApp.getTutorsFromCourse(request.getCourse().getCourseName(), request.getDayOfWeek(), request.getCurrentTime(), false, zipCodesByRadiusList.get(i));
                tutorList.addAll(temp);
                System.out.println("Zip code " + i + ": " + zipCodesByRadiusList.get(i));
                System.out.println("temp " + i + ": " + temp);
            }
        }
        return result;
    }

    /**
     * Send request to a specific tutor
     *
     * @param t
     */
    public void sendToTutor(Tutor t) {
        tut4youApp.addPendingRequest(t, request);
        tutorList.remove(t);
    }

    /**
     * Sets a tutor to the request if tutor accepts
     *
     * @param r
     * @return
     */
    public String setTutorToRequest(Request r) {
        tut4youApp.setTutorToRequest(r);
        return "chat";
    }

    /**
     * Remove the request from the notification list
     *
     * @param r
     */
    public void removeRequestFromTutor(Request r) {
        tut4youApp.removeRequestFromNotification(r);
    }

    /**
     * http://square.github.io/okhttp/ get request to use for api
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String getJSON(String url) throws IOException {
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();

        return response.body().string();

    }

    /**
     * used to retrieve data from zipcode api api used:
     * http://www.zip-codes.com/content/api/samples/FindZipCodesInRadius.html
     *
     * @param maxRadius
     * @param zipCode
     * @return string of zipcodes
     */
    public static String[] getData(int maxRadius, String zipCode) {
        String json = null;
        try {
            json = getJSON("http://api.zip-codes.com/ZipCodesAPI.svc/1.0/FindZipCodesInRadius?zipcode=" + zipCode + "&minimumradius=0&maximumradius=" + maxRadius + "&key=9EH2HQMG9WY56C9UO3EJ");
        } catch (IOException e) {

        }

        Gson gson = new Gson();
        ZipCodeAPI zipCodeAPI = gson.fromJson(json, ZipCodeAPI.class);

        return new String[]{
            Arrays.toString(zipCodeAPI.getDataList())
        };
    }


    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Date getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Date dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public boolean isCheckRequestTutorEmail(Tutor t) {
        this.tutor = t;
        return tut4youApp.checkRequestTutorEmail(tutor);
    }

    public boolean checkRequestTutorEmail(Tutor t) {
        this.tutor = t;
        System.out.println("String:" + t);
        if (tutor != null) {
            return tut4youApp.checkRequestTutorEmail(tutor);
        } else {
            return false;
        }
    }

    public void setCheckRequestTutorEmail(boolean checkRequestTutorEmail) {
        this.checkRequestTutorEmail = checkRequestTutorEmail;
    }

}
