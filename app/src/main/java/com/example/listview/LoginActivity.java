package com.example.listview;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.safetynet.SafetyNetClient;

public class LoginActivity extends AppCompatActivity {

    private SafetyNetClient safetyNetClient;
    private Button buttonLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        safetyNetClient = SafetyNet.getClient(this);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyCaptcha();
            }
        });
    }

    private void verifyCaptcha() {
        safetyNetClient.verifyWithRecaptcha("<YOUR_RECAPTCHA_SITE_KEY>")
                .addOnSuccessListener(this, response -> {
                    if (response.getTokenResult().isEmpty()) {
                        // Captcha verificado con éxito, redirigir a RegisterActivity
                        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                    } else {
                        // Error al verificar el captcha
                        Toast.makeText(LoginActivity.this, "Captcha verification failed", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(this, e -> {
                    if (e instanceof ApiException) {
                        ApiException apiException = (ApiException) e;
                        int statusCode = apiException.getStatusCode();
                        // Manejar el error de la API de SafetyNet
                        if (statusCode == CommonStatusCodes.NETWORK_ERROR) {
                            Toast.makeText(LoginActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "SafetyNet API error", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Otro tipo de error
                        Toast.makeText(LoginActivity.this, "Error occurred", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
