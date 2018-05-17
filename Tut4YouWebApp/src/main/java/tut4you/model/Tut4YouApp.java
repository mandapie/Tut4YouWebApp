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

import com.paypal.exception.ClientActionRequiredException;
import com.paypal.exception.HttpErrorException;
import com.paypal.exception.InvalidCredentialException;
import com.paypal.exception.InvalidResponseDataException;
import com.paypal.exception.MissingCredentialException;
import com.paypal.exception.SSLConfigurationException;
import com.paypal.sdk.exceptions.OAuthException;
import com.paypal.svcs.services.AdaptivePaymentsService;
import com.paypal.svcs.types.ap.PayRequest;
import com.paypal.svcs.types.ap.PayResponse;
import com.paypal.svcs.types.ap.Receiver;
import com.paypal.svcs.types.ap.ReceiverList;
import com.paypal.svcs.types.common.RequestEnvelope;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import javax.ejb.Stateless;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import tut4you.exception.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.persistence.NoResultException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import static org.omnifaces.util.Faces.getServletContext;
import tut4you.controller.PaymentBean;
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

    /* @POST
    @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public Response createBook(Rating rating) {
        em.persist(rating);
        URI bookUri = uriInfo.getAbsolutePathBuilder().path(rating.getId().toString()).build();
        return Response.created(bookUri).build();
    }*/
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
     * Query all subjects from the database
     *
     * @return List of subjects
     */
    @RolesAllowed("tut4youapp.moderator")
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<ModeratorApplication> getModeratorApplications() {
        TypedQuery<ModeratorApplication> query = em.createNamedQuery(ModeratorApplication.FIND_ALL_MODERATOR_APPLICATIONS, ModeratorApplication.class);
        query.setParameter("applicationStatus", ModeratorApplication.ApplicationStatus.PENDING);
        return query.getResultList();
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
     * Based on the selected course, query all the questions
     * @param courseName is course name
     * @return List of questions
     */
    @RolesAllowed("tut4youapp.student")
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Question> getQuestions(String courseName) {
        TypedQuery<Question> questionQuery = em.createNamedQuery(Question.FIND_QUESTION_BY_COURSE, Question.class);
        questionQuery.setParameter("name", courseName);
        return questionQuery.getResultList();
    }
    
        /**
     * Based on the selected question, query all the responses
     * @param questionTitle is the question's title
     * @return List of responses
     */
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Responses> getResponses(String questionTitle) {
        TypedQuery<Responses> responsesQuery = em.createNamedQuery(Responses.FIND_RESPONSES_BY_QUESTION, tut4you.model.Responses.class);
        responsesQuery.setParameter("title", questionTitle);
        return responsesQuery.getResultList();
    }
    
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Question findQuestionTitle(String title){
        try{
            TypedQuery<Question> questionQuery = em.createNamedQuery(Question.FIND_QUESTION_BY_TITLE, Question.class);
            questionQuery.setParameter("title", title);
            return questionQuery.getSingleResult(); 
        }
        catch(NoResultException nre){
            return null;
        }
        
    }
    
    /**
     * Adds a question to the database
     * @param question new question being added
     * @return question 
     */
    @RolesAllowed("tut4youapp.student")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Question askNewQuestion(Question question){
        UserBean userBean = new UserBean();
        Course course;
        String currentUserEmail = userBean.getEmailFromSession();
        if (currentUserEmail == null) {
            return null;
        }
        else {
            User student = findUser(currentUserEmail);
            if (student != null){
                student.addQuestion(question);
                question.setStudent(student);
                course = question.getCourse();
                course.addQuestion(question);
            }
            else{
                return null;
            }
        }
        em.persist(question);
        em.flush();
        return question;
    }
    
    /**
     * Adds a question to the database
     * @param responses new question being added
     * @return question 
     */
    @RolesAllowed("tut4youapp.tutor")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Responses responses(Responses responses){
        UserBean userBean = new UserBean();
        Question question;
        String currentUserEmail = userBean.getEmailFromSession();
        if (currentUserEmail == null) {
            return null;
        }
        else {
            Tutor tutor = findTutor(currentUserEmail);
            if (tutor != null){
                tutor.addResponses(responses);
                responses.setTutor(tutor);
                question = responses.getQuestion();
                question.addResponses(responses);
                System.out.println("inside responses ejb");
                System.out.println(question.getTitle());
            }
            else{
                return null;
            }
        }
        em.persist(responses);
        em.flush();
        return responses;
    }
    /**
     * This method can only be called by a student. This methods gets the
     * username of the current session and checks if the username is null, if so
     * return null. Otherwise, findUser the user email to add the request to be
     * submitted.
     *
     * @param request to be submitted
     * @return request if successful
     */
    @RolesAllowed("tut4youapp.student")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Request newRequest(Request request) {
        UserBean userBean = new UserBean();
        String currentUserEmail = userBean.getEmailFromSession();
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
     * Find the current Tutor that is logged in
     *
     * @return
     */
    @RolesAllowed("tut4youapp.student")
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Tutor findCurrentTutor() {
        UserBean userBean = new UserBean();
        String currentUserEmail = userBean.getEmailFromSession();
        Tutor tutor;
        if (currentUserEmail == null) {
            tutor = null;
        } else {
            tutor = findTutor(currentUserEmail);
        }
        return tutor;
    }

    /**
     *
     * @return a list of requests from a user
     */
    @RolesAllowed("tut4youapp.student")
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Request> getActiveRequest() {
        UserBean userBean = new UserBean();
        String currentUserEmail = userBean.getEmailFromSession();
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
     * Gets a list of accepted requests
     *
     * @return a list of requests from a user
     */
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Request> getAcceptedRequestList() {
        UserBean userBean = new UserBean();
        String currentUserEmail = userBean.getEmailFromSession();
        if (currentUserEmail == null) {
            return null;
        }
        Tutor tutor = findTutor(currentUserEmail);
        if (tutor == null) {
            User user = findUser(currentUserEmail);
            String email = user.getEmail();

            TypedQuery<Request> requestQuery = em.createNamedQuery(Request.FIND_REQUEST_BY_EMAIL, Request.class);
            requestQuery.setParameter("student_email", email);
            requestQuery.setParameter("status", Request.Status.ACCEPTED);
            List<Request> list = requestQuery.getResultList();

            return list;
        } else {
            String email = currentUserEmail;
            TypedQuery<Request> requestQuery = em.createNamedQuery(Request.FIND_REQUEST_BY_TUTOR_EMAIL, Request.class);
            requestQuery.setParameter("tutor_email", email);
            requestQuery.setParameter("status", Request.Status.ACCEPTED);
            List<Request> list = requestQuery.getResultList();

            TypedQuery<Request> request2Query = em.createNamedQuery(Request.FIND_REQUEST_BY_EMAIL, Request.class);
            request2Query.setParameter("student_email", email);
            request2Query.setParameter("status", Request.Status.ACCEPTED);
            List<Request> list2 = request2Query.getResultList();

            List<Request> acceptedRequest = new ArrayList<>(list);
            acceptedRequest.addAll(list2);

            return acceptedRequest;
        }
    }

    /**
     *
     * @return a list of requests from a user
     */
    @RolesAllowed("tut4youapp.student")
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Request> getDeclinedRequest() {
        UserBean userBean = new UserBean();
        String currentUserEmail = userBean.getEmailFromSession();
        String email;
        if (currentUserEmail == null) {
            return null;
        } else {
            User user = findUser(currentUserEmail);
            email = user.getEmail();
            TypedQuery<Request> declined = em.createNamedQuery(Request.FIND_REQUEST_BY_EMAIL, Request.class
            );
            declined.setParameter("student_email", email);
            declined.setParameter("status", Request.Status.DECLINED);
            return declined.getResultList();
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
        Request pendingRequest = em.find(Request.class, r.getId());
        pendingRequest.setStatus(Request.Status.CANCELLED);
        for (Tutor t : pendingRequest.getAvailableTutors()) {
            t.removePendingRequest(pendingRequest);
            em.merge(t);
        }
        pendingRequest.removeAllAvailableTutor(r.getAvailableTutors());
        em.merge(pendingRequest);
        em.flush();
    }

    /**
     * A student can cancel pending requests
     *
     * @param r
     */
    @RolesAllowed("tut4youapp.tutor")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void declineRequest(Request r) {
        Request pendingRequest = em.find(Request.class, r.getId());
        r.setStatus(Request.Status.DECLINED);
        Tutor tutor = r.getTutor();
        tutor.removePendingRequest(pendingRequest);
        pendingRequest.removeAvailableTutor(tutor);
        em.merge(r);
        em.merge(tutor);
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
    @RolesAllowed("tut4youapp.student")
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public long getNumOfTutorsFromCourse(String course) {
        TypedQuery<Long> courseTutorQuery = em.createNamedQuery(Tutor.FIND_TUTORS_BY_COURSE, Long.class);
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
     * @param zipCode
     * @return the number of tutors that tutors the course
     * @author Andrew Kaichi <ahkaichi@gmail.com>
     * @author Keith Tran <keithtran25@gmail.com>
     * @author Amanda Pan <daikiraidemodaisui@gmail.com>
     */
    @RolesAllowed("tut4youapp.student")
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Tutor> getTutorsFromCourse(String course, String dayOfWeek, java.util.Date time, Boolean doNotDisturb, String zipCode) {
        TypedQuery<Tutor> courseTutorQueryD = em.createNamedQuery(Tutor.FIND_TUTORS_BY_COURSE_DAY_TIME_DZIP, Tutor.class);
        courseTutorQueryD.setParameter("coursename", course);
        courseTutorQueryD.setParameter("dayofweek", dayOfWeek);
        courseTutorQueryD.setParameter("requestTime", time, TemporalType.TIME);
        courseTutorQueryD.setParameter("doNotDisturb", false);
        courseTutorQueryD.setParameter("zipCode", zipCode);
        TypedQuery<Tutor> courseTutorQueryC = em.createNamedQuery(Tutor.FIND_TUTORS_BY_COURSE_DAY_TIME_CZIP, Tutor.class);
        courseTutorQueryC.setParameter("coursename", course);
        courseTutorQueryC.setParameter("dayofweek", dayOfWeek);
        courseTutorQueryC.setParameter("requestTime", time, TemporalType.TIME);
        courseTutorQueryC.setParameter("doNotDisturb", false);
        courseTutorQueryC.setParameter("zipCode", zipCode);
        //print to test
        System.out.println("ZIPCODE: " + zipCode);
        System.out.println("COURSENAME: " + course);
        System.out.println("DAYOFWEEK: " + dayOfWeek);
        System.out.println("REQUESTTIME: " + time);
        List<Tutor> allAvailableTutors = new ArrayList();
        allAvailableTutors.addAll(courseTutorQueryD.getResultList());
        for (Tutor x : courseTutorQueryC.getResultList()) {
            if (!allAvailableTutors.contains(x)) {
                allAvailableTutors.add(x);
            }
        }
        return allAvailableTutors;
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
        UserBean userBean = new UserBean();
        String currentUserEmail = userBean.getEmailFromSession();
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
        UserBean userBean = new UserBean();
        String currentUserEmail = userBean.getEmailFromSession();
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
     *
     * @return
     */
    @RolesAllowed("tut4youapp.tutor")
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Request> getPendingRequestForTutor() {
        UserBean userBean = new UserBean();
        String currentUserEmail = userBean.getEmailFromSession();
        Tutor tutor = findTutor(currentUserEmail);
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
     * @author Keith Tran <keithtran25@gmai l.com>
     * @author Amanda Pan <daikiraidemodaisuki@gmail.com>
     * Referenced code from Alvaro Monge <alvaro.monge@csulb.edu>
     */
    @RolesAllowed("tut4youapp.tutor")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Course addCourse(Course course) throws CourseExistsException {
        UserBean userBean = new UserBean();
        String currentUserEmail = userBean.getEmailFromSession();
        if (currentUserEmail == null) {
            return null;
        } else {
            Tutor tutor = findTutor(currentUserEmail);
            Course groupCourse = em.find(Course.class,
                    course.getCourseName());
            if (groupCourse == null) {
                groupCourse = course;
            }
            if (!tutor.getCourses().contains(course)) {
                tutor.addCourse(groupCourse);
                groupCourse.addTutor(tutor);
                em.merge(tutor);
                em.flush();
            } else {
                throw new CourseExistsException();
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
     * @author Amanda Pan <daikiraidemodaisuki@gmail.com>
     * Referenced code from Alvaro Monge <alvaro.monge@csulb.edu>
     * @throws tut4you.exception.CourseExistsException
     */
    @RolesAllowed("tut4youapp.tutor")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Course addNewCourse(Course course) throws CourseExistsException {
        UserBean userBean = new UserBean();
        String currentUserEmail = userBean.getEmailFromSession();
        if (currentUserEmail == null) {
            return null;
        } else {
            Tutor tutor = findTutor(currentUserEmail);
            Course groupCourse = em.find(Course.class,
                    course.getCourseName());
            if (groupCourse == null) {
                groupCourse = course;
                tutor.addCourse(groupCourse);
                groupCourse.addTutor(tutor);
                em.persist(groupCourse);
            } else {
                throw new CourseExistsException();
            }
            return groupCourse;
        }
    }

    /**
     * Only a tutor can delete his/her course
     *
     * @author Syed Haider <shayder426@gmail.com>
     * @param course
     */
    @RolesAllowed("tut4youapp.tutor")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteCourse(Course course) {
        UserBean userBean = new UserBean();
        String currentUserEmail = userBean.getEmailFromSession();
        Course toBeDeleted = em.find(Course.class, course.getCourseName());
        if (toBeDeleted == null) {
            toBeDeleted = course;
        }
        Tutor tutor = findTutor(currentUserEmail);
        Course groupCourse = em.find(Course.class, course.getCourseName());
        tutor.removeCourse(course);
        groupCourse.removeTutor(tutor);
        em.merge(tutor);
        em.merge(groupCourse);
        em.flush();
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
        UserBean userBean = new UserBean();
        String currentUserEmail = userBean.getEmailFromSession();
        String email;
        if (currentUserEmail == null) {
            return null;
        } else {
            Tutor tutor = findTutor(currentUserEmail);
            email = tutor.getEmail();
            TypedQuery<Course> courseQuery = em.createNamedQuery(Course.FIND_COURSES_BY_TUTOR, Course.class
            );
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
        UserBean userBean = new UserBean();
        String currentUserEmail = userBean.getEmailFromSession();
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
        UserBean userBean = new UserBean();
        String currentUserEmail = userBean.getEmailFromSession();
        String email;
        if (currentUserEmail == null) {
            return null;
        } else {
            Tutor tutor = findTutor(currentUserEmail);
            email = tutor.getEmail();
            TypedQuery<Availability> availabilityQuery = em.createNamedQuery(Availability.FIND_AVAILABILITY_BY_TUTOR, Availability.class
            );
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
     * @throws tut4you.exception.AvailabilityExistsException
     */
    @RolesAllowed("tut4youapp.tutor")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Availability addAvailability(Availability availability) throws AvailabilityExistsException {
        UserBean userBean = new UserBean();
        String currentUserEmail = userBean.getEmailFromSession();
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
                throw new AvailabilityExistsException();
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
        Availability updatedAvailability = em.find(Availability.class,
                availability.getId());
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
    public void deleteAvailability(Availability availability) {
        Availability toBeDeleted = em.find(Availability.class, availability.getId());
        if (toBeDeleted == null) {
            toBeDeleted = availability;
        }
        em.remove(toBeDeleted);
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
    public boolean switchDoNotDisturb(Boolean doNotDisturb) {
        UserBean userBean = new UserBean();
        String currentUserEmail = userBean.getEmailFromSession();
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
     *
     * @param email
     * @return user email
     */
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)

    public User findUser(String email) {
        return em.find(User.class, email);
    }

    public User findUserByUsername(String username) {
        TypedQuery<User> query = em.createNamedQuery(User.FIND_USER_BY_UNAME, User.class);
        query.setParameter("username", username);
        return query.getSingleResult();

    }

    /**
     * Gets a user by finding the email in the user entity.
     *
     * @param email
     * @return user email
     * @author Keith Tran <keithtran25@gmail.com>
     */
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public FlaggedUser findflaggeduser(String email) {
        return em.find(FlaggedUser.class, findUser(email).getFlaggedUser().getId());
    }

    /**
     * Gets a tutor by finding the email in the tutor entity.
     *
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
     * Gets a tutor by finding the email in the tutor entity.
     *
     * @param username
     * @return tutor email
     * @Keith <keithtran25@gmail.com>
     */
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Tutor findTutorByUsername(String username) {
        TypedQuery<Tutor> tutorQuery = em.createNamedQuery(Tutor.FIND_TUTOR_BY_USERNAME, Tutor.class);
        tutorQuery.setParameter("username", username);
        return tutorQuery.getSingleResult();
    }

    /**
     * Find low rating tutors of 2 stars or lower
     *
     * @return Low rating tutor
     * @Keith <keithtran25@gmail.com>
     */
    @RolesAllowed("tut4youapp.moderator")
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Tutor> findLowRatingTutors() {
        TypedQuery<Tutor> query = em.createNamedQuery(Tutor.FIND_LOW_RATING_TUTORS, Tutor.class);
        query.setParameter("overallRating", 2);

        return query.getResultList();
    }

    /**
     * Gets a moderatorApplication by finding the email in the entity.
     *
     * @param username
     * @return moderator application
     * @Keith <keithtran25@gmail.com>
     */
    @RolesAllowed("tut4youapp.moderator")
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public ModeratorApplication findModeratorApplication(String username) {
        TypedQuery<ModeratorApplication> query = em.createNamedQuery(ModeratorApplication.FIND_MODERATOR_APPLICATION_BY_UNAME, ModeratorApplication.class);
        query.setParameter("username", username);
        return query.getSingleResult();
    }

    /**
     * Gets a complaint by finding the id in the entity.
     *
     * @param id
     * @return moderator application
     * @Keith <keithtran25@gmail.com>
     */
    @RolesAllowed("tut4youapp.moderator")
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Complaint findComplaint(int id) {
        TypedQuery<Complaint> query = em.createNamedQuery(Complaint.FIND_COMPLAINT_BY_ID, Complaint.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }
    /**
     * Gets a request by finding the id in the entity.
     *
     * @param id
     * @return moderator application
     * @Keith <keithtran25@gmail.com>
     */
    @RolesAllowed("tut4youapp.moderator")
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Request findRequest(int id) {
        TypedQuery<Request> query = em.createNamedQuery(Request.FIND_REQUEST_BY_ID, Request.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    /**
     * Converts student to be a tutor. The student will be added a tutor role.
     *
     * @param user
     * @param userType
     * @param priceRate
     * @param defaultZip
     * @param joinedDateAsTutor
     * @throws tut4you.exception.UserExistsException
     */
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void registerUser(User user, String userType, double priceRate, String defaultZip, Date joinedDateAsTutor) throws UserExistsException {
        if (null == em.find(User.class,
                user.getEmail())) {
            Group group = em.find(Group.class, "tut4youapp.student");
            User newStudent = new User(user);
            if (group == null) {
                group = new Group("tut4youapp.student");
            }
            if (userType.equals("Student")) {
                newStudent.addGroup(group);
                group.addStudent(newStudent);
                em.persist(newStudent);
            } else {
                Tutor newTutor = new Tutor(user);
                newTutor.setDateJoinedAsTutor(joinedDateAsTutor);
                newTutor.setHourlyRate(priceRate);
                newTutor.setDefaultZip(defaultZip);
                newTutor.addGroup(group); //Add user a student role
                group.addTutor(newTutor);
                group = em.find(Group.class, "tut4youapp.tutor");
                newTutor.addGroup(group); //Add user a tutor role
                group.addTutor(newTutor);
                em.persist(newTutor);

            }
            em.flush();
        } else {
            throw new UserExistsException();
        }
    }

    /**
     * This method can only be called by a student. This methods gets the
     * username of the current session and checks if the username is null, if so
     * return null. Otherwise, findUser the user email to add the rating to be
     * submitted.
     *
     * @param tutor the tutor receiving the rating
     * @param rating to be submitted
     * @return rating if successful
     */
    @RolesAllowed("tut4youapp.student")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Rating createRating(Rating rating, Tutor tutor) {
        UserBean userBean = new UserBean();
        String currentUserEmail = userBean.getEmailFromSession();
        Date date = new Date();
        if (currentUserEmail == null) {
            return null;
        } else {
            User student = findUser(currentUserEmail);
            rating.setDateRated(date);
            if (student != null) {
                student.addRating(rating);
                rating.setStudent(student);
                tutor.addRating(rating);
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
     * This method can only be called by a student. It will update the rating
     * that a student has previously submitted.
     *
     * @param rating the rating being updated
     * @param description the description being updated
     * @param ratingValue the ratingValue being updated
     * @author Syed Haider <shayder426@gmail.com>
     */
    @RolesAllowed("tut4youapp.student")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void updateRating(Rating rating, String description, Integer ratingValue) {
        Rating updatedRating = em.find(Rating.class, rating.getId());
        if (updatedRating == null) {
            updatedRating = rating;
        }
        Date date = new Date();
        updatedRating.setDateRated(date);
        updatedRating.setDescription(description);
        updatedRating.setRatingValue(ratingValue);
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
        UserBean userBean = new UserBean();
        String currentUserEmail = userBean.getEmailFromSession();
        Rating toBeDeleted = em.find(Rating.class,
                rating.getId());
        if (toBeDeleted == null) {
            toBeDeleted = rating;
        }
        User user = findUser(currentUserEmail);
        em.merge(user);
        em.remove(toBeDeleted);
    }

    /**
     * Get a list of ratings of a tutor.
     *
     * @param tutorEmail
     * @return the list of courses to the bean
     * @author: Syed Haider <shayder426@gmail.com>
     */
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Rating> getRatingList(String tutorEmail) {
        UserBean userBean = new UserBean();
        String currentUserEmail = userBean.getEmailFromSession();
        String email;
        /*Checks to see if tutor is viewing reviews or student
        is looking at a tutor's profile
         */
        if (!currentUserEmail.equals(tutorEmail)) {
            currentUserEmail = tutorEmail;
        }
        if (currentUserEmail == null) {
            return null;
        } else {
            Tutor tutor = findTutor(currentUserEmail);
            email = tutor.getEmail();
            TypedQuery<Rating> ratingQuery = em.createNamedQuery(Rating.FIND_RATING_BY_TUTOR, Rating.class
            );
            ratingQuery.setParameter("email", email);
            return ratingQuery.getResultList();
        }
    }

    /**
     * Get a list of ratings of a tutor.
     *
     * @param ratingEmail
     * @return the list of courses to the bean
     * @author: Syed Haider <shayder426@gmail.com>
     */
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public boolean checkIfUserRated(String ratingEmail) {
        UserBean userBean = new UserBean();
        String currentUserEmail = userBean.getEmailFromSession();
        return ratingEmail.equals(currentUserEmail);
    }

    /**
     *
     * @return a list of requests from a user
     */
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Request> getCompletedRequests() {
        UserBean userBean = new UserBean();
        String currentUserEmail = userBean.getEmailFromSession();
        Tutor tutor = findTutor(currentUserEmail);
        if (currentUserEmail == null) {
            return null;
        }
        if (tutor == null) {
            User user = findUser(currentUserEmail);
            String email = user.getEmail();

            TypedQuery<Request> requestQuery = em.createNamedQuery(Request.FIND_REQUEST_BY_EMAIL, Request.class);
            requestQuery.setParameter("student_email", email);
            requestQuery.setParameter("status", Request.Status.COMPLETED);
            List<Request> list = requestQuery.getResultList();

            return list;
        } else {
            String email = currentUserEmail;
            TypedQuery<Request> requestQuery = em.createNamedQuery(Request.FIND_REQUEST_BY_TUTOR_EMAIL, Request.class);
            requestQuery.setParameter("tutor_email", email);
            requestQuery.setParameter("status", Request.Status.COMPLETED);
            List<Request> list = requestQuery.getResultList();

            TypedQuery<Request> request2Query = em.createNamedQuery(Request.FIND_REQUEST_BY_EMAIL, Request.class);
            request2Query.setParameter("student_email", email);
            request2Query.setParameter("status", Request.Status.COMPLETED);
            List<Request> list2 = request2Query.getResultList();

            List<Request> acceptedRequest = new ArrayList<>(list);
            acceptedRequest.addAll(list2);

            return acceptedRequest;
        }
    }

    /**
     * Sets a tutor to the request when a tutor completes the request. IN
     * PROGRESS
     *
     * @param r request that is being partaken
     * @param sessionTimer
     * @return
     */
    @RolesAllowed("tut4youapp.tutor")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Session startSessionTime(Request r, Session sessionTimer) {
        Request request = em.find(Request.class, r.getId());
        Date startTime = new Date();
        sessionTimer.setStartSessionTime(startTime);
        request.setSession(sessionTimer);
        sessionTimer.setRequest(request);
        em.persist(sessionTimer);
        em.merge(request);
        em.flush();
        return sessionTimer;
    }

    /**
     * Sets a tutor to the request when a tutor completes the request.
     *
     * @param r the request to be set to completed
     * @param sessionTimer
     * @return
     */
    @RolesAllowed("tut4youapp.tutor")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public String endSessionTime(Request r, Session sessionTimer) {
        Date endTime = new Date();
        //Session session = em.find(Session.class, sessionTimer.getId());
        sessionTimer.setEndSessionTime(endTime);
        UserBean userBean = new UserBean();
        String currentUserEmail = userBean.getEmailFromSession();
        double elapsedTime = endTime.getTime() - sessionTimer.getStartSessionTime().getTime();
        double minutes = (elapsedTime / 1000) / 60;
        double hours = minutes / 60;
        sessionTimer.setElapsedTimeOfSession(hours);
        em.merge(sessionTimer);
        Tutor tutor = findTutor(currentUserEmail);
        Request request = em.find(Request.class, r.getId());
        request.setStatus(Request.Status.COMPLETED);
        request.setTutor(tutor);
        em.merge(request);
        em.flush();
        return "sessionCompleted";
    }

    /**
     *
     * @param answer
     * @param email
     * @return
     */
    public boolean checkAnswer(String answer, String email) {
        User user = findUser(email);
        String securityAnswer = user.getSecurityAnswer();
        boolean val = securityAnswer.equals(answer);
        return val;
    }

    /**
     * Updates the average rating of the tutor
     *
     * @author Syed Haider <shayder426@gmail.com>
     * @param email
     */
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void updateAverageRating(String email) {
        TypedQuery<Double> averageRatingQuery = em.createNamedQuery(Rating.FIND_AVG_RATING_BY_TUTOR, Double.class);
        averageRatingQuery.setParameter("email", email);
        UserBean userBean = new UserBean();
        String currentUserEmail = userBean.getEmailFromSession();
        if (!currentUserEmail.equals(email)) {
            currentUserEmail = email;
        }
        Tutor tutor = findTutor(currentUserEmail);
        int avgRating = averageRatingQuery.getSingleResult().intValue();
        tutor.setOverallRating(avgRating);
        em.merge(tutor);
    }

    /**
     * Gets user in the database by email
     *
     * @param email
     * @author Amanda Pan <daikiraidemodaisuki@gmail.com>
     * @return user
     */
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public User getUser(String email) {
        TypedQuery<User> Query = em.createNamedQuery(User.FIND_USER_BY_EMAIL, User.class);
        Query.setParameter("email", email);
        return Query.getSingleResult();
    }

    /**
     *
     * @param transcriptFileLocation
     */
    @RolesAllowed("tut4youapp.tutor")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void addTranscriptFileLocation(String transcriptFileLocation) {
        UserBean userBean = new UserBean();
        String currentUserEmail = userBean.getEmailFromSession();
        Tutor tutor = findTutor(currentUserEmail);
        tutor.setTrancriptFileLocation(transcriptFileLocation);
        em.merge(tutor);
        em.flush();
    }

    @PermitAll
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void addResumeFileLocation(String resumeFilePath, String reason
    ) {
        UserBean userBean = new UserBean();
        String currentUserEmail = userBean.getEmailFromSession();
        //User user = findUser(currentUserEmail);
        Tutor tutor = findTutor(currentUserEmail);
        ModeratorApplication moderatorApplication = new ModeratorApplication(resumeFilePath, reason);
        if (tutor == null) {
            User student = findUser(currentUserEmail);
            student.setModeratorApplication(moderatorApplication);
            moderatorApplication.setUser(student);
            moderatorApplication.setApplicationStatus(ModeratorApplication.ApplicationStatus.PENDING);
            em.persist(moderatorApplication);
        } else {
            tutor.setModeratorApplication(moderatorApplication);
            moderatorApplication.setUser(tutor);
            moderatorApplication.setApplicationStatus(ModeratorApplication.ApplicationStatus.PENDING);
            em.persist(moderatorApplication);
        }
        em.flush();
        Group group = em.find(Group.class, "tut4youapp.moderator");
        List<String> userEmails = getUserEmails();

        for (int i = 0; i < userEmails.size(); i++) {
            User user = em.find(User.class, userEmails.get(i));
            if (user.getGroups().contains(group)) {
                user.addModeratorApplication(moderatorApplication);
                em.merge(user);
                em.flush();
            }

        }
    }

    /**
     * Decline the moderator application and do not set the applicant into a
     * moderator
     *
     * @param moderatorApplication
     * @param moderator
     */
    @RolesAllowed("tut4youapp.moderator")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void declineModeratorApplication(ModeratorApplication moderatorApplication) {
        UserBean userBean = new UserBean();
        String currentUserEmail = userBean.getEmailFromSession();
        User moderator = findUser(currentUserEmail);
        moderatorApplication.setModerator(moderator);
        moderatorApplication.setApplicationStatus(ModeratorApplication.ApplicationStatus.DECLINED);
        em.merge(moderatorApplication);
        em.flush();

    }

    /**
     * Accept the moderator application and turn the applicant into a moderator
     *
     * @param moderatorApplication
     */
    @RolesAllowed("tut4youapp.moderator")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void acceptModeratorApplication(ModeratorApplication moderatorApplication) {
        UserBean userBean = new UserBean();
        String currentUserEmail = userBean.getEmailFromSession();
        User moderator = findUser(currentUserEmail);
        User user = em.find(User.class, moderatorApplication.getUser().getEmail());
        ModeratorApplication moderatorApplicationClone = em.find(ModeratorApplication.class, moderatorApplication.getId());

        Group moderatorGroup = em.find(Group.class, "tut4youapp.moderator");

        moderatorApplicationClone.setModerator(moderator);
        moderator.addModeratorApplication(moderatorApplicationClone);
        moderatorApplicationClone.setApplicationStatus(ModeratorApplication.ApplicationStatus.ACCEPTED);

        moderatorApplicationClone.setUser(user);
        user.setModeratorApplication(moderatorApplicationClone);

        user.addGroup(moderatorGroup);
        moderatorGroup.addStudent(user);

        em.merge(moderatorApplicationClone);
        em.merge(user);

    }

    /**
     *
     * @param updateUser
     * @param hr
     */
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void updateUser(User updateUser, double hr) {
        UserBean userBean = new UserBean();
        String currentUserEmail = userBean.getEmailFromSession();
        Tutor tutor = findTutor(currentUserEmail);
        if (tutor == null) {
            em.merge(updateUser);
        } else {
            tutor = (Tutor) updateUser;
            tutor.setHourlyRate(hr);
            em.merge(tutor);
        }
        em.flush();
    }

    /**
     *
     * @param newPassword
     */
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void changePassword(String newPassword) {
        UserBean userBean = new UserBean();
        String currentUserEmail = userBean.getEmailFromSession();
        User user = findUser(currentUserEmail);
        if (user != null) {
            user.setPassword(newPassword);
            em.merge(user);
            em.flush();
        }
    }

    /**
     * update current zip code of tutor
     *
     * @param currentZip
     * @return tutor
     * @author Keith Tran <keithtran25@gmail.com>
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @RolesAllowed("tut4youapp.tutor")
    public Tutor updateCurrentZip(String currentZip) {
        UserBean userBean = new UserBean();
        String currentUserEmail = userBean.getEmailFromSession();
        Tutor tutor = findTutor(currentUserEmail);
        tutor.setCurrentZip(currentZip);
        em.merge(tutor);
        em.flush();
        return tutor;
    }

    /**
     * set the current zip of tutor as the default zip
     *
     * @return tutor
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @RolesAllowed("tut4youapp.tutor")
    public Tutor setDefaultToCurrentZip() {
        UserBean userBean = new UserBean();
        String currentUserEmail = userBean.getEmailFromSession();
        Tutor tutor = findTutor(currentUserEmail);
        tutor.setCurrentZip(tutor.getDefaultZip());
        em.merge(tutor);
        em.flush();
        return tutor;
    }

    /**
     * retrieve list of user email
     *
     * @return list of user email
     * @author Keith Tran <keithtran25@gmail.com>
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    @PermitAll
    public List<String> getUserEmails() {
        TypedQuery<String> Query = em.createNamedQuery(User.FIND_USER_EMAILS, String.class);
        return Query.getResultList();
    }

    /**
     *
     * @return
     */
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Double getHourlyRate() {
        UserBean userBean = new UserBean();
        String currentUserEmail = userBean.getEmailFromSession();
        TypedQuery<Double> Query = em.createNamedQuery(Tutor.FIND_HOURLY_RATE_BY_EMAIL, Double.class);
        Query.setParameter("email", currentUserEmail);
        return Query.getSingleResult();
    }

    /**
     *
     * @return
     */
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Date getDateJoinedAsTutor(String email) {
        UserBean userBean = new UserBean();
        //String currentUserEmail = userBean.getEmailFromSession();
        TypedQuery<Date> Query = em.createNamedQuery(Tutor.FIND_DATE_JOINED_BY_EMAIL, Date.class);
        Query.setParameter("email", email);
        return Query.getSingleResult();
    }

    /**
     * retrieve list of user usernames
     *
     * @return list of usernames
     * @author Keith Tran <keithtran25@gmail.com>
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    @PermitAll
    public List<String> getUserUserNames() {
        TypedQuery<String> Query = em.createNamedQuery(User.FIND_USER_USERNAMES, String.class);
        return Query.getResultList();
    }

    /**
     * Adds ZipCode to DB if it is not already in DB but first checks if it is
     * in the DB
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
        Query.setParameter("zipCode", zipCode.getCurrentZipCode());
        Query.setParameter("maxRadius", zipCode.getMaxRadius());
        if (Query.getResultList().isEmpty()) {
            em.persist(zipCode);
            em.flush();
        } else {
            zipCode = Query.getSingleResult();
        }
        return zipCode;
    }

    /**
     * Find ZipCodesByRadius of a ZipCode ID
     *
     * @param id
     * @return list of zip codes by radius
     */
    public List<String> findZipCodeByRadius(Long id) {
        TypedQuery<String> Query = em.createNamedQuery(ZipCodeByRadius.FIND_ZIPCODEBYRADIUS, String.class);
        Query.setParameter("id", id);
        return Query.getResultList();
    }

    /**
     * Add ZipCodeByRadius if it does not belong to zip code location
     *
     * @param zipCode
     * @param zipCodeByRadius
     * @return ZipCodeByRadius
     * @author Keith Tran <keithtran25@gmail.com>
     */
    @RolesAllowed("tut4youapp.student")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public ZipCodeByRadius addZipCodeByRadius(ZipCode zipCode, ZipCodeByRadius zipCodeByRadius) {
        boolean isZipCodeInDB = false;
        ZipCode findZipCode = em.find(ZipCode.class, zipCode.getId());
        ZipCodeByRadius zipCodeByRadiusTemp = em.find(ZipCodeByRadius.class, zipCodeByRadius.getZipCodeByRadius());

        List<String> zipCodesByRadiusList = findZipCodeByRadius(findZipCode.getId());

        if (zipCodeByRadiusTemp != null) {
            for (int i = 0; i < zipCodesByRadiusList.size(); i++) {
                if (zipCodesByRadiusList.get(i).equals(zipCodeByRadiusTemp.getZipCodeByRadius())) {
                    isZipCodeInDB = true;
                }
            }
        }
        if (isZipCodeInDB == false) {
            if (zipCodeByRadiusTemp == null) {
                zipCode.addZipCodeByRadius(zipCodeByRadius);
                zipCodeByRadius.addZipCode(zipCode);
                em.persist(zipCodeByRadius);
                em.flush();
            } else {
                zipCodeByRadiusTemp.addZipCode(zipCode);
                zipCode.addZipCodeByRadius(zipCodeByRadiusTemp);
                em.merge(zipCode);
                em.flush();
            }
        }
        return zipCodeByRadius;
    }

    /**
     * saves the message to the database
     *
     * @param message
     */
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveMessage(Message message) {
        em.persist(message);
        em.flush();
    }

    /**
     * Allows student to become a tutor after already registering as a student
     *
     * @param hourlyRate
     * @param dateJoinedAsTutor
     * @param defaultZip
     * @author Keith Tran <keithtran25@gmail.com>
     */
    @RolesAllowed("tut4youapp.student")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void becomeTutor(double hourlyRate, Date dateJoinedAsTutor, String defaultZip) {
        UserBean userBean = new UserBean();
        String currentUserEmail = userBean.getEmailFromSession();
        User clone = em.find(User.class, currentUserEmail);
        clone.setGroups(null);
        TypedQuery<Request> requestQuery = em.createNamedQuery(Request.FIND_REQUESTS_BY_USER, Request.class);
        requestQuery.setParameter("email", clone.getEmail());
        clone.setRequests(null);
        List<Request> requestsClone = requestQuery.getResultList();

        em.remove(clone);
        em.flush();
        Group group = em.find(Group.class, "tut4youapp.student");
        if (group == null) {
            group = new Group("tut4youapp.student");
        }
        Tutor tutor = new Tutor(clone);
        tutor.addGroup(group); //Add user a student role
        group.addTutor(tutor);
        group = em.find(Group.class, "tut4youapp.tutor");
        tutor.addGroup(group); //Add user a tutor role
        group.addTutor(tutor);
        tutor.setDateJoinedAsTutor(dateJoinedAsTutor);
        tutor.setHourlyRate(hourlyRate);
        tutor.setDefaultZip(defaultZip);

        tutor.setRequests(requestsClone);

        em.persist(tutor);
        em.flush();
    }

    /**
     * create a new complaint
     *
     * @param reportedUser
     * @param complaint
     * @author Keith Tran <keithtran25@gmail.com>
     */
    @RolesAllowed("tut4youapp.student")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void createNewComplaint(User reportedUser, Complaint complaint) {
        UserBean userBean = new UserBean();
        String currentUserEmail = userBean.getEmailFromSession();

        User user = em.find(User.class, currentUserEmail);
        
        complaint.setUser(user);
        complaint.setReportedUser(reportedUser);
        em.persist(complaint);
        em.flush();
    }

    /**
     * Query all complaints from the database
     *
     * @return list of complaints
     * @author Keith Tran <keithtran25@gmail.com>
     */
    @RolesAllowed("tut4youapp.moderator")
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Complaint> getComplaints() {
        TypedQuery<Complaint> query = em.createNamedQuery(Complaint.FIND_UNRESOLVED_COMPLAINTS, Complaint.class);
        query.setParameter("isReviewed", false);
        return query.getResultList();
    }

    /**
     * close complaint and set isReviewed boolean to true
     *
     * @param complaint
     * @author Keith Tran <keithtran25@gmail.com>
     */
    @RolesAllowed("tut4youapp.moderator")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void closeComplaint(Complaint complaint) {
        UserBean userBean = new UserBean();
        String currentUserEmail = userBean.getEmailFromSession();

        User moderator = em.find(User.class, currentUserEmail);

        complaint.setModerator(moderator);
        complaint.setIsReviewed(true);
        em.merge(complaint);
        em.flush();
    }

    /**
     * find flagged user based on user email
     *
     * @param email
     * @return flaggedUser
     * @author Keith Tran <keithtran25@gmail.com>
     */
    @RolesAllowed("tut4youapp.moderator")
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public FlaggedUser findFlaggedUser(String email) {
        TypedQuery<FlaggedUser> query = em.createNamedQuery(FlaggedUser.FIND_FLAGGED_USER, FlaggedUser.class);
        query.setParameter("email", email);

        FlaggedUser flaggedUser;
        if (query.getResultList().isEmpty()) {
            flaggedUser = new FlaggedUser();
        } else {
            flaggedUser = query.getSingleResult();
        }

        return flaggedUser;
    }

    /**
     * checks to see if user logging in was banned or suspended
     *
     * @param email
     * @return flaggedUser
     * @author Keith Tran <keithtran25@gmail.com>
     */
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public FlaggedUser checkFlaggedUserLogIn(String email) {
        TypedQuery<FlaggedUser> query = em.createNamedQuery(FlaggedUser.FIND_FLAGGED_USER, FlaggedUser.class);
        query.setParameter("email", email);

        FlaggedUser flaggedUser;

        if (query.getResultList().isEmpty()) {
            flaggedUser = null;
        } else {
            flaggedUser = query.getSingleResult();
        }

        return flaggedUser;
    }

    /**
     * Flags a User
     *
     * @param reportedUser is the user being flagged
     * @param dateFlagged is the date the user was flagged
     * @param type is whether it is a suspension or ban
     * @author Keith Tran <keithtran25@gmail.com>
     */
    @RolesAllowed("tut4youapp.moderator")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void flagUser(User reportedUser, Date dateFlagged, String type) {
        UserBean userBean = new UserBean();
        String currentUserEmail = userBean.getEmailFromSession();
        boolean newFlaggedUser = false;
        int count = 1;
        if (type.equals("ban")) {
            count = 4;
        }

        User moderator = em.find(User.class, currentUserEmail);
        FlaggedUser flaggedUser = checkFlaggedUserLogIn(reportedUser.getEmail());
        if (flaggedUser == null) {
            flaggedUser = new FlaggedUser();
            newFlaggedUser = true;
        }

        if (newFlaggedUser == true) {
            flaggedUser.setUser(reportedUser);
            reportedUser.setFlaggedUser(flaggedUser);
            flaggedUser.addModerator(moderator);
            moderator.addModeratorFlaggingUser(flaggedUser);
            flaggedUser.setCount(flaggedUser.getCount() + count);
            flaggedUser.setDateFlagged(dateFlagged);
            em.persist(flaggedUser);
        } else {
            if (!(flaggedUser.getModerators().contains(moderator))) {
                flaggedUser.addModerator(moderator);
                moderator.addModeratorFlaggingUser(flaggedUser);
            }
            flaggedUser.setCount(flaggedUser.getCount() + count);
            flaggedUser.setDateFlagged(dateFlagged);
            em.merge(flaggedUser);
        }
        em.flush();
    }

    /**
     * When a student clicks "Pay Now", it generates a paykey from the Pay
     * response.
     *
     * @param email - receiver of the payment
     * @param hourlyRate - hourly rate the tutor charges
     * @param elapsedTimeOfSession - total time of a session
     * @return payKey - allows student to complete a payment
     */
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public String generatePayKey(String email, double hourlyRate, double elapsedTimeOfSession) {
        PayRequest payRequest = new PayRequest();
        try {
            RequestEnvelope env = new RequestEnvelope();
            env.setErrorLanguage("en_US");
            List<Receiver> receiver = new ArrayList<>();
            Receiver rec = new Receiver();
            /**
             * FIXME: This needs to take in hourly rate * elapsed time
             */
            double amount = Math.round(hourlyRate * elapsedTimeOfSession * 100.0) / 100.0;
            rec.setAmount(amount);
            rec.setEmail(email);
            receiver.add(rec);
            String returnUrl = "http://localhost:8080/Tut4YouWebApp/accounts/myPayments.xhtml";
            String cancelUrl = "http://localhost:8080/Tut4YouWebApp/accounts/index.xhtml";
            String currencyCode = "USD";
            ReceiverList receiverlst = new ReceiverList(receiver);
            payRequest.setReceiverList(receiverlst);
            payRequest.setRequestEnvelope(env);
            payRequest.setActionType("PAY");
            payRequest.setCancelUrl(cancelUrl);
            payRequest.setReturnUrl(returnUrl);
            payRequest.setCurrencyCode(currencyCode);
            //Creating the configuration map
            Properties prop = new Properties();
            InputStream propstream = new FileInputStream(getServletContext().getRealPath("WEB-INF/sdk_config.properties"));
            prop.load(propstream);
            Map<String, String> customConfigurationMap = new HashMap<>();
            customConfigurationMap.put("mode", "sandbox"); // Load the map with all mandatory parameters
            customConfigurationMap.put("acct1.UserName", prop.getProperty("acct1.UserName"));
            customConfigurationMap.put("acct1.Password", prop.getProperty("acct1.Password"));
            customConfigurationMap.put("acct1.Signature", prop.getProperty("acct1.Signature"));
            customConfigurationMap.put("acct1.AppId", prop.getProperty("acct1.AppId"));
            //Creating service wrapper object
            AdaptivePaymentsService adaptivePaymentsService = new AdaptivePaymentsService(customConfigurationMap);
            PayResponse payResponse = adaptivePaymentsService.pay(payRequest, prop.getProperty("acct1.Username"));
            String payKey = payResponse.getPayKey();

            return payKey;

        } catch (IOException ex) {
            Logger.getLogger(Tut4YouApp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidCredentialException ex) {
            Logger.getLogger(PaymentBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (HttpErrorException ex) {
            Logger.getLogger(PaymentBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidResponseDataException ex) {
            Logger.getLogger(PaymentBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClientActionRequiredException ex) {
            Logger.getLogger(PaymentBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MissingCredentialException ex) {
            Logger.getLogger(PaymentBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(PaymentBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OAuthException ex) {
            Logger.getLogger(PaymentBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SSLConfigurationException ex) {
            Logger.getLogger(PaymentBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    /**
     * It initially creates a payment with just the paykey, tutor, and session.
     * Since the payment details are not processed until after navigating to the
     * "My Payments" page (a completed payment will redirec the user to the
     * page), the payment is only partially created in the database.
     *
     * @param payKey payKey that is generated from PayResponse
     * @param session the session that tutor is being paid for
     * @param request
     * @param tutor the tutor being paid
     * @return payment - a payment that has a paykey, session id, and tutor.
     */
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Payment createPayment(String payKey, Session session, Request request) {
        Session sessionTimer = em.find(Session.class, session.getId());
        Request req = em.find(Request.class, request.getId());
        Tutor tutor = req.getTutor();
        User student = req.getStudent();
        Payment payment = new Payment();
        payment.setPayKey(payKey);

        sessionTimer.setPayment(payment);
        payment.setSession(sessionTimer);
        tutor.addPayment(payment);
        payment.setTutor(tutor);

        tutor.addPayment(payment);
        payment.setTutor(tutor);

        student.addPayment(payment);
        payment.setStudent(student);

        em.persist(payment);
        em.flush();
        return payment;
    }

    /**
     * Gets a list of payments based on a user's email
     *
     * @return paymentList - a list of payments
     */
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<Payment> getPaymentList() {
        //This will get a list of payments based off the current user's email
        List<Payment> payKeyList = createPaymentList();
        Map<String, String> map = new HashMap<>();
        for (int x = 0; x < payKeyList.size(); x++) {
            //This will use the paykey to get details of the payment
            //and store it into a map
            Payment payment = em.find(Payment.class, payKeyList.get(x).getPayKey());
            if (payment.getPaymentStatus() != null) {
                if (payment.getPaymentStatus().equals("COMPLETED")) {
                    break;
                }
            } else {
                map = getPayments(payKeyList.get(x).getPayKey());
                payment = em.find(Payment.class, payKeyList.get(x).getPayKey());
                payment.setPaymentAmount(Double.parseDouble(map.get("paymentInfoList.paymentInfo(0).receiver.amount")));
                payment.setPaymentId(map.get("paymentInfoList.paymentInfo(0).transactionId"));
                payment.setPaymentStatus(map.get("status"));
                payment.setTimeOfPayment(map.get("responseEnvelope.timestamp"));
                em.merge(payment);
            }
        }
        //This is called again because more payment attributes
        //were merged
        List<Payment> paymentList = createPaymentList();
        return paymentList;
    }

    /**
     * This creates a payment list based off the user's email
     *
     * @return paymentList - a list of payments
     */
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Payment> createPaymentList() {
        UserBean userBean = new UserBean();
        String currentUserEmail = userBean.getEmailFromSession();
        String email;
        if (currentUserEmail == null) {
            return null;
        } else {
            User user = findUser(currentUserEmail);
            email = user.getEmail();
            TypedQuery<Payment> paymentQuery = em.createNamedQuery(Payment.FIND_PAYMENTS_BY_EMAIL, Payment.class);
            paymentQuery.setParameter("email", email);
            return paymentQuery.getResultList();
        }
    }

    /**
     * This will get the details of the payment using the paykey
     *
     * @param payKey payKey used to get details of the payment
     *
     * @return map - mpa that contains name-value pairs of the payment details
     */
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Map<String, String> getPayments(String payKey) {
        //Code generated from Postman
        OkHttpClient client = new OkHttpClient();
        Map<String, String> map = new HashMap<>();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        //Parameters in the POST request to get payment details
        String json = "payKey=" + payKey + "&requestEnvelope.errorLanguage=en_US";
        RequestBody body = RequestBody.create(mediaType, json);
        Properties prop = new Properties();
        try {
            InputStream propstream = new FileInputStream(getServletContext().getRealPath("WEB-INF/sdk_config.properties"));
            prop.load(propstream);
        } catch (IOException ex) {
            Logger.getLogger(Tut4YouApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Creating the request
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("https://svcs.sandbox.paypal.com/AdaptivePayments/PaymentDetails")
                .post(body)
                .addHeader("X-PAYPAL-SECURITY-USERID", prop.getProperty("acct1.UserName"))
                .addHeader("X-PAYPAL-SECURITY-PASSWORD", prop.getProperty("acct1.Password"))
                .addHeader("X-PAYPAL-SECURITY-SIGNATURE", prop.getProperty("acct1.Signature"))
                .addHeader("X-PAYPAL-REQUEST-DATA-FORMAT", "NV")
                .addHeader("X-PAYPAL-RESPONSE-DATA-FORMAT", "NV")
                .addHeader("X-PAYPAL-APPLICATION-ID", prop.getProperty("acct1.AppId"))
                .build();
        try {
            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            String[] nameValue = responseBody.split("&|=");

            for (int i = 0; i < nameValue.length - 1;) {
                map.put(nameValue[i++], nameValue[i++]);
            }

            //This formats the timestamp to not include any special characters
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey().equals("responseEnvelope.timestamp")) {
                    String result = entry.getValue();
                    result = result.substring(0, 10);
                    map.put(entry.getKey(), result);
                }
            }
            return map;

        } catch (IOException ex) {
            Logger.getLogger(PaymentBean.class.getName()).log(Level.SEVERE, null, ex);

        }
        return map;

    }

    /**
     * If the current user logged in is a tutor and is viewing a previous
     * session, the "Pay Now" button should not appear.
     *
     * @param tutor
     * @return true if the tutor email and email of the tutor in a session are
     * the same
     */
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public boolean checkRequestTutorEmail(Tutor tutor) {
        UserBean userBean = new UserBean();
        String currentUserEmail = userBean.getEmailFromSession();
        if (!tutor.getEmail().isEmpty() || tutor.getEmail() != null) {
            String tutorEmail = tutor.getEmail();
            return currentUserEmail.equals(tutorEmail);
        } else {
            return false;
        }
    }

    /**
     * This checks to see if the payment is completed.
     *
     * @param payKey finds payments based off paykey
     * @return true if payment is completed
     */
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public boolean checkCompletedStatus(String payKey) {
        TypedQuery<Payment> paymentQuery = em.createNamedQuery(Payment.FIND_PAYMENTS_BY_PAYKEY, Payment.class);
        paymentQuery.setParameter("payKey", payKey);
        Payment payment = paymentQuery.getSingleResult();
        String val = payment.getPaymentStatus();
        return val.equals("COMPLETED");
    }

    /**
     * boolean to check if complaint was submitted
     *
     * @param complaints
     * @return
     * @author Keith Tran <keithtran25@gmail.com>
     */
    @RolesAllowed("tut4youapp.student")
    public boolean isComplaintSubmitted(Collection<Complaint> complaints) {
        boolean isComplaintSubmitted;
        UserBean userBean = new UserBean();
        String currentUserEmail = userBean.getEmailFromSession();
        Tutor tutor = findTutor(currentUserEmail);
        Complaint newComplaint = new Complaint();
        newComplaint.setUser(tutor);

        if (complaints.contains(newComplaint)) {
            isComplaintSubmitted = true;
        } else {
            isComplaintSubmitted = false;
        }
        return isComplaintSubmitted;
    }

    @PermitAll
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public boolean hasSubmittedTranscript() {
        UserBean userBean = new UserBean();
        String currentUserEmail = userBean.getEmailFromSession();
        TypedQuery<String> transcriptPathQuery = em.createNamedQuery(Tutor.FIND_TRANSCRIPT_PATH_BY_EMAIL, String.class);
        transcriptPathQuery.setParameter("email", currentUserEmail);
        return transcriptPathQuery.getSingleResult() == null;
    }
    /**
     * find request entity by ID
     * @param id
     * @return request
     */
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Request findRequest(Long id) {
        return em.find(Request.class, id);
    }
}