package com.example.biker;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.example.biker.Urls.book_service_url;
import static com.example.biker.Urls.getMobile;
import static com.example.biker.list_user_service.getServiceListMethod;
import static com.example.biker.list_user_service.setProgressBarVisibilityService;

public class MyListServiceMethods {

    private static String acceptMessage = " Service booked by you is accepted by ";
    private static String solvedMessage = " Service is solved by ";


    public void CancelServiceMethod(final Context context, MyListServiceData myListServiceData, JSONObject jsonObjectFirstMethod, String s) {
        Log.e("kk", "Cancel Service......." + jsonObjectFirstMethod);
        setProgressBarVisibilityService(View.VISIBLE);


        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("servicer", jsonObjectFirstMethod.getInt("servicer"));
            jsonBody.put("user", jsonObjectFirstMethod.getInt("user"));
            jsonBody.put("brand", jsonObjectFirstMethod.getInt("brand"));
            jsonBody.put("vehicle_fk", jsonObjectFirstMethod.getInt("vehicle_fk"));
            jsonBody.put("model_fk", jsonObjectFirstMethod.getInt("model_fk"));
            jsonBody.put("solved", jsonObjectFirstMethod.getBoolean("solved"));
            jsonBody.put("accept", jsonObjectFirstMethod.getBoolean("accept"));
            jsonBody.put("remarks", jsonObjectFirstMethod.getString("remarks"));
            jsonBody.put("review", jsonObjectFirstMethod.getString("review"));
            if (s.trim().equals("servicer")) {
                jsonBody.put("cancel_user", jsonObjectFirstMethod.getBoolean("cancel_user"));
                jsonBody.put("cancel_servicer", true);
            } else if (s.trim().equals("user")) {
                jsonBody.put("cancel_user", true);
                jsonBody.put("cancel_servicer", jsonObjectFirstMethod.getBoolean("cancel_servicer"));
            } else {
                jsonBody.put("cancel_user", jsonObjectFirstMethod.getBoolean("cancel_user"));
                jsonBody.put("cancel_servicer", jsonObjectFirstMethod.getBoolean("cancel_servicer"));
            }
            jsonBody.put("problem", jsonObjectFirstMethod.getString("problem"));
            jsonBody.put("created_at", jsonObjectFirstMethod.getString("created_at"));
        } catch (JSONException e) {
            setProgressBarVisibilityService(View.GONE);
            e.printStackTrace();
        }
        final String requestBody = jsonBody.toString();

        String service_update_url = "";
        try {
            service_update_url = book_service_url + jsonObjectFirstMethod.get("id") + "/";
        } catch (JSONException e) {
            setProgressBarVisibilityService(View.GONE);
            e.printStackTrace();
        }
        StringRequest request = new StringRequest(Request.Method.PUT, service_update_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(login.this, ""+response, Toast.LENGTH_SHORT).show();
//                progressBar.setVisibility(View.GONE);
                try {
                    setProgressBarVisibilityService(View.GONE);
                    Toast.makeText(context, "Service is Cancelled Successfully!!", Toast.LENGTH_SHORT).show();
                    ShowUpdatedServiceListMethod(context);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                progressBar.setVisibility(View.GONE);
                setProgressBarVisibilityService(View.GONE);
                ShowUpdatedServiceListMethod(context);
                if (error.networkResponse.data != null) {
                    try {
                        String errorMessage = new String(error.networkResponse.data, "UTF-8");
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, "ERROR: " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }
        ) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 60000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 5;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        requestQueue.add(request);


    }

    public void AcceptServiceMethod(final Context context, final MyListServiceData myListServiceData, final JSONObject jsonObjectFirstMethod, String s) {
        Log.e("kk", "Accept Service......." + jsonObjectFirstMethod);
        setProgressBarVisibilityService(View.VISIBLE);


        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("servicer", jsonObjectFirstMethod.getInt("servicer"));
            jsonBody.put("user", jsonObjectFirstMethod.getInt("user"));
            jsonBody.put("brand", jsonObjectFirstMethod.getInt("brand"));
            jsonBody.put("vehicle_fk", jsonObjectFirstMethod.getInt("vehicle_fk"));
            jsonBody.put("model_fk", jsonObjectFirstMethod.getInt("model_fk"));
            jsonBody.put("solved", jsonObjectFirstMethod.getBoolean("solved"));
            jsonBody.put("accept", true);
            jsonBody.put("remarks", "" + s);
            jsonBody.put("review", jsonObjectFirstMethod.getString("review"));
            jsonBody.put("cancel_user", jsonObjectFirstMethod.getBoolean("cancel_user"));
            jsonBody.put("cancel_servicer", jsonObjectFirstMethod.getBoolean("cancel_servicer"));
            jsonBody.put("problem", jsonObjectFirstMethod.getString("problem"));
            jsonBody.put("created_at", jsonObjectFirstMethod.getString("created_at"));
        } catch (Exception e) {
            setProgressBarVisibilityService(View.GONE);
            e.printStackTrace();
        }
        final String requestBody = jsonBody.toString();

        String service_update_url = "";
        try {
            service_update_url = book_service_url + jsonObjectFirstMethod.get("id") + "/";
        } catch (JSONException e) {
            setProgressBarVisibilityService(View.GONE);
            e.printStackTrace();
        }
        StringRequest request = new StringRequest(Request.Method.PUT, service_update_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(login.this, ""+response, Toast.LENGTH_SHORT).show();
//                progressBar.setVisibility(View.GONE);
                try {
                    setProgressBarVisibilityService(View.GONE);
                    Toast.makeText(context, "Service is Accepted by Servicer!!", Toast.LENGTH_SHORT).show();
                    ShowUpdatedServiceListMethod(context);
                    String finalAcceptMessageToSend = "SVC" + jsonObjectFirstMethod.getString("id") + acceptMessage + myListServiceData.getServicerName() + " Servicer" + "\nServicer Contact No.  " + getMobile(context);
                    sendWhatsappMessage(context, finalAcceptMessageToSend, "91" + myListServiceData.getMobile());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                progressBar.setVisibility(View.GONE);
                setProgressBarVisibilityService(View.GONE);
                ShowUpdatedServiceListMethod(context);
                if (error.networkResponse.data != null) {
                    try {
                        String errorMessage = new String(error.networkResponse.data, "UTF-8");
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, "ERROR: " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }
        ) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 60000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 5;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        requestQueue.add(request);


    }

    public void SolvedServiceMethod(final Context context, final MyListServiceData myListServiceData, final JSONObject jsonObjectFirstMethod) {
        Log.e("kk", "Solved Service......." + jsonObjectFirstMethod);
        setProgressBarVisibilityService(View.VISIBLE);


        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("servicer", jsonObjectFirstMethod.getInt("servicer"));
            jsonBody.put("user", jsonObjectFirstMethod.getInt("user"));
            jsonBody.put("brand", jsonObjectFirstMethod.getInt("brand"));
            jsonBody.put("vehicle_fk", jsonObjectFirstMethod.getInt("vehicle_fk"));
            jsonBody.put("model_fk", jsonObjectFirstMethod.getInt("model_fk"));
            jsonBody.put("solved", true);
            jsonBody.put("accept", jsonObjectFirstMethod.getBoolean("accept"));
            jsonBody.put("remarks", jsonObjectFirstMethod.getString("remarks"));
            jsonBody.put("review", jsonObjectFirstMethod.getString("review"));
            jsonBody.put("cancel_user", jsonObjectFirstMethod.getBoolean("cancel_user"));
            jsonBody.put("cancel_servicer", jsonObjectFirstMethod.getBoolean("cancel_servicer"));
            jsonBody.put("problem", jsonObjectFirstMethod.getString("problem"));
            jsonBody.put("created_at", jsonObjectFirstMethod.getString("created_at"));
        } catch (Exception e) {
            setProgressBarVisibilityService(View.GONE);
            e.printStackTrace();
        }
        final String requestBody = jsonBody.toString();

        String service_update_url = "";
        try {
            service_update_url = book_service_url + jsonObjectFirstMethod.get("id") + "/";
        } catch (JSONException e) {
            setProgressBarVisibilityService(View.GONE);
            e.printStackTrace();
        }
        StringRequest request = new StringRequest(Request.Method.PUT, service_update_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(login.this, ""+response, Toast.LENGTH_SHORT).show();
//                progressBar.setVisibility(View.GONE);
                try {
                    setProgressBarVisibilityService(View.GONE);
                    Toast.makeText(context, "Service is Updated!!", Toast.LENGTH_SHORT).show();
                    ShowUpdatedServiceListMethod(context);
                    String finalSolvedMessageToSend = "SVC" + jsonObjectFirstMethod.getString("id") + solvedMessage + myListServiceData.getServicerName() + " Servicer.\nPlease give Review, ignore this message if given.";
                    sendWhatsappMessage(context, finalSolvedMessageToSend, "91" + myListServiceData.getMobile());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                progressBar.setVisibility(View.GONE);
                setProgressBarVisibilityService(View.GONE);
                ShowUpdatedServiceListMethod(context);
                if (error.networkResponse.data != null) {
                    try {
                        String errorMessage = new String(error.networkResponse.data, "UTF-8");
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, "ERROR: " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }
        ) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 60000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 5;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        requestQueue.add(request);


    }

    public void RemarkServiceMethod(final Context context, MyListServiceData myListServiceData, JSONObject jsonObjectFirstMethod, String s) {
        Log.e("kk", "Remark Service......." + jsonObjectFirstMethod);
        setProgressBarVisibilityService(View.VISIBLE);


        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("servicer", jsonObjectFirstMethod.getInt("servicer"));
            jsonBody.put("user", jsonObjectFirstMethod.getInt("user"));
            jsonBody.put("brand", jsonObjectFirstMethod.getInt("brand"));
            jsonBody.put("vehicle_fk", jsonObjectFirstMethod.getInt("vehicle_fk"));
            jsonBody.put("model_fk", jsonObjectFirstMethod.getInt("model_fk"));
            jsonBody.put("solved", jsonObjectFirstMethod.getBoolean("solved"));
            jsonBody.put("accept", jsonObjectFirstMethod.getBoolean("accept"));
            jsonBody.put("remarks", "" + s);
            jsonBody.put("review", jsonObjectFirstMethod.getString("review"));
            jsonBody.put("cancel_user", jsonObjectFirstMethod.getBoolean("cancel_user"));
            jsonBody.put("cancel_servicer", jsonObjectFirstMethod.getBoolean("cancel_servicer"));
            jsonBody.put("problem", jsonObjectFirstMethod.getString("problem"));
            jsonBody.put("created_at", jsonObjectFirstMethod.getString("created_at"));
        } catch (Exception e) {
            setProgressBarVisibilityService(View.GONE);
            e.printStackTrace();
        }
        final String requestBody = jsonBody.toString();

        String service_update_url = "";
        try {
            service_update_url = book_service_url + jsonObjectFirstMethod.get("id") + "/";
        } catch (JSONException e) {
            setProgressBarVisibilityService(View.GONE);
            e.printStackTrace();
        }
        StringRequest request = new StringRequest(Request.Method.PUT, service_update_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(login.this, ""+response, Toast.LENGTH_SHORT).show();
//                progressBar.setVisibility(View.GONE);
                try {
                    setProgressBarVisibilityService(View.GONE);
                    Toast.makeText(context, "Remark is Updated Successfully!!", Toast.LENGTH_SHORT).show();
                    ShowUpdatedServiceListMethod(context);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                progressBar.setVisibility(View.GONE);
                setProgressBarVisibilityService(View.GONE);
                ShowUpdatedServiceListMethod(context);
                if (error.networkResponse.data != null) {
                    try {
                        String errorMessage = new String(error.networkResponse.data, "UTF-8");
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, "ERROR: " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }
        ) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 60000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 5;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        requestQueue.add(request);


    }

    public void ReviewServiceMethod(final Context context, MyListServiceData myListServiceData, JSONObject jsonObjectFirstMethod, String s) {
        Log.e("kk", "Review Service......." + jsonObjectFirstMethod);
        setProgressBarVisibilityService(View.VISIBLE);


        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("servicer", jsonObjectFirstMethod.getInt("servicer"));
            jsonBody.put("user", jsonObjectFirstMethod.getInt("user"));
            jsonBody.put("brand", jsonObjectFirstMethod.getInt("brand"));
            jsonBody.put("vehicle_fk", jsonObjectFirstMethod.getInt("vehicle_fk"));
            jsonBody.put("model_fk", jsonObjectFirstMethod.getInt("model_fk"));
            jsonBody.put("solved", jsonObjectFirstMethod.getBoolean("solved"));
            jsonBody.put("accept", jsonObjectFirstMethod.getBoolean("accept"));
            jsonBody.put("remarks", jsonObjectFirstMethod.getString("remarks"));
            jsonBody.put("review", "" + s);
            jsonBody.put("cancel_user", jsonObjectFirstMethod.getBoolean("cancel_user"));
            jsonBody.put("cancel_servicer", jsonObjectFirstMethod.getBoolean("cancel_servicer"));
            jsonBody.put("problem", jsonObjectFirstMethod.getString("problem"));
            jsonBody.put("created_at", jsonObjectFirstMethod.getString("created_at"));
        } catch (Exception e) {
            setProgressBarVisibilityService(View.GONE);
            e.printStackTrace();
        }
        final String requestBody = jsonBody.toString();

        String service_update_url = "";
        try {
            service_update_url = book_service_url + jsonObjectFirstMethod.get("id") + "/";
        } catch (JSONException e) {
            setProgressBarVisibilityService(View.GONE);
            e.printStackTrace();
        }
        StringRequest request = new StringRequest(Request.Method.PUT, service_update_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(login.this, ""+response, Toast.LENGTH_SHORT).show();
//                progressBar.setVisibility(View.GONE);
                try {
                    setProgressBarVisibilityService(View.GONE);
                    Toast.makeText(context, "Review is Submitted Successfully!!", Toast.LENGTH_SHORT).show();
                    ShowUpdatedServiceListMethod(context);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                progressBar.setVisibility(View.GONE);
                setProgressBarVisibilityService(View.GONE);
                ShowUpdatedServiceListMethod(context);
                if (error.networkResponse.data != null) {
                    try {
                        String errorMessage = new String(error.networkResponse.data, "UTF-8");
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, "ERROR: " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }
        ) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 60000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 5;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        requestQueue.add(request);


    }


    public void ShowUpdatedServiceListMethod(final Context context) {
/*
        new Thread() {
            @Override
            public void run() {
                getServiceListMethod(context);
            }
        }.start();
*/
        getServiceListMethod(context);
    }


    private void sendWhatsappMessage(Context context, String finalMessageToSend, String toNumber) {
        try {

            /*

            // Creating new intent
            Intent intent = new Intent(Intent.ACTION_SEND);

            intent.setType("text/plain");
            intent.setPackage("com.whatsapp");

            // Give your message here
            intent.putExtra(Intent.EXTRA_TEXT, finalMessageToSend);

            // Checking whether Whatsapp
            // is installed or not
            if (intent.resolveActivity(context.getPackageManager()) == null) {
                Toast.makeText(context, "Please install WhatsApp first.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Starting Whatsapp
            context.startActivity(intent);
        */

/*
            Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + "" + toNumber + "?body=" + finalMessageToSend));
            sendIntent.setPackage("com.whatsapp");
            context.startActivity(sendIntent);
*/
            Intent intent = new Intent(Intent.ACTION_VIEW);
//            Intent intent = new Intent(Intent.ACTION_SEND);  OR (Intent.ACTION_SENDTO)  -> BOTH NOT WORKING FOR BELOW URL or URI
            intent.setPackage("com.whatsapp");
//            Log.e("kkk","Uri.... "+ Uri.parse("http://api.whatsapp.com/send?phone=" + toNumber + "&text=" + finalMessageToSend));
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + toNumber + "&text=" + finalMessageToSend));
            context.startActivity(intent);

        }
        catch (Exception e){
            e.printStackTrace();
            Log.e("kkk", "Error: "+e.toString());
            Toast.makeText(context,"Please install WhatsApp first.",Toast.LENGTH_SHORT).show();

        }
    }

}
