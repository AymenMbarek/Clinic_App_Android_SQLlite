package pt.ubi.di.pmd.a43855_t5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AppointmentDetails extends Activity {

    String apID;
    DataBase db;
    Appointment currentAp;
    Client c;

    //Components
    TextView typeText;
    TextView doctorText;
    TextView patientText;
    TextView dateText;
    TextView hourText;
    TextView emailText;
    TextView notesText;
    Button cancelButton;
    Button reportButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Irá inicializar a view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment_details);
        Intent iCameToApdetails = getIntent();
        apID = iCameToApdetails.getStringExtra("SNS");

        db = DataBase.getInstance(getApplicationContext());
        currentAp = db.myDao().getAppointmentByID(Integer.parseInt(apID));
        c = db.myDao().getClientBySNS(currentAp.getSNS());


        //Initialize the compontents
        typeText = (TextView) findViewById(R.id.def_typeText);
        doctorText = (TextView) findViewById(R.id.def_doctorText);
        patientText = (TextView) findViewById(R.id.def_patientText);
        dateText = (TextView) findViewById(R.id.def_dateText);
        hourText = (TextView) findViewById(R.id.def_hourText);
        emailText = (TextView) findViewById(R.id.def_emailText);
        notesText = (TextView) findViewById(R.id.def_notesText);
        reportButton = (Button) findViewById(R.id.det_reportButton);
        cancelButton = (Button) findViewById(R.id.det_cancelButton);


        //Set the components text
        typeText.setText(currentAp.getType());
        doctorText.setText(currentAp.getMedicResponsable());
        patientText.setText(c.getName() + " " + c.getSurname());
        dateText.setText(currentAp.getDay() + "/" + currentAp.getMonth() + "/" + currentAp.getYear());
        hourText.setText(currentAp.getHour() + "");
        if(!currentAp.getEmail().isEmpty())
            emailText.setText(currentAp.getEmail());
        else
            emailText.setText(c.getEmail());
        notesText.setText(currentAp.getNotes());


        //Button listeners
        //Sends the report using an intent
        reportButton.setOnClickListener(
                oView ->{
                    String subject = "Clinic Ai senhor doutor! appointment no." + currentAp.getIDappointment() + " report";
                    String to;

                    if(!currentAp.getEmail().isEmpty())
                        to = currentAp.getEmail();
                    else
                        to = c.getEmail();

                    String message = "Nºutente/SNS number : " + currentAp.getSNS() + "\n"
                            +"Date (DD/MM/YYYY): " + currentAp.getDay() + "/" + currentAp.getMonth() + "/" + currentAp.getYear() + "\n"
                            +"Hour (24h format): " + currentAp.getHour() + ":00" + "\n"
                            +"Duration: 1 hour\n"
                            +"Type of appointment: " + currentAp.getType() + "\n"
                            +"Doctor responsible: " + currentAp.getMedicResponsable() + "\n"
                            +"Notes: " + currentAp.getNotes() + "\n\n"
                            +"Thank you for choosing us!";

                    System.out.println(subject);
                    System.out.println(message);

                    Toast.makeText(AppointmentDetails.this,
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


        cancelButton.setOnClickListener(
                oView ->{
                    Intent DetailstoList = new Intent(this, AppointmentList.class);
                    DetailstoList.putExtra("SNS", String.valueOf(c.getSNS()));
                    finish();
                    this.overridePendingTransition(0, 0);
                    startActivity(DetailstoList);
                }
        );
    }

    @Override
    public void onBackPressed() {
        Intent DetailstoList = new Intent(this, AppointmentList.class);
        DetailstoList.putExtra("SNS", String.valueOf(c.getSNS()));
        finish();
        this.overridePendingTransition(0, 0);
        startActivity(DetailstoList);
    }
}
