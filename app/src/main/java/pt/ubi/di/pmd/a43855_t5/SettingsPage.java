package pt.ubi.di.pmd.a43855_t5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.List;

public class SettingsPage extends Activity {

    ImageButton settings_list_button;
    ImageButton settings_add_button;
    ImageButton settings_home_button;
    ImageView profilePic;
    TextView profileName;
    Button reportButton;
    Button logoutButton;

    String id;
    DataBase db;
    Client c;
    List<Appointment> apList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Irá inicializar a view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_page);
        Intent iCameToSettings = getIntent();
        id = iCameToSettings.getStringExtra("SNS");
        db = DataBase.getInstance(getApplicationContext());
        c = db.myDao().getClientBySNS(Integer.parseInt(id));
        apList = db.myDao().getAppointmentsBySNS(c.getSNS());

        //Initialize the components
        settings_list_button = (ImageButton) findViewById(R.id.settings_appointmentsButton);
        settings_home_button = (ImageButton) findViewById(R.id.settings_homeButton);
        settings_add_button = (ImageButton) findViewById(R.id.settings_addButton);
        profilePic = (ImageView) findViewById(R.id.profilePic);
        profileName = (TextView) findViewById(R.id.username);
        reportButton = (Button) findViewById(R.id.settings_reportButton);
        logoutButton = (Button) findViewById(R.id.settings_outButton);

        //sets the name of the user under the profile pic
        profileName.setText(c.getName());

        //Sets the buttons
        logoutButton.setOnClickListener(
                oView -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);

                    builder.setTitle("Log-out");
                    builder.setMessage("Are you sure?");

                    builder.setPositiveButton("YES", (dialog, which) -> {
                        // Do nothing but close the dialog
                        finish();
                        System.exit(0);
                        dialog.dismiss();
                    });
                    builder.setNegativeButton("NO", (dialog, which) -> {

                        // Do nothing
                        dialog.dismiss();
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
        );

        //Sends the appointments log to the user using an intent
        reportButton.setOnClickListener(
                oView ->{
                    String subject = "Clinic  " + c.getName() + "'s report";
                    String to = c.getEmail();

                    String message = "Name : " + c.getName() + " " + c.getSurname() + "\nAppointments log:\n";

                    message = message + "----------------------------------------------------------\n";
                    for(Appointment a : apList)
                    {
                        message = message + a.getType() + " , " + a.getMedicResponsable() + " , " + a.getDay() + "/" + a.getMonth() + "/" + a.getYear() + " , " + a.getHour() + ":00\n\n";
                    }
                    message = message + "----------------------------------------------------------\n";
                    message = message + "\nThank you for choosing us!\n";

                    System.out.println(subject);
                    System.out.println(message);

                    Toast.makeText(SettingsPage.this,
                            "Report sent!", Toast.LENGTH_SHORT).show();

                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
                    email.putExtra(Intent.EXTRA_SUBJECT, subject);
                    email.putExtra(Intent.EXTRA_TEXT, message);

                    //need this to prompts email client only
                    email.setType("message/rfc822");

                    startActivity(Intent.createChooser(email, "Choose an Email client :"));
                }
        );


        //Setup the menu buttons
        settings_add_button.setOnClickListener(
                oView -> {
                    Intent SettingsToAdd = new Intent(this, AddAppointment.class);
                    SettingsToAdd.putExtra("SNS", id);
                    finish();
                    this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right);
                    startActivity(SettingsToAdd);
                }
        );
        settings_list_button.setOnClickListener(
                oView -> {
                    Intent SettingsToList = new Intent(this, AppointmentList.class);
                    SettingsToList.putExtra("SNS", id);
                    finish();
                    this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right);
                    startActivity(SettingsToList);
                }
        );
        settings_home_button.setOnClickListener(
                oView -> {
                    Intent SettingsToHome = new Intent(this, InitialPage.class);
                    SettingsToHome.putExtra("SNS", id);
                    finish();
                    this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right);
                    startActivity(SettingsToHome);
                }
        );
    }

    @Override
    public void onBackPressed() {
        Intent SettingsToHome = new Intent(this, InitialPage.class);
        SettingsToHome.putExtra("SNS", id);
        finish();
        this.overridePendingTransition(0, android.R.anim.fade_out);
        startActivity(SettingsToHome);
    }
}
