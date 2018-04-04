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
import javax.servlet.http.HttpServletRequest;
import tut4you.exception.*;
import java.util.Arrays;
import java.util.*;
import javax.faces.application.FacesMessage;

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
     * username of the current session and checks if the username is null, if so
     * return null. Otherwise, find the user email to add the request to be
     * submitted.
     *
     * @param request to be submitted
     * @return request if successful
     */
    @RolesAllowed("tut4youapp.student")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Request newRequest(Request request) {
        String userName = getUsernameFromSession();
        if (userName == null) {
            return null;
        } else {
            User student = find(userName);
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
        String userName = getUsernameFromSession();
        String email;
        if (userName == null) {
            return null;
        } else {
            User user = find(userName);
            email = user.getEmail();
            TypedQuery<Request> requestQuery = em.createNamedQuery(Request.FIND_REQUEST_BY_EMAIL, Request.class);
            requestQuery.setParameter("student_email", email);
            requestQuery.setParameter("status", Request.Status.PENDING);
            return requestQuery.getResultList();
        }
    }

    /**
     * A student can cancel pending requests
     *
     * @param r
     */
    @RolesAllowed("tut4youapp.student")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void cancelRequest(Request r) {
        r.setStatus(Request.Status.CANCELED);
        removeRequestFromNotification(r);
        em.merge(r);
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
    public int getNumOfTutorsFromCourse(String course) {
        TypedQuery<Integer> courseTutorQuery = em.createNamedQuery(Tutor.FIND_TUTORS_BY_COURSE, Integer.class);
        courseTutorQuery.setParameter("coursename", course);
        return courseTutorQuery.getSingleResult();
    }

    /**

     * Only students can see the list of available tutors that tutors the
     * requested course. Finds all tutors that teaches the course.
     *
     * @param course selected course to be tutored
     * @param dayOfWeek
     * @param time
     * @param doNotDisturb
     * @return the number of tutors that tutors the course
     * @author Andrew Kaichi <ahkaichi@gmail.com>
     */
    @RolesAllowed("tut4youapp.student")
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Tutor> getTutorsFromCourse(String course, String dayOfWeek, java.util.Date time, Boolean doNotDisturb) {
        TypedQuery<Tutor> courseTutorQuery = em.createNamedQuery(Tutor.FIND_TUTORS_BY_COURSE_DAY_TIME, Tutor.class);
        courseTutorQuery.setParameter("coursename", course);
        courseTutorQuery.setParameter("dayofweek", dayOfWeek);
        courseTutorQuery.setParameter("requestTime", time, TemporalType.TIME);
        courseTutorQuery.setParameter("doNotDisturb", false);
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
        String userName = getUsernameFromSession();
        Tutor tutor = findTutorUserName(userName);
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
        String userName = getUsernameFromSession();
        Tutor tutor = findTutorUserName(userName);
        tutor.removePendingRequest(pendingRequest);
        pendingRequest.removeAvailableTutor(tutor);
        em.merge(tutor);
        em.flush();
    }

    /**
     * Tutors are able to view pending requests.
     *
     * @return
     */
    @RolesAllowed("tut4youapp.tutor")
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Request> getPendingRequestForTutor() {
        String userName = getUsernameFromSession();
        Tutor tutor = findTutorUserName(userName);
        TypedQuery<Request> requestTutorQuery = em.createNamedQuery(Request.FIND_REQUESTS_BY_TUTOR, Request.class);
        requestTutorQuery.setParameter("email", tutor.getEmail());
        return requestTutorQuery.getResultList();
    }

        
    /**
     * Only a tutor can add a course from the database. The course will be
     * persisted to the courses_tutors table.
     *
     * @param course to be added
     * @return the selected course to the bean.
     * @throws CourseExistsException
     * @author Keith <keithtran25@gmail.com>
     * Referenced code from Alvaro Monge <alvaro.monge@csulb.edu>
     */
    @RolesAllowed("tut4youapp.tutor")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Course addCourse(Course course) throws CourseExistsException {
        String userName = getUsernameFromSession();
        if (userName == null) {
            return null;
        } else {
            Tutor tutor = findTutorUserName(userName);
            if (tutor != null) {
                Course groupCourse = em.find(Course.class, course.getCourseName());
                if (groupCourse == null) {
                    groupCourse = course;
                }
                if (!tutor.getCourses().contains(course)) {
                    tutor.addCourse(groupCourse);
                    groupCourse.addTutor(tutor);
                    em.persist(tutor);
                    em.flush();
                } else {
                    throw new CourseExistsException();
                }
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "A course exists already with email addresss {0}", tutor);
                return null;
            }
            return course;
        }
    }

    /**
     * Only a tutor can add a course that is not from the database. For new
     * course that isn't in database added by a tutor will be persisted to the
     * course table and courses_tutors table.
     *
     * @param course
     * @return the course to the bean
     * @author Keith <keithtran25@gmail.com>
     * Referenced code from Alvaro Monge <alvaro.monge@csulb.edu>
     */
    @RolesAllowed("tut4youapp.tutor")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Course addNewCourse(Course course) {
        String userName = getUsernameFromSession();
        if (userName == null) {
            return null;
        } else {
            Tutor tutor = findTutorUserName(userName);
            if (tutor != null) {
                tutor.addCourse(course);
                course.addTutor(tutor);
                em.merge(tutor);
                em.persist(course);
            } else {
                return null;
            }
            return course;
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
    public List<Course> getTutorCourses() {
        String userName = getUsernameFromSession();
        String email;
        if (userName == null) {
            return null;
        } else {
            Tutor tutor = findTutorUserName(userName);
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
        String userName = getUsernameFromSession();
        String email;
        if (userName == null) {
            return null;
        } else {
            Tutor tutor = findTutorUserName(userName);
            email = tutor.getEmail();
            TypedQuery<Availability> availabilityQuery = em.createNamedQuery(Availability.FIND_AVAILABILITY_BY_TUTOR, Availability.class);
            availabilityQuery.setParameter("email", email);
            return availabilityQuery.getResultList();
        }
    }

    /**
     * Only a tutor can view the list of courses that they can teach.
     * @return the list of courses to the bean
     * @author: Syed Haider <shayder426@gmail.com>
     */
    @RolesAllowed("tut4youapp.tutor")
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Availability> getAvailabilityList() {
        String userName = getUsernameFromSession();
        String email;
        if (userName == null) {
            return null;
        } else {
            Tutor tutor = findTutorUserName(userName);
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
    public Availability addAvailability(Availability availability, Date startTime) {
        String userName = getUsernameFromSession();
        if (userName == null) {
            return null;
        } else {
            Tutor tutor = findTutorUserName(userName);
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
    public void updateAvailability(Availability availability, Date startTime,
            Date endTime
    ) {
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
     *
     * @param availability
     * @author Syed Haider <shayder426@gmail.com>
     */
    @RolesAllowed("tut4youapp.tutor")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteAvailability(Availability availability
    ) {
        System.out.println("Have I made it here yet?");
        Availability toBeDeleted = em.find(Availability.class, availability.getId());
        if (toBeDeleted == null) {
            toBeDeleted = availability;
        }
        System.out.println(toBeDeleted);
        String userName = getUsernameFromSession();
        Tutor tutor = findTutorUserName(userName);
//        tutor.removeAvailability(toBeDeleted);

        em.merge(tutor);
        em.remove(em.merge(availability));
    }

    /**
     * A tutor can set their status to be available to receive notifications and
     * set their status to be not available to not receive notifications.
     *
     * @param doNotDisturb
     * @return
     */
    @RolesAllowed("tut4youapp.tutor")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean updateDoNotDisturb(Boolean doNotDisturb
    ) {
        String userName = getUsernameFromSession();
        Tutor tutor = findTutorUserName(userName);
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
     * Gets a logged in username by getting the username from the session.
     *
     * @return username Source:
     * https://dzone.com/articles/liferay-jsf-how-get-current-lo Had further
     * help by Subject2Change group.
     */
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public String getUsernameFromSession() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        String userName = request.getRemoteUser();
        return userName;
    }

    /**
     * Gets a student by finding the username student entity.
     *
     * @param username
     * @return a student
     */
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)

    public User find(String username
    ) {
        return em.find(User.class, username);
    }

    /**
     * Gets a tutor by finding the username in the tutor entity.
     *
     * @param username
     * @return tutor
     * @Keith <keithtran25@gmail.com>
     */
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Tutor findTutorUserName(String username
    ) {
        return em.find(Tutor.class, username);
    }

    /**
     * Registers user as a student. The student will be added a student role.
     *
     * @param student
     * @param groupName
     * @throws StudentExistsException Referenced code from Alvaro Monge
     * <alvaro.monge@csulb.edu>
     */
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void registerStudent(User student, String groupName) throws StudentExistsException {
        // 1: Use security EJB to add username/password to security
        // 2: if successful, then add as a registered student
        if (null == em.find(User.class, student.getEmail())) {
            Group group = em.find(Group.class, groupName);
            if (group == null) {
                group = new Group(groupName);
            }
            student.addGroup(group);
            group.addStudent(student);
            em.persist(student);
            em.flush();
        } else {
            throw new StudentExistsException();
        }
    }

    /**
     * Registers user as a tutor. The student will be added a student and tutor
     * role.
     *
     * @param tutor
     * @param groupName
     * @param groupName2
     * @throws StudentExistsException Referenced code from Alvaro Monge
     * <alvaro.monge@csulb.edu>
     */
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void registerTutor(Tutor tutor, String groupName,
            String groupName2) throws StudentExistsException {
        // 1: Use security EJB to add username/password to security
        // 2: if successful, then add as a registered tutor
        if (null == em.find(Tutor.class, tutor.getEmail())) {
            Group group = em.find(Group.class, groupName);
            Group group2 = em.find(Group.class, groupName2);
            if (group == null) {
                group = new Group(groupName);
                group2 = new Group(groupName2);
            }
            tutor.addGroup(group);
            tutor.addGroup(group2);
            group.addTutor(tutor);
            group2.addTutor(tutor);
            em.persist(tutor);
            em.flush();
        } else {
            throw new StudentExistsException();
        }
    }
    
//    /**
//     * Converts student to be a tutor. The student will be added a tutor role.
//     * @param user
//     * @param groupName
//     * https://stackoverflow.com/questions/20098791/jpa-inheritance-change-the-entity-type
//     */
//    //IN PROGRESS
//    @PermitAll
//    @TransactionAttribute(TransactionAttributeType.REQUIRED)
//    public void addTutorRole(User user, String groupName) {
//        if (user.getDecriminatorValue().equals("Student")) {
//            em.createNativeQuery("UPDATE Users SET user_type='Tutor'").setParameter("email",user.getEmail()).executeUpdate();
//            Group group = em.find(Group.class, groupName);
//            if (group == null) {
//                group = new Group(groupName);
//            }
//            user.addGroup(group);
//            group.addStudent(user);
//            em.flush();
//        }
//    }
//}

    /**
     * Converts student to be a tutor. The student will be added a tutor role.
     *
     * @param user
     * @param userType
     * @throws tut4you.exception.StudentExistsException
     */
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void registerUser(User user, String userType) throws StudentExistsException {
        Group group = em.find(Group.class, "tut4youapp.student");
        if (group == null) {
            group = new Group("tut4youapp.student");
        }
        if (userType.equals("Student")) {
            user.addGroup(group);
            group.addStudent(user);
            em.persist(user);
        } else {
            Tutor newTutor = new Tutor(user);
            newTutor.addGroup(group);
            group.addTutor(newTutor);
            group = em.find(Group.class, "tut4youapp.tutor");
            newTutor.addGroup(group);
            group.addTutor(newTutor);
            em.persist(newTutor);
        }
        em.flush();
    }

    /**
     * This method can only be called by a student. This methods gets the
     * username of the current session and checks if the username is null, if so
     * return null. Otherwise, find the user email to add the request to be
     * submitted.
     *
     * @param rating
     * @param request to be submitted
     * @return request if successful
     */
    @RolesAllowed("tut4youapp.student")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Rating newRating(Rating rating, Tutor tutor
    ) {
        String userName = getUsernameFromSession();
        if (userName == null) {
            return null;
        } else {
            User student = find(userName);
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
     * This method can only be called by a student. This methods gets the
     * username of the current session and checks if the username is null, if so
     * return null. Otherwise, find the user email to add the request to be
     * submitted.
     *
     * @param rating
     * @param description
     * @param ratingValue
     * @param request to be submitted
     * @return request if successful
     */
    @RolesAllowed("tut4youapp.student")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void updateRating(Rating rating, String description,
            Integer ratingValue
    ) {
        Date date = new Date();
        Rating updatedRating = em.find(Rating.class, rating.getId());
        System.out.println("test from ejb");
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
     * @param availability
     * @author Syed Haider <shayder426@gmail.com>
     */
    @RolesAllowed("tut4youapp.student")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteRating(Rating rating) {
        Rating toBeDeleted = em.find(Rating.class, rating.getId());
        if (toBeDeleted == null) {
            toBeDeleted = rating;
        }
        System.out.println(toBeDeleted);
        String userName = getUsernameFromSession();
        Tutor tutor = findTutorUserName(userName);
//        tutor.removeAvailability(toBeDeleted);

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
        String userName = getUsernameFromSession();
        String email;
        if (userName == null) {
            return null;
        } else {
            Tutor tutor = findTutorUserName(userName);
            email = tutor.getEmail();
            TypedQuery<Rating> ratingQuery = em.createNamedQuery(Rating.FIND_RATING_BY_TUTOR, Rating.class);
            ratingQuery.setParameter("email", email);
            return ratingQuery.getResultList();
        }
    }

    /**
     *
     * @return a list of requests from a user
     */
    @RolesAllowed("tut4youapp.student")
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Request> getCompletedRequests() {
        String userName = getUsernameFromSession();
        String email;
        if (userName == null) {
            return null;
        } else {
            User user = find(userName);
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
     * @param r
     */
    @RolesAllowed("tut4youapp.tutor")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void setRequestToComplete(Request r
    ) {
        String userName = getUsernameFromSession();
        Tutor tutor = findTutorUserName(userName);
        r.setStatus(Request.Status.COMPLETED);
        r.setTutor(tutor);
        em.merge(r);
        em.flush();
    }

    /**
     * Only students can see the number of tutors that tutors the requested
     * course. Finds all tutors that teaches the course.
     *
     * @param course selected course to be tutored
     * @return the number of tutors that tutors the course
     * @author Andrew Kaichi <ahkaichi@gmail.com>
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
        int value;
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
        System.out.println(dates);
        //System.out.println(dates);
        return 0;
    }

    

    @PermitAll
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public boolean checkEmail(String email) {
        String userName = getUsernameFromSession();
        System.out.println("Tut4YouApp " + userName.equals(email));
        return userName.equals(email);
    }

   

}
