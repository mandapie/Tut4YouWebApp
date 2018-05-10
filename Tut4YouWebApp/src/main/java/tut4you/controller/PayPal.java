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
            //rec.setAmount(hourlyRate * elapsedTimeOfSession);
            rec.setAmount(2.0);
            /**
             * FIXME: This needs to take in the tutor's email
             */
            //rec.setEmail(email);
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

    public void getPayments() {
        OkHttpClient client = new OkHttpClient();
//"payKey=AP-39X27479A5096814N&requestEnvelope.errorLanguage=en_US"
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        String payKey = "AP-85190631AT676160W";
        String json = "payKey=" + payKey + "&requestEnvelope.errorLanguage=en_US";
        RequestBody body = RequestBody.create(mediaType, json);
        Request request = new Request.Builder()
                .url("https://svcs.sandbox.paypal.com/AdaptivePayments/PaymentDetails")
                .post(body)
                .addHeader("X-PAYPAL-SECURITY-USERID", "shayder426-facilitator_api1.gmail.com")
                .addHeader("X-PAYPAL-SECURITY-PASSWORD", "KY5V6AAWCEFSKE5R")
                .addHeader("X-PAYPAL-SECURITY-SIGNATURE", "Aea3S-zQp8Wqw4vgMOI6c015u53PAox42t7UAN9wZg.1Y7bs3AEWi7rK")
                .addHeader("X-PAYPAL-REQUEST-DATA-FORMAT", "NV")
                .addHeader("X-PAYPAL-RESPONSE-DATA-FORMAT", "NV")
                .addHeader("X-PAYPAL-APPLICATION-ID", "APP-80W284485P519543T")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Postman-Token", "d8ea77a8-d3cd-486b-ba13-093a7f30eeb6")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        try {
            Response response = client.newCall(request).execute();
            System.out.println(response.body().string());
            Map<String, String> mapString = convert(response.body().string());
            for (Map.Entry<String, String> entry : mapString.entrySet()) {
                System.out.print("Where are my slashesl");
                System.out.println(entry.getKey() + " / " + entry.getValue());
            }
        } catch (IOException ex) {
            Logger.getLogger(PayPal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Map<String, String> convert(String str) {
        String[] tokens = str.split(" |=");
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < tokens.length - 1;) {
            map.put(tokens[i++], tokens[i++]);
        }
        return map;
    }

}
