package pt.ubi.di.pmd.a43855_t5;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class RegisterUser extends Activity {

    DataBase db;
    List<Client> list;
    Client newClient;

    EditText sns;
    EditText name;
    EditText surname;
    EditText email;
    EditText password;
    EditText retypePassword;
    Button confirm;
    Button cancel;

    //Retorna true se o utilizador for válido e false se algumas informações forem inválidas ou o user já existir
    private Boolean checkUser(List<Client> list, Client c)
    {
        //Verificar se não contém nenhum espaço em branco
        if(c.getName().isEmpty()
                || c.getName().contains(" ")
                || c.getEmail().contains(" ")
                || c.getEmail().isEmpty()
                || c.getPassword().contains(" ")
                || c.getSurname().isEmpty()
                || c.getSurname().contains(" "))
        {
            Toast.makeText(RegisterUser.this,
                    "Can't contain an empty space!", Toast.LENGTH_SHORT).show();
            return false;
        }

        //Verify if SNS number has 9 digits
        if(String.valueOf(c.getSNS()).length() != 9)
        {
            Toast.makeText(RegisterUser.this,
                    "SNS number must have 9 digits!", Toast.LENGTH_SHORT).show();
            return false;
        }

        //Verify if the entered mail is really a mail using regex
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if(!c.getEmail().matches(emailPattern))
        {
            Toast.makeText(RegisterUser.this,
                    "Invalid email", Toast.LENGTH_SHORT).show();
            return false;
        }

        //Verificar se a password tem mais de 7 chars
        if(c.getPassword().length() < 8)
        {
            Toast.makeText(RegisterUser.this,
                    "Password must have more than 8 characters!", Toast.LENGTH_SHORT).show();
            return false;
        }

        //Verificar se o user já existe seja o seu nº de sns como o seu email
        for(Client cl : list)
        {
            if(cl.getSNS() == c.getSNS() || cl.getEmail().equals(c.getEmail())) {
                Toast.makeText(RegisterUser.this,
                        "User already exists!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user);

        Intent iCameFromMain = getIntent();

        db = DataBase.getInstance(getApplicationContext());
        list = db.myDao().getClients();

        sns = (EditText) findViewById(R.id.snsEdit);
        name = (EditText) findViewById(R.id.nameEdit);
        surname = (EditText) findViewById(R.id.surnameEdit);
        email = (EditText) findViewById(R.id.emailEdit);
        password = (EditText) findViewById(R.id.passwordEdit);
        retypePassword = (EditText) findViewById(R.id.retypePasswordEdit);
        confirm = (Button) findViewById(R.id.confirm);
        cancel = (Button) findViewById(R.id.cancel);


        confirm.setOnClickListener(
                oView -> {
                    String strSNS = sns.getText().toString();
                    int numSNS;

                    //Check if the number is really an int
                    try {
                        numSNS = Integer.parseInt(strSNS);
                    } catch (NumberFormatException e) {
                        Toast.makeText(RegisterUser.this,
                                "SNS no. should be a number", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String strName = name.getText().toString();
                    String strSurname = surname.getText().toString();
                    String strEmail = email.getText().toString();
                    String strPass = password.getText().toString();
                    String strRePass = retypePassword.getText().toString();

                    //Verifica se a password é igual à que reescreveu
                    if(!strPass.equals(strRePass))
                    {
                        Toast.makeText(RegisterUser.this,
                                "Passwords don't match", Toast.LENGTH_SHORT).show();

                        return;
                    }


                    newClient = new Client(numSNS,strName,strEmail,strPass, strSurname);

                    //If all the information is in the correct form
                    if(checkUser(list, newClient))
                    {
                        newClient.setEmail(strEmail);
                        newClient.setName(strName);
                        newClient.setSurname(strSurname);
                        newClient.setSNS(Integer.parseInt(strSNS));
                        newClient.setPassword(strPass);
                        db.myDao().addClient(newClient);
                        Toast.makeText(RegisterUser.this,
                                "Success", Toast.LENGTH_SHORT).show();

                        Intent iInitialPage = new Intent(this, InitialPage.class);
                        iInitialPage.putExtra("SNS", strSNS);
                        finish();
                        this.overridePendingTransition(0, 0);
                        startActivity(iInitialPage);
                    }
                    System.out.println(newClient.toString());
                }
        );

        //Goes back to the front page
        cancel.setOnClickListener(
                oView -> super.finish()
        );

    }
}
