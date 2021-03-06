package com.example.pairingclient;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

public class CustomAdapter extends BaseAdapter {

    Context context;
    List<ListItem> listItems;
    private Boolean editable, favorites, PickUpMode;
    ListView listView;
    ViewHolder viewHolder;
    String ListType = "";

    CustomAdapter(Context context, List<ListItem> listItems, ListView listView) {
        this.context = context;
        this.listItems = listItems;
        this.listView = listView;

    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int i) {
        return listItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return listItems.indexOf(getItem(i));
    }

    private class ViewHolder {
        public TextView textViewCourseInfo;
        public TextView textViewTaskInfo;
        public TextView textViewPairInfo;
        public TextView textViewAgreementStatus;
        public Button buttonViewAgree;
        public Button buttonViewDontAgree;
        public Button buttonViewFullInfo;
        public Button buttonViewOkay;
    }

    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    public void deleteEvent(int ID) {
        for (int i = 0; i < listItems.size(); i++) {
            if (listItems.get(i).getId() == ID) {
                listItems.remove(i);
                //notifyDataSetChanged();
                return;
            }
        }
    }



    @Override
    public View getView(int i, View v, ViewGroup viewGroup) {
        //final ViewHolder holder =null;
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (v == null) {
            v = mInflater.inflate(R.layout.list_item, null);
            final ViewHolder holder = new ViewHolder();
            viewHolder = holder;
            holder.textViewCourseInfo = (TextView) v.findViewById(R.id.TVCourseInfo);
            holder.textViewTaskInfo = (TextView) v.findViewById(R.id.TVTaskInfo);
            holder.textViewPairInfo = (TextView) v.findViewById(R.id.TVPairInfo);
            holder.textViewAgreementStatus = (TextView) v.findViewById(R.id.TVAgreementStatus);
            holder.buttonViewAgree = (Button) v.findViewById(R.id.btnAgreePair);
            holder.buttonViewDontAgree = (Button) v.findViewById(R.id.btnDontAgreePair);
            holder.buttonViewFullInfo = (Button) v.findViewById(R.id.btnPairFullInfo);
            holder.buttonViewOkay = (Button) v.findViewById(R.id.btnOkay);
            //final Typeface tvFont = Typeface.createFromAsset(context.getAssets(), "fonts/newfont.otf");
            final Typeface tvFont = ResourcesCompat.getFont(context, R.font.newfont);
            holder.textViewCourseInfo.setTypeface(tvFont);
            holder.textViewTaskInfo.setTypeface(tvFont);
            holder.textViewPairInfo.setTypeface(tvFont);
            holder.textViewAgreementStatus.setTypeface(tvFont);
            holder.buttonViewAgree.setTypeface(tvFont);
            holder.buttonViewDontAgree.setTypeface(tvFont);
            holder.buttonViewFullInfo.setTypeface(tvFont);
            holder.buttonViewOkay.setTypeface(tvFont);


            holder.buttonViewFullInfo.setVisibility(View.INVISIBLE);
            holder.buttonViewOkay.setVisibility(View.INVISIBLE);
            final ListItem listItem = listItems.get(i);
            holder.buttonViewOkay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {

                                    //Intent intent = new Intent();
                                    //getActivity().startActivity(intent);
                                    //Intent intent = new Intent(AuthenticateUser.this, RegisterEventActivity.class);
                                    //AuthenticateUser.this.startActivity(intent);

                                } else {
                                    Toast.makeText(context, "מחיקה נכשלה", Toast.LENGTH_LONG).show();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setMessage("Deletion Failed")
                                            .setNegativeButton("Retry", null)
                                            .create()
                                            .show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    RemovePairReq removePairReq = new RemovePairReq(listItem.getId(), listItem.getFaculty(), listItem.getCourse(), listItem.getWorktype(), "u747931869_FindPair", "u747931869_yuosifhanna", "V!5:Eg0H~", responseListener);
                    RequestQueue queue = Volley.newRequestQueue(context);
                    queue.add(removePairReq);
                    holder.buttonViewOkay.setVisibility(View.INVISIBLE);
                    holder.textViewAgreementStatus.setText("נשארת לשידוך הבא");
                }
            });

            holder.buttonViewFullInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Globals.itemDetails = listItem;
                    if (Globals.global_user_name.equals(listItem.getUsername()))
                        Globals.typePair = 0;
                    else if (Globals.global_user_name.equals(listItem.getPair_UN()))
                        Globals.typePair = 1;
                    Intent intent = new Intent(context, showPartner.class);
                    context.startActivity(intent);
                }
            });

            if (Globals.global_user_name.equals(listItem.getUsername())) {

                holder.textViewCourseInfo.setText(listItem.getCourse());
                holder.textViewTaskInfo.setText(listItem.getWorktype());
                holder.textViewPairInfo.setText(listItem.getPairName());

                if (listItem.getAgreed2() == -1) {
                    holder.textViewAgreementStatus.setText("שותפ/ה שלך לא הסכים/ה");
                    holder.buttonViewDontAgree.setVisibility(View.INVISIBLE);
                    holder.buttonViewAgree.setVisibility(View.INVISIBLE);
                    holder.buttonViewOkay.setVisibility(View.VISIBLE);
                } else if (listItem.getAgreed2() == 1) {
                    holder.textViewAgreementStatus.setText("שותפ/ה שלך מסכים/ה");
                    if (listItem.getAgreed1() == 1) {
                        holder.textViewAgreementStatus.setText("אתם שותפים");
                        holder.buttonViewDontAgree.setVisibility(View.INVISIBLE);
                        holder.buttonViewAgree.setVisibility(View.INVISIBLE);
                        holder.buttonViewFullInfo.setVisibility(View.VISIBLE);
                    } else if (listItem.getAgreed1() == -1) {
                        holder.textViewAgreementStatus.setText("את/ה לא הסכמת");

                    }
                } else if (listItem.getAgreed2() == 0) {
                    holder.textViewAgreementStatus.setText("");
                    if (listItem.getAgreed1() == 1) {
                        holder.textViewAgreementStatus.setText("ממתין להסכמה");
                        holder.buttonViewDontAgree.setVisibility(View.INVISIBLE);
                        holder.buttonViewAgree.setVisibility(View.INVISIBLE);
                    } else if (listItem.getAgreed1() == -1) {
                        holder.textViewAgreementStatus.setText("את/ה לא הסכמת");

                    }
                }
                holder.buttonViewAgree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");

                                    if (success) {
                                        Toast.makeText(context, "הסכמה התבצעה", Toast.LENGTH_LONG).show();
                                        holder.buttonViewAgree.setVisibility(View.INVISIBLE);
                                        holder.buttonViewDontAgree.setVisibility(View.INVISIBLE);
                                        listItem.setAgreed1(1);
                                        if (listItem.getAgreed2() == -1) {
                                            holder.textViewAgreementStatus.setText("שותפ/ה שלך לא הסכים/ה");
                                            holder.buttonViewDontAgree.setVisibility(View.INVISIBLE);
                                            holder.buttonViewAgree.setVisibility(View.INVISIBLE);
                                            holder.buttonViewOkay.setVisibility(View.VISIBLE);
                                        } else if (listItem.getAgreed2() == 1) {
                                            holder.textViewAgreementStatus.setText("שותפ/ה שלך מסכים/ה");
                                            if (listItem.getAgreed1() == 1) {
                                                holder.textViewAgreementStatus.setText("אתם שותפים");
                                                holder.buttonViewDontAgree.setVisibility(View.INVISIBLE);
                                                holder.buttonViewAgree.setVisibility(View.INVISIBLE);
                                                holder.buttonViewFullInfo.setVisibility(View.VISIBLE);
                                                UpdateUserDatabase(listItem.getUsername(),listItem.getPair_UN(),listItem.getFaculty(),listItem.getCourse(),listItem.getWorktype(), 1);
                                            } else if (listItem.getAgreed1() == -1) {
                                                holder.textViewAgreementStatus.setText("את/ה לא הסכמת");
                                                UpdateUserDatabase(listItem.getUsername(),listItem.getPair_UN(),listItem.getFaculty(),listItem.getCourse(),listItem.getWorktype(), 0);
                                            }
                                        } else if (listItem.getAgreed2() == 0) {
                                            holder.textViewAgreementStatus.setText("");
                                            if (listItem.getAgreed1() == 1) {
                                                holder.textViewAgreementStatus.setText("ממתין להסכמה");
                                                holder.buttonViewDontAgree.setVisibility(View.INVISIBLE);
                                                holder.buttonViewAgree.setVisibility(View.INVISIBLE);
                                            } else if (listItem.getAgreed1() == -1) {
                                                holder.textViewAgreementStatus.setText("את/ה לא הסכמת");
                                                UpdateUserDatabase(listItem.getUsername(),listItem.getPair_UN(),listItem.getFaculty(),listItem.getCourse(),listItem.getWorktype(), 0);
                                            }
                                        }
                                        //Intent intent = new Intent();
                                        //getActivity().startActivity(intent);
                                        //Intent intent = new Intent(AuthenticateUser.this, RegisterEventActivity.class);
                                        //AuthenticateUser.this.startActivity(intent);

                                        try {
                                            //if (AuthenticateUser.this != null)
                                            //hideSoftKeyboard(AuthenticateUser.this);
                                        } catch (Exception e) {

                                        }

                                    } else {
                                        Toast.makeText(context, "שליחה נכשלה", Toast.LENGTH_LONG).show();
                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        builder.setMessage("Register Failed")
                                                .setNegativeButton("Retry", null)
                                                .create()
                                                .show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        AgreementReq AgreementRequest = new AgreementReq(listItem.getId(), 0, 1, "u747931869_FindPair", "u747931869_yuosifhanna", "V!5:Eg0H~", responseListener);
                        RequestQueue queue = Volley.newRequestQueue(context);
                        queue.add(AgreementRequest);

                    }
                });
                holder.buttonViewDontAgree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if (success) {
                                        Toast.makeText(context, "הסכמה התבצעה", Toast.LENGTH_LONG).show();
                                        holder.buttonViewAgree.setVisibility(View.INVISIBLE);
                                        holder.buttonViewDontAgree.setVisibility(View.INVISIBLE);
                                        listItem.setAgreed1(-1);
                                        if (listItem.getAgreed2() == -1) {
                                            holder.textViewAgreementStatus.setText("שותפ/ה שלך לא הסכים/ה");
                                            holder.buttonViewDontAgree.setVisibility(View.INVISIBLE);
                                            holder.buttonViewAgree.setVisibility(View.INVISIBLE);
                                            holder.buttonViewOkay.setVisibility(View.VISIBLE);
                                        } else if (listItem.getAgreed2() == 1) {
                                            holder.textViewAgreementStatus.setText("שותפ/ה שלך מסכים/ה");
                                            if (listItem.getAgreed1() == 1) {
                                                holder.textViewAgreementStatus.setText("אתם שותפים");
                                                holder.buttonViewDontAgree.setVisibility(View.INVISIBLE);
                                                holder.buttonViewAgree.setVisibility(View.INVISIBLE);
                                                holder.buttonViewFullInfo.setVisibility(View.VISIBLE);
                                                UpdateUserDatabase(listItem.getUsername(),listItem.getPair_UN(),listItem.getFaculty(),listItem.getCourse(),listItem.getWorktype(), 1);
                                            } else if (listItem.getAgreed2() == -1) {
                                                holder.textViewAgreementStatus.setText("את/ה לא הסכמת");
                                                UpdateUserDatabase(listItem.getUsername(),listItem.getPair_UN(),listItem.getFaculty(),listItem.getCourse(),listItem.getWorktype(), 0);
                                            }
                                        } else if (listItem.getAgreed2() == 0) {
                                            holder.textViewAgreementStatus.setText("");
                                            if (listItem.getAgreed1() == 1) {
                                                holder.textViewAgreementStatus.setText("ממתין להסכמה");
                                                holder.buttonViewDontAgree.setVisibility(View.INVISIBLE);
                                                holder.buttonViewAgree.setVisibility(View.INVISIBLE);
                                            } else if (listItem.getAgreed1() == -1) {
                                                holder.textViewAgreementStatus.setText("את/ה לא הסכמת");
                                                UpdateUserDatabase(listItem.getUsername(),listItem.getPair_UN(),listItem.getFaculty(),listItem.getCourse(),listItem.getWorktype(), 0);
                                            }
                                        }
                                        //Intent intent = new Intent();
                                        //getActivity().startActivity(intent);
                                        //Intent intent = new Intent(AuthenticateUser.this, RegisterEventActivity.class);
                                        //AuthenticateUser.this.startActivity(intent);

                                        try {
                                            //if (AuthenticateUser.this != null)
                                            //hideSoftKeyboard(AuthenticateUser.this);
                                        } catch (Exception e) {

                                        }

                                    } else {
                                        Toast.makeText(context, "שליחה נכשלה", Toast.LENGTH_LONG).show();
                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        builder.setMessage("Register Failed")
                                                .setNegativeButton("Retry", null)
                                                .create()
                                                .show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        AgreementReq AgreementRequest = new AgreementReq(listItem.getId(), 0, -1, "u747931869_FindPair", "u747931869_yuosifhanna", "V!5:Eg0H~", responseListener);
                        RequestQueue queue = Volley.newRequestQueue(context);
                        queue.add(AgreementRequest);
                    }
                });
            }
            else if (Globals.global_user_name.equals(listItem.getPair_UN())) {
                holder.textViewCourseInfo.setText(listItem.getCourse());
                holder.textViewTaskInfo.setText(listItem.getWorktype());
                holder.textViewPairInfo.setText(listItem.getName());
                if (listItem.getAgreed1() == -1) {
                    holder.textViewAgreementStatus.setText("שותפ/ה שלך לא הסכים/ה");
                    holder.buttonViewDontAgree.setVisibility(View.INVISIBLE);
                    holder.buttonViewAgree.setVisibility(View.INVISIBLE);
                    holder.buttonViewOkay.setVisibility(View.VISIBLE);
                } else if (listItem.getAgreed1() == 1) {
                    holder.textViewAgreementStatus.setText("שותפ/ה שלך מסכים/ה");
                    if (listItem.getAgreed2() == 1) {
                        holder.textViewAgreementStatus.setText("אתם שותפים");
                        holder.buttonViewDontAgree.setVisibility(View.INVISIBLE);
                        holder.buttonViewAgree.setVisibility(View.INVISIBLE);
                        holder.buttonViewFullInfo.setVisibility(View.VISIBLE);
                    } else if (listItem.getAgreed2() == -1) {
                        holder.textViewAgreementStatus.setText("את/ה לא הסכמת");
                    }
                } else if (listItem.getAgreed1() == 0) {
                    holder.textViewAgreementStatus.setText("");
                    if (listItem.getAgreed2() == 1) {
                        holder.textViewAgreementStatus.setText("ממתין להסכמה");
                        holder.buttonViewDontAgree.setVisibility(View.INVISIBLE);
                        holder.buttonViewAgree.setVisibility(View.INVISIBLE);
                    } else if (listItem.getAgreed2() == -1) {
                        holder.textViewAgreementStatus.setText("את/ה לא הסכמת");
                    }
                }
                holder.buttonViewAgree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if (success) {
                                        Toast.makeText(context, "הסכמה התבצעה", Toast.LENGTH_LONG).show();
                                        holder.buttonViewAgree.setVisibility(View.INVISIBLE);
                                        holder.buttonViewDontAgree.setVisibility(View.INVISIBLE);
                                        listItem.setAgreed2(1);
                                        if (listItem.getAgreed1() == -1) {
                                            holder.textViewAgreementStatus.setText("שותפ/ה שלך לא הסכים/ה");
                                            holder.buttonViewDontAgree.setVisibility(View.INVISIBLE);
                                            holder.buttonViewAgree.setVisibility(View.INVISIBLE);
                                            holder.buttonViewOkay.setVisibility(View.VISIBLE);
                                        } else if (listItem.getAgreed1() == 1) {
                                            holder.textViewAgreementStatus.setText("שותפ/ה שלך מסכים/ה");
                                            if (listItem.getAgreed2() == 1) {
                                                holder.textViewAgreementStatus.setText("אתם שותפים");
                                                holder.buttonViewDontAgree.setVisibility(View.INVISIBLE);
                                                holder.buttonViewAgree.setVisibility(View.INVISIBLE);
                                                holder.buttonViewFullInfo.setVisibility(View.VISIBLE);
                                                UpdateUserDatabase(listItem.getUsername(),listItem.getPair_UN(),listItem.getFaculty(),listItem.getCourse(),listItem.getWorktype(), 1);
                                            } else if (listItem.getAgreed2() == -1) {
                                                holder.textViewAgreementStatus.setText("את/ה לא הסכמת");
                                                UpdateUserDatabase(listItem.getUsername(),listItem.getPair_UN(),listItem.getFaculty(),listItem.getCourse(),listItem.getWorktype(), 0);
                                            }
                                        } else if (listItem.getAgreed1() == 0) {
                                            holder.textViewAgreementStatus.setText("");
                                            if (listItem.getAgreed2() == 1) {
                                                holder.textViewAgreementStatus.setText("ממתין להסכמה");
                                                holder.buttonViewDontAgree.setVisibility(View.INVISIBLE);
                                                holder.buttonViewAgree.setVisibility(View.INVISIBLE);
                                            } else if (listItem.getAgreed2() == -1) {
                                                holder.textViewAgreementStatus.setText("את/ה לא הסכמת");
                                                UpdateUserDatabase(listItem.getUsername(),listItem.getPair_UN(),listItem.getFaculty(),listItem.getCourse(),listItem.getWorktype(), 0);
                                            }
                                        }
                                        //Intent intent = new Intent();
                                        //getActivity().startActivity(intent);
                                        //Intent intent = new Intent(AuthenticateUser.this, RegisterEventActivity.class);
                                        //AuthenticateUser.this.startActivity(intent);

                                        try {
                                            //if (AuthenticateUser.this != null)
                                            //hideSoftKeyboard(AuthenticateUser.this);
                                        } catch (Exception e) {

                                        }

                                    } else {
                                        Toast.makeText(context, "שליחה נכשלה", Toast.LENGTH_LONG).show();
                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        builder.setMessage("Register Failed")
                                                .setNegativeButton("Retry", null)
                                                .create()
                                                .show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        AgreementReq AgreementRequest = new AgreementReq(listItem.getId(), 1, 1, "u747931869_FindPair", "u747931869_yuosifhanna", "V!5:Eg0H~", responseListener);
                        RequestQueue queue = Volley.newRequestQueue(context);
                        queue.add(AgreementRequest);

                    }
                });
                holder.buttonViewDontAgree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if (success) {
                                        Toast.makeText(context, "הסכמה התבצעה", Toast.LENGTH_LONG).show();
                                        holder.buttonViewAgree.setVisibility(View.INVISIBLE);
                                        holder.buttonViewDontAgree.setVisibility(View.INVISIBLE);
                                        listItem.setAgreed2(-1);
                                        if (listItem.getAgreed1() == -1) {
                                            holder.textViewAgreementStatus.setText("שותפ/ה שלך לא הסכים/ה");
                                            holder.buttonViewDontAgree.setVisibility(View.INVISIBLE);
                                            holder.buttonViewAgree.setVisibility(View.INVISIBLE);
                                            holder.buttonViewOkay.setVisibility(View.VISIBLE);
                                        } else if (listItem.getAgreed1() == 1) {
                                            holder.textViewAgreementStatus.setText("שותפ/ה שלך מסכים/ה");
                                            if (listItem.getAgreed2() == 1) {
                                                holder.textViewAgreementStatus.setText("אתם שותפים");
                                                holder.buttonViewDontAgree.setVisibility(View.INVISIBLE);
                                                holder.buttonViewAgree.setVisibility(View.INVISIBLE);
                                                holder.buttonViewFullInfo.setVisibility(View.VISIBLE);
                                                UpdateUserDatabase(listItem.getUsername(),listItem.getPair_UN(),listItem.getFaculty(),listItem.getCourse(),listItem.getWorktype(), 1);
                                            } else if (listItem.getAgreed2() == -1) {
                                                holder.textViewAgreementStatus.setText("את/ה לא הסכמת");
                                                UpdateUserDatabase(listItem.getUsername(),listItem.getPair_UN(),listItem.getFaculty(),listItem.getCourse(),listItem.getWorktype(), 0);
                                            }
                                        } else if (listItem.getAgreed1() == 0) {
                                            holder.textViewAgreementStatus.setText("");
                                            if (listItem.getAgreed2() == 1) {
                                                holder.textViewAgreementStatus.setText("ממתין להסכמה");
                                                holder.buttonViewDontAgree.setVisibility(View.INVISIBLE);
                                                holder.buttonViewAgree.setVisibility(View.INVISIBLE);
                                            } else if (listItem.getAgreed2() == -1) {
                                                holder.textViewAgreementStatus.setText("את/ה לא הסכמת");
                                                UpdateUserDatabase(listItem.getUsername(),listItem.getPair_UN(),listItem.getFaculty(),listItem.getCourse(),listItem.getWorktype(), 0);
                                            }
                                        }
                                        //Intent intent = new Intent();
                                        //getActivity().startActivity(intent);
                                        //Intent intent = new Intent(AuthenticateUser.this, RegisterEventActivity.class);
                                        //AuthenticateUser.this.startActivity(intent);

                                        try {
                                            //if (AuthenticateUser.this != null)
                                            //hideSoftKeyboard(AuthenticateUser.this);
                                        } catch (Exception e) {

                                        }

                                    } else {
                                        Toast.makeText(context, "שליחה נכשלה", Toast.LENGTH_LONG).show();
                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        builder.setMessage("Register Failed")
                                                .setNegativeButton("Retry", null)
                                                .create()
                                                .show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        AgreementReq AgreementRequest = new AgreementReq(listItem.getId(), 1, -1, "u747931869_FindPair", "u747931869_yuosifhanna", "V!5:Eg0H~", responseListener);
                        RequestQueue queue = Volley.newRequestQueue(context);
                        queue.add(AgreementRequest);
                    }
                });
            }


        }

        return v;
    }


    void UpdateUserDatabase(String username1, String username2, String faculty, String course, String workType, int TypeU) {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {

                        //Intent intent = new Intent();
                        //getActivity().startActivity(intent);
                        //Intent intent = new Intent(AuthenticateUser.this, RegisterEventActivity.class);
                        //AuthenticateUser.this.startActivity(intent);

                        try {
                            //if (AuthenticateUser.this != null)
                            //hideSoftKeyboard(AuthenticateUser.this);
                        } catch (Exception e) {

                        }

                    } else {
                        Toast.makeText(context, "שליחה נכשלה", Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Register Failed")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        RemoveUsersReq AgreementRequest = new RemoveUsersReq(username1, username2, faculty, course, workType, TypeU, "u747931869_FindPair", "u747931869_yuosifhanna", "V!5:Eg0H~", responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(AgreementRequest);

    }
}
