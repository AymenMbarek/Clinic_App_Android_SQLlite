package pt.ubi.di.pmd.a43855_t5;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.util.Calendar;
import java.util.List;

public class InitialPage extends Activity {

        String id;
        Client c;
        DataBase db;
        List<Appointment> list,monthList;
        Calendar todayCal;
        int hour,day,month,year, qtAp,remainingAp;

        //XML objects
        TextView welcomeMessage;
        TextView monthAppointments;
        TextView remainingAppointmens;
        ImageButton home_list_button;
        ImageButton home_add_button;
        ImageButton home_settings_button;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            //Irá inicializar a view
            super.onCreate(savedInstanceState);
            setContentView(R.layout.initial_page);
            Intent iCameFromMain = getIntent();
            id = iCameFromMain.getStringExtra("SNS");

            //Setup the database
            db = DataBase.getInstance(getApplicationContext());
            c = db.myDao().getClientBySNS(Integer.parseInt(id));
            todayCal = Calendar.getInstance();
            hour = todayCal.get(Calendar.HOUR_OF_DAY) + 1;
            day = todayCal.get(Calendar.DAY_OF_MONTH);
            month = todayCal.get(Calendar.MONTH) + 1;
            year = todayCal.get(Calendar.YEAR);

            //Appointments this month
            monthList = db.myDao().getMonthAppointments(year,month,c.getSNS());
            qtAp = countMonthAppointments();

            //All appointments
            list = db.myDao().getAppointmentsBySNS(c.getSNS());
            remainingAp = countRemainingAppointments();


            //Update the title
            welcomeMessage = (TextView) findViewById(R.id.welcomeText);
            String str = getResources().getString(R.string.home_welcome_en, c.getName());
            welcomeMessage.setText(str);

            //Update texts
            monthAppointments = (TextView) findViewById(R.id.home_monthAp);
            str = getResources().getString(R.string.home_monthAp_en, qtAp);
            monthAppointments.setText(str);

            remainingAppointmens = (TextView) findViewById(R.id.home_remainingAp);
            str = getResources().getString(R.string.home_totalAp_en, remainingAp);
            remainingAppointmens.setText(str);


            //Setup the menu buttons
            home_list_button = (ImageButton) findViewById(R.id.home_appointmentsButton);
            home_settings_button = (ImageButton) findViewById(R.id.home_settingsButton);
            home_add_button = (ImageButton) findViewById(R.id.home_addButton);

            home_add_button.setOnClickListener(
                    oView -> {
                        Intent HomeToAdd = new Intent(this, AddAppointment.class);
                        HomeToAdd.putExtra("SNS", id);
                        finish();
                        this.overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_left);
                        startActivity(HomeToAdd);
                    }
            );
            home_list_button.setOnClickListener(
                    oView -> {
                        Intent HomeToList = new Intent(this, AppointmentList.class);
                        HomeToList.putExtra("SNS", id);
                        finish();
                        this.overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_left);
                        startActivity(HomeToList);
                    }
            );
            home_settings_button.setOnClickListener(
                    oView -> {
                        Intent HomeToSettings = new Intent(this, SettingsPage.class);
                        HomeToSettings.putExtra("SNS", id);
                        finish();
                        this.overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_left);
                        startActivity(HomeToSettings);
                    }
            );
        }

    //Returns the number of appointments left
    private int countRemainingAppointments()
    {
            int count = 0;
            for(int i=0; i<list.size();i++)
            {
                if ((list.get(i).getYear() > year)
                        || (list.get(i).getYear() == year && list.get(i).getMonth() > month)
                        || (list.get(i).getYear() == year && list.get(i).getMonth() == month && list.get(i).getDay() > day)
                        || (list.get(i).getYear() == year && list.get(i).getMonth() == month && list.get(i).getDay() == day && list.get(i).getHour() > hour)) {
                    count++;
                }
            }

            return count;
    }

    //Returns how many appointments are left this month
    private int countMonthAppointments()
    {
        int count = 0;

        for(int i=0; i<monthList.size();i++)
        {
            if(monthList.get(i).getDay() > day || (monthList.get(i).getDay() == day && monthList.get(i).getHour() > hour))
            {
                count++;
            }
        }

        return count;
    }

    @Override
        public void onBackPressed() {
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
}
