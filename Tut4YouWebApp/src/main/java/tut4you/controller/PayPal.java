
package tut4you.controller;

import com.paypal.exception.ClientActionRequiredException;
import com.paypal.exception.HttpErrorException;
import com.paypal.exception.InvalidCredentialException;
import com.paypal.exception.InvalidResponseDataException;
import com.paypal.exception.MissingCredentialException;
import com.paypal.exception.SSLConfigurationException;
import com.paypal.sdk.exceptions.OAuthException;

import com.paypal.svcs.services.AdaptivePaymentsService;
import com.paypal.svcs.types.common.RequestEnvelope;
import com.paypal.svcs.types.ap.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import static org.omnifaces.util.Faces.getServletContext;

/**
 *
 * @author Syed haider
 */
public class PayPal {


    public String generatePayKey() {
        PayResponse payResponse;
        try {
            RequestEnvelope env = new RequestEnvelope();
            env.setErrorLanguage("en_US");
            List<Receiver> receiver = new ArrayList<>();
            Receiver rec = new Receiver();
            /**
             * FIXME: This needs to take in hourly rate * elapsed time
             */
            rec.setAmount(2.0);
            /**
             * FIXME: This needs to take in the tutor's email
             */
            rec.setEmail("shayder426test1@gmail.com");
            receiver.add(rec);
            String actionType = "Pay";
            String returnUrl = "http://localhost:8080/Tut4YouWebApp/accounts/index.xhtml";
            String cancelUrl = "http://localhost:8080/Tut4YouWebApp/accounts/index.xhtml";
            String currencyCode = "USD";
            ReceiverList receiverlst = new ReceiverList(receiver);
            PayRequest payRequest = new PayRequest();
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
            payResponse = adaptivePaymentsService.pay(payRequest, prop.getProperty("acct1.Username"));
            return payResponse.getPayKey();

            // Response.Redirect("https://www.sandbox.paypal.com/webscr?cmd=_ap-payment&paykey=" + payResponse.payKey);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PayPal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PayPal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidCredentialException ex) {
            Logger.getLogger(PayPal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (HttpErrorException ex) {
            Logger.getLogger(PayPal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidResponseDataException ex) {
            Logger.getLogger(PayPal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClientActionRequiredException ex) {
            Logger.getLogger(PayPal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MissingCredentialException ex) {
            Logger.getLogger(PayPal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(PayPal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OAuthException ex) {
            Logger.getLogger(PayPal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SSLConfigurationException ex) {
            Logger.getLogger(PayPal.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "success";
    }
}
