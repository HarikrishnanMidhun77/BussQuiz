package com.quizapp.hp.quiz;

/**
 * Created by VishnuMidhun on 06-11-2015.
 */
public class Config {
    // server URL configuration
    public static final String URL_REQUEST_SMS = "http://gokera.phacsin.com/include/Request_SMS.php";
        public static final String URL_VERIFY_OTP = "http://gokera.phacsin.com/include/verify_otp.php";
        // SMS provider identification
                // It should match with your SMS gateway origin
                // You can use  MSGIND, TESTER and ALERTS as sender ID
                // If you want custom sender Id, approve MSG91 to get one
                public static final String SMS_ORIGIN = "Gokera Quiz";
        // special character to prefix the otp. Make sure this character appears only once in the sms
                public static final String OTP_DELIMITER = ":";
}
