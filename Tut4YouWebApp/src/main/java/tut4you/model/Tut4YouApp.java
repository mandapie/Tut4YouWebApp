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
package tut4you.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import javax.ejb.Stateless;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import tut4you.exception.*;
import java.util.Arrays;
import javax.faces.application.FacesMessage;
import tut4you.controller.UserBean;

/**
 * This class is an EJB that handles all functionalities of the Web Application.
 *
 * @author Amanda Pan <daikiraidemodaisuki@gmail.com>
 */
@Stateless
public class Tut4YouApp {

    /**
     * Gets the persistence unit name and creates an entity manager to persist
     * data into the database.
     */
    @PersistenceContext(unitName = "tut4youWebAppPU")
    private EntityManager em;
    
    private static final Logger LOGGER = Logger.getLogger("Tut4YouApp");
    
    UserBean userBean = new UserBean();
    String currentUserEmail = userBean.getEmailFromSession();

    /**
     * Query all subjects from the database
     *
     * @return List of subjects
     */
    @RolesAllowed("tut4youapp.student")
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Subject> getSubjects() {
        TypedQuery<Subject> subjectQuery = em.createNamedQuery(Subject.FIND_ALL_SUBJECTS, Subject.class);
        return subjectQuery.getResultList();
    }

    /**
     * Based on the selected subject, query all the courses
     *
     * @param subject takes in the subject name
     * @return List of courses
     */
    @RolesAllowed("tut4youapp.student")
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Course> getCourses(String subject) {
        TypedQuery<Course> courseQuery = em.createNamedQuery(Course.FIND_COURSE_BY_SUBJECT, Course.class);
        courseQuery.setParameter("name", subject);
        return courseQuery.getResultList();
    }

    /**
     * This method can only be called by a student. This methods gets the
 username of the current session and checks if the username is null, if so
 return null. Otherwise, findUser the user email to add the request to be
 submitted.
     *
     * @param request to be submitted
     * @return request if successful
     */
    @RolesAllowed("tut4youapp.student")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Request newRequest(Request request) {
        if (currentUserEmail == null) {
            return null;
        } else {
            User student = findUser(currentUserEmail);
            if (student != null) {
                student.addRequest(request);
                request.setStudent(student);
                request.setStatus(Request.Status.PENDING);
            } else {
                return null;
            }
        }
        em.persist(request);
        em.flush();
        return request;
    }

    /**
     *
     * @return a list of requests from a user
     */
    @RolesAllowed("tut4youapp.student")
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Request> getActiveRequest() {
        String email;
        if (currentUserEmail == null) {
            return null;
        } else {
            User user = findUser(currentUserEmail);
            email = user.getEmail();
            TypedQuery<Request> requestQuery = em.createNamedQuery(Request.FIND_REQUEST_BY_EMAIL, Request.class);
            requestQuery.setParameter("student_email", email);
            requestQuery.setParameter("status", Request.Status.PENDING);
            return requestQuery.getResultList();
        }
    }

/**
     * 
     * @return a list of requests from a user
     */
    @RolesAllowed("tut4youapp.student")
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Request> getDeclinedRequest() {
        String email;
        if (currentUserEmail == null) {
            return null;
        }
        else {
            User user = findUser(currentUserEmail);
            email = user.getEmail();
            TypedQuery<Request> declined = em.createNamedQuery(Request.FIND_REQUEST_BY_EMAIL, Request.class);
            declined.setParameter("student_email", email);
            declined.setParameter("status", Request.Status.DECLINED);
            return declined.getResultList();
        }
    }    
    /**
     * A student can cancel pending requests
     * @param r 
     */
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void cancelRequest(Request r) {
        Request pendingRequest = em.find(Request.class, r.getId());
        r.setStatus(Request.Status.CANCELLED);
        if (r.getTutor() == null){
           em.merge(r);
           //em.remove(r);
           em.flush();
        }
        else if (r.getTutor() != null){
            Tutor tutor = r.getTutor();
            tutor.removePendingRequest(pendingRequest);
            pendingRequest.removeAvailableTutor(tutor);
            em.merge(r);
            //em.remove(r); 
            em.merge(tutor);
            em.flush();
        }
    }
    /**
     * A student can cancel pending requests
     * @param r 
     */
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void declineRequest(Request r) {
        Request pendingRequest = em.find(Request.class, r.getId());
        r.setStatus(Request.Status.DECLINED);
        if (r.getTutor() == null){
           em.merge(r);
           em.flush();
        }
        else if (r.getTutor() != null){
            Tutor tutor = r.getTutor();
            tutor.removePendingRequest(pendingRequest);
            pendingRequest.removeAvailableTutor(tutor);
            em.merge(r);
            em.merge(tutor);
            em.flush();
        }
    } 

    /**
     * Only students can see the number of tutors that tutors the requested
     * course. Finds all tutors that teaches the course.
     *
     * @param course selected course to be tutored
     * @return the number of tutors that tutors the course
     * @author Andrew Kaichi <ahkaichi@gmail.com>
     */
    @RolesAllowed("tut4youapp.student")
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public long getNumOfTutorsFromCourse(String course) {
        TypedQuery<Long> courseTutorQuery = em.createNamedQuery(Tutor.FIND_TUTORS_BY_COURSE, Long.class);
        courseTutorQuery.setParameter("coursename", course);
        return courseTutorQuery.getSingleResult();
    }


    /**
     * Only students can see the list of available tutors that tutors the requested
     * course.
     * Finds all tutors that teaches the course.
     * @param course selected course to be tutored
     * @param dayOfWeek
     * @param time
     * @param doNotDisturb
     * @param zipCode
     * @return the number of tutors that tutors the course
     * @author Andrew Kaichi <ahkaichi@gmail.com> Keith Tran <keithtran25@gmail.com>
     */
    @RolesAllowed("tut4youapp.student")
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Tutor> getTutorsFromCourse(String course, String dayOfWeek, java.util.Date time, Boolean doNotDisturb, String zipCode) {
        TypedQuery<Tutor> courseTutorQuery = em.createNamedQuery(Tutor.FIND_TUTORS_BY_COURSE_DAY_TIME_DZIP, Tutor.class);
        courseTutorQuery.setParameter("coursename", course);
        courseTutorQuery.setParameter("dayofweek", dayOfWeek);
        courseTutorQuery.setParameter("requestTime", time, TemporalType.TIME);
        courseTutorQuery.setParameter("doNotDisturb", false);
        courseTutorQuery.setParameter("zipCode", zipCode);
        return courseTutorQuery.getResultList();
    }

    /**
     * Only students can see the list of tutors.
     *
     * @return
     * @author Andrew Kaichi <ahkaichi@gmail.com>
     */
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Tutor> getTutorsList() {
        TypedQuery<Tutor> courseTutorQuery = em.createNamedQuery(Tutor.FIND_TUTORS, Tutor.class);
        return courseTutorQuery.getResultList();
    }

    /**
     * the selected tutor will be added to a pending request and vice versa.
     *
     * @param tutor
     * @param pending
     */
    @RolesAllowed("tut4youapp.student")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void addPendingRequest(Tutor tutor, Request pending) {
        Request pendingRequest = em.find(Request.class, pending.getId());
        if (pendingRequest == null) {
            pendingRequest = pending;
        }
        tutor.addPendingRequest(pendingRequest);
        pendingRequest.addAvailableTutor(tutor);
        em.merge(tutor);
        em.flush();
    }

    /**
     * Sets a tutor to the request when a tutor accepts the request.
     *
     * @param r
     */
    @RolesAllowed("tut4youapp.tutor")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void setTutorToRequest(Request r) {
        Tutor tutor = findTutor(currentUserEmail);
        r.setStatus(Request.Status.ACCEPTED);
        r.setTutor(tutor);
        em.merge(r);
        em.flush();
        removeRequestFromNotification(r);
    }

    /**
     * Pending request will be removed from the notification list when a tutor
     * declines it.
     *
     * @param r
     */
    @RolesAllowed("tut4youapp.tutor")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void removeRequestFromNotification(Request r) {
        Request pendingRequest = em.find(Request.class, r.getId());
        if (pendingRequest == null) {
            pendingRequest = r;
        }
        Tutor tutor = findTutor(currentUserEmail);
        tutor.removePendingRequest(pendingRequest);
        pendingRequest.removeAvailableTutor(tutor);
        em.merge(tutor);
        em.flush();
    }

    /**
     * Tutors are able to view pending requests.
     * @return
     */
    @RolesAllowed("tut4youapp.tutor")
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Request> getPendingRequestForTutor() {
        Tutor tutor = findTutor(currentUserEmail);
        TypedQuery<Request> requestTutorQuery = em.createNamedQuery(Request.FIND_REQUESTS_BY_TUTOR, Request.class);
        requestTutorQuery.setParameter("email", tutor.getEmail());
        return requestTutorQuery.getResultList();
    }

    /**
     * Only a tutor can add a course from the database. The course will be
     * persisted to the courses_tutors table.
     * @param course to be added
     * @return the selected course to the bean.
     * @throws CourseExistsException
     * @author Keith Tran <keithtran25@gmai l.com>
     * @author Amanda Pan <daikiraidemodaisuki@gmail.com>
     * Referenced code from Alvaro Monge <alvaro.monge@csulb.edu>
     */
    @RolesAllowed("tut4youapp.tutor")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Course addCourse(Course course) throws CourseExistsException {
        if (currentUserEmail == null) {
            return null;
        }
        else {
            Tutor tutor = findTutor(currentUserEmail);
            Course groupCourse = em.find(Course.class, course.getCourseName());
            if (groupCourse == null) {
                groupCourse = course;
            }
            if (!tutor.getCourses().contains(course)) {
                tutor.addCourse(groupCourse);
                groupCourse.addTutor(tutor);
                em.merge(tutor);
                em.flush();
            }
            else {
                throw new CourseExistsException();
            }
            return course;
        }
    }

    /**
     * Only a tutor can add a course that is not from the database. For new
     * course that isn't in database added by a tutor will be persisted to the
     * course table and courses_tutors table.
     * @param course
     * @return the course to the bean
     * @author Keith <keithtran25@gmail.com>
     * @author Amanda Pan <daikiraidemodaisuki@gmail.com>
     * Referenced code from Alvaro Monge <alvaro.monge@csulb.edu>
     * @throws tut4you.exception.CourseExistsException
     */
    @RolesAllowed("tut4youapp.tutor")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Course addNewCourse(Course course) throws CourseExistsException {
        if (currentUserEmail == null) {
            return null;
        }
        else {
            Tutor tutor = findTutor(currentUserEmail);
            Course groupCourse = em.find(Course.class, course.getCourseName());
            if (groupCourse == null) {
                groupCourse = course;
                tutor.addCourse(groupCourse);
                groupCourse.addTutor(tutor);
                em.persist(groupCourse);
            }
            else {
                throw new CourseExistsException();
            }
            return groupCourse;
        }
    }

    /**
     * Only a tutor can view the list of courses that they can teach.
     * @return the list of courses to the bean
     * @author: Syed Haider <shayder426@gmail.com>
     */
    @RolesAllowed("tut4youapp.tutor")
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Course> getTutorCourses() {
        String email;
        if (currentUserEmail == null) {
            return null;
        }
        else {
            Tutor tutor = findTutor(currentUserEmail);
            email = tutor.getEmail();
            TypedQuery<Course> courseQuery = em.createNamedQuery(Course.FIND_COURSES_BY_TUTOR, Course.class);
            courseQuery.setParameter("email", email);
            return courseQuery.getResultList();
        }
    }

    /**
     * Only a tutor can view the list of courses that they can teach.
     *
     * @return the list of courses to the bean
     * @author: Syed Haider <shayder426@gmail.com>
     */
    @RolesAllowed("tut4youapp.tutor")
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Availability> getAvailability() {
        String email;
        if (currentUserEmail == null) {
            return null;
        } else {
            Tutor tutor = findTutor(currentUserEmail);
            email = tutor.getEmail();
            TypedQuery<Availability> availabilityQuery = em.createNamedQuery(Availability.FIND_AVAILABILITY_BY_TUTOR, Availability.class);
            availabilityQuery.setParameter("email", email);
            return availabilityQuery.getResultList();
        }
    }

    /**
     * Only a tutor can view the list of courses that they can teach.
     *
     * @return the list of courses to the bean
     * @author: Syed Haider <shayder426@gmail.com>
     */
    @RolesAllowed("tut4youapp.tutor")
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Availability> getAvailabilityList() {
        String email;
        if (currentUserEmail == null) {
            return null;
        } else {
            Tutor tutor = findTutor(currentUserEmail);
            email = tutor.getEmail();
            TypedQuery<Availability> availabilityQuery = em.createNamedQuery(Availability.FIND_AVAILABILITY_BY_TUTOR, Availability.class);
            availabilityQuery.setParameter("email", email);
            return availabilityQuery.getResultList();
        }
    }

    /**
     * Only a tutor can add availability to the database.
     *
     * @param availability
     * @return the availability
     * @author Andrew <ahkaichi@gmail.com>
     */
    @RolesAllowed("tut4youapp.tutor")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Availability addAvailability(Availability availability) {
        if (currentUserEmail == null) {
            return null;
        } else {
            Tutor tutor = findTutor(currentUserEmail);
            if (tutor != null) {
                tutor.addAvailability(availability);
                availability.setTutor(tutor);
                em.persist(availability);
                em.flush();
            } else {
                return null;
            }
        }
        return availability;
    }

    /**
     * Only a tutor can update his/her availability times.
     *
     * @param availability
     * @author Andrew <ahkaichi@gmail.com>
     * @param startTime
     * @param endTime
     */
    @RolesAllowed("tut4youapp.tutor")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void updateAvailability(Availability availability, Date startTime, Date endTime) {
        Availability updatedAvailability = em.find(Availability.class, availability.getId());
        System.out.println("test from ejb");
        if (updatedAvailability == null) {
            updatedAvailability = availability;
        }
        updatedAvailability.setStartTime(startTime);
        updatedAvailability.setEndTime(endTime);
        em.merge(updatedAvailability);
        em.flush();
    }

    /**
     * Only a tutor can delete his/her availability times.
     * @param availability
     * @author Syed Haider <shayder426@gmail.com>
     */
    @RolesAllowed("tut4youapp.tutor")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteAvailability(Availability availability) {
        Availability toBeDeleted = em.find(Availability.class, availability.getId());
        if (toBeDeleted == null) {
            toBeDeleted = availability;
        }
        Tutor tutor = findTutor(currentUserEmail);
        em.merge(tutor);
        em.remove(em.merge(availability));
    }

    /**
     * A tutor can set their status to be available to receive notifications and
     * set their status to be not available to not receive notifications.
     * @param doNotDisturb
     * @return
     */
    @RolesAllowed("tut4youapp.tutor")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean switchDoNotDisturb(Boolean doNotDisturb
    ) {
        Tutor tutor = findTutor(currentUserEmail);
        doNotDisturb = tutor.isDoNotDisturb();
        if (doNotDisturb == true) {
            tutor.setDoNotDisturb(false);
            em.merge(tutor);
            return doNotDisturb;
        } else {
            tutor.setDoNotDisturb(true);
            em.merge(tutor);
            return doNotDisturb;
        }
    }

    /**
     * Gets a user by finding the email in the user entity.
     * @param email
     * @return user email
     */
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)

    public User findUser(String email) {
        return em.find(User.class, email);
    }

    /**
     * Gets a tutor by finding the email in the tutor entity.
     * @param email
     * @return tutor email
     * @Keith <keithtran25@gmail.com>
     */
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Tutor findTutor(String email) {
        return em.find(Tutor.class, email);
    }
    
    /**
     * Converts student to be a tutor. The student will be added a tutor role.
     * @param user
     * @param userType
     * @param priceRate
     * @param defaultZip
     * @param maxRadius
     * @param joinedDateAsTutor
     * @throws tut4you.exception.UserExistsException
     * @throws java.text.ParseException
     */
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void registerUser(User user, String userType, double priceRate, String defaultZip, int maxRadius, Date joinedDateAsTutor) throws UserExistsException, ParseException {
        if (null == em.find(User.class, user.getEmail())) {
            Group group = em.find(Group.class, "tut4youapp.student");
            User newStudent = new User(user);
            if (group == null) {
                group = new Group("tut4youapp.student");
            }
            if (userType.equals("Student")) {
                newStudent.addGroup(group);
                group.addStudent(newStudent);
                em.persist(newStudent);
            }
            else {
                Tutor newTutor = new Tutor(user);
                newTutor.setDateJoined(joinedDateAsTutor);
                newTutor.setPriceRate(priceRate);
                newTutor.setMaxRadius(maxRadius);
                newTutor.setDefaultZip(defaultZip);
                newTutor.addGroup(group); //Add user a student role
                group.addTutor(newTutor);
                group = em.find(Group.class, "tut4youapp.tutor");
                newTutor.addGroup(group); //Add user a tutor role
                group.addTutor(newTutor);
                em.persist(newTutor);
            }
            em.flush();
        }
        else {
            throw new UserExistsException();
        }
    }

     /**
     * This method can only be called by a student. This methods gets the
     * username of the current session and checks if the username is null, if so
     * return null. Otherwise, findUser the user email to add the rating to be
     * submitted.
     * @param tutor the tutor receiving the rating
     * @param rating to be submitted
     * @return rating if successful
     */
    @RolesAllowed("tut4youapp.student")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Rating newRating(Rating rating, Tutor tutor
    ) {
        if (currentUserEmail == null) {
            return null;
        } else {
            User student = findUser(currentUserEmail);
            if (student != null) {
                student.addRating(rating);
                rating.setStudent(student);
                tutor.addPendingRating(rating);
                rating.setTutor(tutor);
            } else {
                return null;
            }
        }
        em.persist(rating);
        em.merge(tutor);
        em.flush();
        return rating;
    }

    /**
     * This method can only be called by a student. 
     * 
     * It will update the rating that a student has previously submitted.
     *
     * @param rating the rating being updated
     * @param description the description being updated
     * @param ratingValue the ratingValue being updated
     * @author Syed Haider <shayder426@gmail.com>
     */
    @RolesAllowed("tut4youapp.student")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void updateRating(Rating rating, String description, Integer ratingValue) {
        Date date = new Date();
        Rating updatedRating = em.find(Rating.class, rating.getId());
        if (updatedRating == null) {
            updatedRating = rating;
        }
        updatedRating.setDescription(description);
        updatedRating.setRatingValue(ratingValue);
        updatedRating.setCurrentTime(date);
        em.merge(updatedRating);
        em.flush();
    }

    /**
     * Only a tutor can delete his/her availability times.
     *
     * @author Syed Haider <shayder426@gmail.com>
     * @param rating
     */
    @RolesAllowed("tut4youapp.student")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteRating(Rating rating) {
        Rating toBeDeleted = em.find(Rating.class, rating.getId());
        if (toBeDeleted == null) {
            toBeDeleted = rating;
        }
        System.out.println(toBeDeleted);
        Tutor tutor = findTutor(currentUserEmail);
        em.merge(tutor);
        em.remove(em.merge(rating));
    }

    /**
     * Get a list of ratings of a tutor.
     *
     * @return the list of courses to the bean
     * @author: Syed Haider <shayder426@gmail.com>
     */
    @RolesAllowed("tut4youapp.tutor")
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Rating> getRatingList() {
        String email;
        if (currentUserEmail == null) {
            return null;
        } else {
            Tutor tutor = findTutor(currentUserEmail);
            email = tutor.getEmail();
            TypedQuery<Rating> ratingQuery = em.createNamedQuery(Rating.FIND_RATING_BY_TUTOR, Rating.class);
            ratingQuery.setParameter("email", email);
            return ratingQuery.getResultList();
        }
    }

    /**
     * Gets a list of the requests that have been completed
     *
     * @return a list of completed requests for a user
     */
    @RolesAllowed("tut4youapp.student")
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Request> getCompletedRequests() {
        String email;
        if (currentUserEmail == null) {
            return null;
        } else {
            User user = findUser(currentUserEmail);
            email = user.getEmail();
            TypedQuery<Request> requestQuery = em.createNamedQuery(Request.FIND_REQUEST_BY_EMAIL, Request.class);
            requestQuery.setParameter("student_email", email);
            requestQuery.setParameter("status", Request.Status.COMPLETED);
            return requestQuery.getResultList();
        }
    }

    /**
     * Sets a tutor to the request when a tutor completes the request.
     *
     * @param r the request to be set to completed
     */
    @RolesAllowed("tut4youapp.tutor")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void setRequestToComplete(Request r) {
        Long endTime = System.currentTimeMillis();
        Tutor tutor = findTutor(currentUserEmail);
        r.setStatus(Request.Status.COMPLETED);
        r.setTutor(tutor);
        
        em.merge(r);
        em.flush();
    }

   /**
     * Sets a tutor to the request when a tutor completes the request.
     * IN PROGRESS
     * @param r request that is being partaken
     * @param s
     */
    @RolesAllowed("tut4youapp.tutor")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void startSessionTime(Request r) {
        Request request = em.find(Request.class, r.getId());
        Long startTime = System.currentTimeMillis();
        em.merge(r);
        em.flush();
    }

    /**
     *  Gets the average rating of the tutor
     *
     * @return the average rating of the tutor
     * @author Syed Haider <shayder426@gmail.com>
     */
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public double getAverageRating() {
        TypedQuery<Double> averageRatingQuery = em.createNamedQuery(Rating.FIND_AVG_RATING_BY_TUTOR, Double.class);
        return averageRatingQuery.getSingleResult();
    }

    @PermitAll
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public int sortByDayOfWeek(Object o1, Object o2
    ) {
        List<String> dates = Arrays.asList(new String[]{
            "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
        });
        Comparator<String> dateComparator = new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                int value;
                try {
                    SimpleDateFormat format = new SimpleDateFormat("EEE");
                    Date d1 = format.parse(s1);
                    Date d2 = format.parse(s2);
                    if (d1.equals(d2)) {
                        value = s1.substring(s1.indexOf(" ") + 1).compareTo(s2.substring(s2.indexOf(" ") + 1));
                    } else {
                        Calendar cal1 = Calendar.getInstance();
                        Calendar cal2 = Calendar.getInstance();
                        cal1.setTime(d1);
                        cal2.setTime(d2);
                        value = cal1.get(Calendar.DAY_OF_WEEK) - cal2.get(Calendar.DAY_OF_WEEK);
                    }
                    return value;
                } catch (ParseException pe) {
                    throw new RuntimeException(pe);
                }
            }
        };
        Collections.sort(dates, dateComparator);
        return 0;
    }

    /**
     * Checks to see if user inputted email
     * and email in database are equivalent
     *
     * @param email
     * @return true if emails are equivalent
     * @author Syed Haider <shayder426@gmail.com>
     */
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public boolean checkEmail(String email) {
        return currentUserEmail.equals(email);
    }

    @RolesAllowed("tut4youapp.tutor")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void addTranscriptFileLocation(String transcriptFileLocation) {
        Tutor tutor = findTutor(currentUserEmail);
        tutor.setTrancriptFileLocation(transcriptFileLocation);
        em.merge(tutor);
        em.flush();
    }

    @PermitAll
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void updateUser(User updateUser) {
        Tutor tutor = findTutor(currentUserEmail);
        System.out.println("Am I being called");
        if (tutor == null) {
            User student = findUser(currentUserEmail);
            student = (User) updateUser;
            
            em.merge(student);
            em.flush();
        } else {
            User student = findUser(currentUserEmail);
            student = (User) updateUser;
            tutor = (Tutor) updateUser;
            em.merge(student);
            em.merge(tutor);
            em.flush();
        }
        FacesMessage message = new FacesMessage("Successfully Updated Profile");
        FacesContext.getCurrentInstance().addMessage(null, message);

    }
    
    /**
     * update current zip code of tutor
     * @param currentZip
     * @return tutor
     * @author Keith Tran <keithtran25@gmail.com>
     */
    public Tutor updateCurrentZip(String currentZip) {
        Tutor tutor = findTutor(currentUserEmail);
        tutor.setCurrentZip(currentZip);
        em.merge(tutor);
        em.flush();
        return tutor;
    }
    
    /**
     * retrieve list of user email
     * @return list of user email
     * @author Keith Tran <keithtran25@gmail.com>
     */
    @PermitAll
    public List<String> getUserEmails() {
        TypedQuery<String> Query = em.createNamedQuery(User.FIND_USER_EMAILS, String.class);
        return Query.getResultList();
    }
    
    /**
     * retrieve list of user usernames
     * @return list of usernames
     * @author Keith Tran <keithtran25@gmail.com>
     */
    @PermitAll
    public List<String> getUserUserNames() {
        TypedQuery<String> Query = em.createNamedQuery(User.FIND_USER_USERNAMES, String.class);
        return Query.getResultList();
    }
    
    /**
     * Adds ZipCode to DB if it is not already in DB but first checks if it is in the DB
     * 
     * @param zipCode
     * @return zipcode
     * @author Keith Tran <keithtran25@gmail.com>
     *
     */
    @RolesAllowed("tut4youapp.student")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public ZipCode addZipCode(ZipCode zipCode) {
        TypedQuery<ZipCode> Query = em.createNamedQuery(ZipCode.FIND_ZIP_BY_ZIP_MAXRADIUS, ZipCode.class);
        Query.setParameter("zipCode", zipCode.getZipCode());   
        Query.setParameter("maxRadius", zipCode.getMaxRadius());      
        if(Query.getResultList().isEmpty()) {
            em.persist(zipCode);
            em.flush();
        }
        return Query.getSingleResult();
    }
    
    /**
     * Add ZipCodeByRadius if it does not belong to zip code location
     * @param zipCode
     * @param zipCodeByRadius
     * @return ZipCodeByRadius
     * Keith Tran <keithtran25@gmail.com>
     */
    public ZipCodeByRadius addZipCodeByRadius(ZipCode zipCode, ZipCodeByRadius zipCodeByRadius) {
        ZipCodeByRadius zipCodeByRadiusTemp = em.find(ZipCodeByRadius.class, zipCodeByRadius.getZipCodeByRadius());
        if(zipCodeByRadiusTemp == null){
            zipCode.addZipCodeByRadius(zipCodeByRadius);
            zipCodeByRadius.addZipCode(zipCode);
            em.persist(zipCodeByRadius);
            em.flush();
        }
        return zipCodeByRadius;
        
    }
    
    /**
     * saves the message to the database
     * @param message
     */
    @RolesAllowed("tut4youapp.student")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveMessage(Message message) {
        em.persist(message);
        em.flush();
    }
}
