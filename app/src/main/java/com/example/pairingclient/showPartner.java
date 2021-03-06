package com.example.pairingclient;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

public class showPartner extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_partner);
        FixLayoutAspects();
        Globals.course = "מבוא למדעי המחשב";
        final TextView partnersText = (TextView) findViewById(R.id.partnersText);
        String partners_string = "";
        if(Globals.typePair == 0)
        {
            partners_string += "קורס: " + Globals.itemDetails.getCourse() + "\nמטלה: " + Globals.itemDetails.getWorktype() + "\nשותף/ה: " + Globals.itemDetails.getPairName() + "\nמס טלפון: "
                    + Globals.itemDetails.getPhoneOfPair() + "\nאימייל: " + Globals.itemDetails.getEmailOfPair() + "\n------\n";
        }
        else
        {
            partners_string += "קורס: " + Globals.itemDetails.getCourse() + "\nמטלה: " + Globals.itemDetails.getWorktype() + "\nשותף/ה: " + Globals.itemDetails.getName() + "\nמס טלפון: "
                    + Globals.itemDetails.getPhone() + "\nאימייל: " + Globals.itemDetails.getEmail() + "\n------\n";
        }
        partnersText.setText(partners_string);

    }
    void FixLayoutAspects()
    {

        RelativeLayout rl = (RelativeLayout) findViewById(R.id.RLShowPartner);
        int childCount = rl.getChildCount();

        ViewGroup.LayoutParams LPR = (ViewGroup.LayoutParams) rl.getLayoutParams();


        if(LPR.width>0)
            LPR.width = (int) (LPR.width * Globals.scaleDP);
        if(LPR.height>0)
            LPR.height = (int) (LPR.height * Globals.scaleDP);
        rl.setLayoutParams(LPR);
        for (int i = 0; i < childCount; i++) {
            View view = rl.getChildAt(i);
            //RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (view.getLayoutParams().width * Globals.scaleDP),
            //        (int) (view.getLayoutParams().height * Globals.scaleDP));
            RelativeLayout.LayoutParams LP = (RelativeLayout.LayoutParams) view.getLayoutParams();
            //layoutParams.setMargins((int) (LP.leftMargin * Globals.scaleDP), (int) (LP.topMargin * Globals.scaleDP),
            //        (int) (LP.rightMargin * Globals.scaleDP), (int) (LP.bottomMargin * Globals.scaleDP));
            RelativeLayout.LayoutParams NewLP = new RelativeLayout.LayoutParams(LP);
            int[] rules = LP.getRules();
            for (int verb = 0; verb < rules.length; verb++) {
                int subject = rules[verb];
                NewLP.addRule(verb, subject);
            }
            NewLP.setMargins((int) (LP.leftMargin * Globals.scaleDP), (int) (LP.topMargin * Globals.scaleDP),
                    (int) (LP.rightMargin * Globals.scaleDP), (int) (LP.bottomMargin * Globals.scaleDP));
            if (NewLP.height > 0 )
                NewLP.height = (int) (LP.height * Globals.scaleDP);
            if(NewLP.width > 0)
                NewLP.width = (int) (LP.width * Globals.scaleDP);

            if (view instanceof Button) {
                Button button = (Button) view;
                float size = button.getTextSize();
                button.setTextSize((button.getTextSize() * Globals.scaleDP * Globals.scaleS) / Globals.DP);
            }
            else if (view instanceof TextView) {
                TextView textView = (TextView) view;
                float size = textView.getTextSize();
                textView.setTextSize((textView.getTextSize() * Globals.scaleDP * Globals.scaleS) / Globals.DP);
            }  else if (view instanceof EditText) {
                EditText editText = (EditText) view;
                float size = editText.getTextSize();
                editText.setTextSize((editText.getTextSize() * Globals.scaleDP * Globals.scaleS) / Globals.DP);
            } else if (view instanceof ImageView) {
                ImageView imageView = (ImageView) view;
                float height = imageView.getHeight();
                float width = imageView.getWidth();
                //view.setLayoutParams(NewLP);

                //imageView.setTextSize((imageView.getTextSize() * Globals.scaleDP)/ Globals.DP);
            } else if (view instanceof Spinner) {
                Spinner spinner = (Spinner) view;
                if(Globals.ActualWidth / (float)(Globals.ActualHeight) > 9.0f /16.0f)
                    NewLP.topMargin = (int)(((NewLP.topMargin / Globals.DP)-15)*Globals.DP) ;
                //view.setLayoutParams(NewLP);
            }
            else if (view instanceof CardView)
            {
                if(Globals.Ratio >17f / 9f ) {
                    NewLP.height = (int) (NewLP.height * 1.1f);
                }
            }
            view.setLayoutParams(NewLP);

            //view.setX(location[0]);
            //view.setY(location[1]);

            // Do something with v.
            // …


        }



        rl = findViewById(R.id.RLCardView);

        LPR = (ViewGroup.LayoutParams) rl.getLayoutParams();
        if (LPR.width > 0)
            LPR.width = (int) (LPR.width * Globals.scaleDP);
        if (LPR.height > 0)
            LPR.height = (int) (LPR.height * Globals.scaleDP);
        if(Globals.Ratio >17f / 9f ) {
            LPR.height = (int) (LPR.height * 1.3f);
        }
        rl.setLayoutParams(LPR);

        CardView c1 = (CardView) findViewById(R.id.CardViewShowPartner);


        childCount = c1.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View view = c1.getChildAt(i);
            //RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (view.getLayoutParams().width * Globals.scaleDP),
            //        (int) (view.getLayoutParams().height * Globals.scaleDP));
            FrameLayout.LayoutParams LP = (FrameLayout.LayoutParams) view.getLayoutParams();
            //layoutParams.setMargins((int) (LP.leftMargin * Globals.scaleDP), (int) (LP.topMargin * Globals.scaleDP),
            //        (int) (LP.rightMargin * Globals.scaleDP), (int) (LP.bottomMargin * Globals.scaleDP));
            FrameLayout.LayoutParams NewLP = new FrameLayout.LayoutParams(LP);
            NewLP.gravity=LP.gravity;
            NewLP.topMargin=LP.topMargin;
            NewLP.leftMargin=LP.leftMargin;
            NewLP.bottomMargin=LP.bottomMargin;
            NewLP.rightMargin=LP.rightMargin;



            NewLP.setMargins((int) (LP.leftMargin * Globals.scaleDP), (int) (LP.topMargin * Globals.scaleDP),
                    (int) (LP.rightMargin * Globals.scaleDP), (int) (LP.bottomMargin * Globals.scaleDP));

            if (NewLP.height > 0)
                NewLP.height = (int) (LP.height * Globals.scaleDP);
            if (NewLP.width > 0)
                NewLP.width = (int) (LP.width * Globals.scaleDP);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                float size = textView.getTextSize();
                textView.setTextSize((textView.getTextSize() * Globals.scaleDP * Globals.scaleS) / Globals.DP);
            } else if (view instanceof Button) {
                Button button = (Button) view;
                float size = button.getTextSize();
                button.setTextSize((button.getTextSize() * Globals.scaleDP * Globals.scaleS) / Globals.DP);
            } else if (view instanceof EditText) {
                EditText editText = (EditText) view;
                float size = editText.getTextSize();
                editText.setTextSize((editText.getTextSize() * Globals.scaleDP * Globals.scaleS) / Globals.DP);
            } else if (view instanceof ImageView) {
                ImageView imageView = (ImageView) view;
                float height = imageView.getHeight();
                float width = imageView.getWidth();

                //imageView.setTextSize((imageView.getTextSize() * Globals.scaleDP)/ Globals.DP);
            } else if (view instanceof Spinner) {
                Spinner spinner = (Spinner) view;

            }
            else if (view instanceof CardView)
            {
                if(Globals.Ratio >17f / 9f ) {
                    NewLP.height = (int) (NewLP.height * 1.1f);
                }
            }
            view.setLayoutParams(NewLP);


        }
    }
}