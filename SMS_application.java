package com.quizapp.hp.quiz;
import android.app.Application;
import android.text.TextUtils;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
/**
 * Created by VishnuMidhun on 06-11-2015.
 */
public class SMS_application extends AnalyticsApplication {
     public static final String TAG = SMS_application.class
                .getSimpleName();
        private RequestQueue mRequestQueue;
        private static SMS_application mInstance;
        @Override
        public void onCreate() {
                super.onCreate();
                mInstance = this;
            }
        public static synchronized SMS_application getInstance() {
             return mInstance;
                }
        public RequestQueue getRequestQueue() {
                if (mRequestQueue == null) {
                        mRequestQueue = Volley.newRequestQueue(SMS_application.this);//getApplicationContext()
                    }
                return mRequestQueue;
            }
        public <T> void addToRequestQueue(Request<T> req, String tag) {
                req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
                getRequestQueue().add(req);
            }
        public <T> void addToRequestQueue(Request<T> req) {
                req.setTag(TAG);
                getRequestQueue().add(req);
            }
        public void cancelPendingRequests(Object tag) {
                if (mRequestQueue != null) {
                        mRequestQueue.cancelAll(tag);
                    }
            }
}

