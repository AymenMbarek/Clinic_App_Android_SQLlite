package pt.ubi.di.pmd.a43855_t5;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.List;


public class MainActivity extends Activity {

    EditText identification;
    EditText password;
    Button confirmation;
    Button registration;
    RelativeLayout cover;
    TextView title;

    DataBase db;
    List<Client> list;

    Boolean userIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Irá inicializar a view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = DataBase.getInstance(getApplicationContext());
        list = db.myDao().getClients();

        identification = findViewById(R.id.identificationBox);
        password = findViewById(R.id.passwordBox);
        confirmation = findViewById(R.id.confirm);
        cover = findViewById(R.id.cover);
        title = findViewById(R.id.welcomeText);
        registration = findViewById(R.id.register);


        //App cover animation using the class Animation
        Handler h =new Handler() ;
        h.postDelayed(() -> {
            Animation animation = new TranslateAnimation(0, 0,0, -10000);
            animation.setDuration(800);
            animation.setFillAfter(true);
            cover.startAnimation(animation);
            cover.setVisibility(View.GONE);


            identification.setVisibility(View.VISIBLE);
            title.setVisibility(View.VISIBLE);
            password.setVisibility(View.VISIBLE);
            confirmation.setVisibility(View.VISIBLE);
            registration.setVisibility(View.VISIBLE);
        }, 2500);

        //Checks if the credentials are ok and if they are sends the user to the home page
        confirmation.setOnClickListener(
                oView -> {
                    userIn = false;
                    String id = identification.getText().toString();
                    String pass = password.getText().toString();

                    for (Client cl : list) {
                        if ((id.equals(String.valueOf(cl.getSNS())) || id.equals(cl.getEmail())) && pass.equals(cl.getPassword())) {
                            Intent iInitialPage = new Intent(this, InitialPage.class);
                            iInitialPage.putExtra("SNS", String.valueOf(cl.getSNS()));
                            userIn = true;
                            startActivity(iInitialPage);
                        }
                    }
                    if (!userIn) {
                        Toast.makeText(MainActivity.this,
                                "Wrong credentials", Toast.LENGTH_LONG).show();
                    }
                }
        );

        registration.setOnClickListener(
                oView -> {
                    Intent iInitialPage = new Intent(this, RegisterUser.class);
                    userIn = true;
                    startActivity(iInitialPage);
                }
        );
    }

    //Leaves the app
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Exit");
        builder.setMessage("Are you sure?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
                System.exit(0);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}