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

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.io.FileUtils;
import static org.omnifaces.util.Faces.getServletContext;
import tut4you.model.Complaint;
import tut4you.model.Tut4YouApp;
import tut4you.model.Tutor;
import tut4you.model.User;


/**
 * bean that handles moderators reviewing complaints and creating complaints
 * @author Keith Tran <keithtran25@gmail.com>
 */
@Named
@ViewScoped
public class ComplaintBean implements Serializable {
    //inject registrationBean
    @Inject
    private RegistrationBean registrationBean;
    //EJB
    @EJB
    private Tut4YouApp tut4youApp;
    //User
    private User user;
    //tutor
    private Tutor tutor;
    //complaint
    private Complaint complaint;
    //list
    private List<Complaint> complaintList = new ArrayList();
    //boolean isTutor
    private boolean isTutor;
    /**
     * boolean to check if user was a tutor in the complaint
     * @return isTutor
     */
    public boolean isIsTutor() {
        return isTutor;
    }
    /**
     * set isTutor
     * @param isTutor 
     */
    public void setIsTutor(boolean isTutor) {
        this.isTutor = isTutor;
    }
    /**
     * get tutor
     * @return tutor
     */
    public Tutor getTutor() {
        return tutor;
    }
    /**
     * set tutor
     * @param tutor 
     */
    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }
    /**
     * get list of complaints
     * @return complaintLIst
     */
    public List<Complaint> getComplaintList() {
        if (complaintList.isEmpty()) {
            complaintList = tut4youApp.getComplaints();
        }
        return complaintList;
    }
    /**
     * set list of complaints
     * @param complaintList 
     */
    public void setComplaintList(List<Complaint> complaintList) {
        this.complaintList = complaintList;
    }
    /**
     * get User
     * @return user
     */
    public User getUser() {
        return user;
    }
    /**
     * set user
     * @param user 
     */
    public void setUser(User user) {
        this.user = user;
    }
    /**
     * get complaint
     * @return complaint
     */
    public Complaint getComplaint() {
        return complaint;
    }
    /**
     * set complaint
     * @param complaint 
     */
    public void setComplaint(Complaint complaint) {
        this.complaint = complaint;
    }
    /**
     * Creates an instance of the courseBean
     */
    @PostConstruct
    public void createComplaintBean() {
        complaint = new Complaint();
        complaint.setIsReviewed(false);
        
    }
    /**
     * Destroys an instance of the courseBean
     */
    @PreDestroy
    public void destroyComplaintBean() {
    }
    
    
    /**
     * create a new complaint
     * @param user
     * @param isTutor
     */
    public void createNewComplaint(User user, boolean isTutor) {
      
        this.isTutor = isTutor;
        complaint.setIsTutor(isTutor);
        tut4youApp.createNewComplaint(user, complaint);
    }
    /**
     * close the complaint
     */
    public void closeComplaint() {
        tut4youApp.closeComplaint(complaint);
    }
    /**
     * flag a reported user
     * @param email
     * @param type
     * @throws ParseException 
     */
    public void flagUser(String email, String type) throws ParseException {
        Date currentDateTime = registrationBean.getCurrentDate();
        tut4youApp.closeComplaint(complaint);
        User user = tut4youApp.findUser(email);
        tut4youApp.flagUser(user, currentDateTime, type);
    }
    /**
     * boolean checks to see if complaint has been submitted
     * @param complaints
     * @return complaints
     */
    public boolean isComplaintSubmitted(Collection<Complaint> complaints) {
        return tut4youApp.isComplaintSubmitted(complaints);
        
    }
    /**
     * download transcript when moderators review a complaint made by a tutor
     * @param username
     * @throws IOException 
     */
    public void downloadTranscript(String username) throws IOException {
        Properties prop = new Properties();
        InputStream propstream = new FileInputStream(getServletContext().getRealPath("WEB-INF/s3.properties"));
        prop.load(propstream);
        AWSCredentials credentials = new BasicAWSCredentials(
                    prop.getProperty("AWSAccessKeyId"),
                    prop.getProperty("AWSSecretKey"));
        String bucketName = prop.getProperty("bucketName");

        //Taken from: https://stackoverflow.com/questions/4bucketName1951978/amazons3clientcredentials-is-deprecated
        AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_WEST_1).withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
        AccessControlList acl = new AccessControlList();
        acl.grantPermission(GroupGrantee.AllUsers, Permission.Write);
        try {
            tutor = tut4youApp.findTutorByUsername(username);
       
                 String keyName = tutor.getTranscriptFilePath();
            if (keyName == null){
                FacesMessage message = new FacesMessage("No transcript uploaded yet! Please upload a transcript.");
                FacesContext.getCurrentInstance().addMessage(null, message); 
            }
            else {
                String transcriptName = tutor.getUsername();

                S3Object s3Object = s3.getObject(new GetObjectRequest(bucketName, keyName));
                S3ObjectInputStream stream = s3Object.getObjectContent();

                String home = System.getProperty("user.home");
                Path path = Paths.get(home);
                File file = new File(home, transcriptName.concat(".pdf"));
                int i = 1;
                while (file.exists() == true) { //if file exists at that location increment the file name by 1 
                    file = new File(home, transcriptName.concat("(" + i + ").pdf"));
                    i++;
                }
                FileUtils.copyInputStreamToFile(stream, file);
                FacesMessage message = new FacesMessage("Succesfully downloaded file to: " + path + "/" + file);
                FacesContext.getCurrentInstance().addMessage(null, message);
                stream.close();
            }     
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it to Amazon S3, but was "
                    + "rejected with an error response for some reason.");
            System.out.println("Error Message:  " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:  " + ase.getErrorCode());
            System.out.println("Error Type:    " + ase.getErrorType());
            System.out.println("Request ID:    " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered an internal error while "
                    + "trying to communicate with S3, such as not being able to access the network.");
        }
    }
}