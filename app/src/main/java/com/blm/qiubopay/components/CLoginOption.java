package com.blm.qiubopay.components;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.Executor;

import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.interfaces.IActivityResult;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;


public class CLoginOption {

    private HActivity context;
    private BiometricPrompt biometricPrompt;

    public static CLoginOption create(HActivity context) {
        return new CLoginOption(context);
    }

    private CLoginOption(HActivity context) {
        this.context = context;
    }

    public CLoginOption onGmail(Button btn_gmail, IClick click) {

        if(btn_gmail == null)
            return this;

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

        btn_gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getContext().setOnActivityResult(new IActivityResult() {
                    @Override
                    public void onActivityResult(int requestCode, int resultCode, Intent data) {

                        try {

                            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                            GoogleSignInAccount account = task.getResult(ApiException.class);

                            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
                            if (acct != null) {

                                String personId = acct.getId();
                                String personEmail = acct.getEmail();
                                String personGivenName = acct.getGivenName();
                                String personFamilyName = acct.getFamilyName();

                                click.onClick(new LoginDTO(personId, personEmail, personGivenName, personFamilyName, "Google"));

                            }

                            mGoogleSignInClient.signOut();

                        } catch (ApiException e) {
                            e.printStackTrace();
                        }

                    }
                });

                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                getContext().startActivityForResult(signInIntent, 1);
            }
        });

        return this;
    }

    public CLoginOption onFacebook(Button btn_facebook, IClick click) {

        return this;
    }

    public void disconnectFacebook() {

    }

    public CLoginOption onBiometric(View btn_biometric, IBiometric biometric) {

        String message = "";
        Boolean result = true;

        BiometricManager biometricManager = BiometricManager.from(getContext());
        switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                message = "Usaste huella digital para logearte";
            break;
            case BiometricManager.BIOMETRIC_STATUS_UNKNOWN:
            case BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED:
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                message = "Este dispositivo no cuenta con huella digital";
                result = false;
            break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                message = "Esta deshabilitado";
                result = false;
            break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                message = "No cuenta con ninguna huella registrada";
                result = false;
            break;
        }

        if(!result) {

            if(btn_biometric != null)
                btn_biometric.setVisibility(View.GONE);

            biometric.onError(false, message);
            return this;
        }

        Executor executor = ContextCompat.getMainExecutor(getContext());

        biometricPrompt = new BiometricPrompt(getContext(), executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                biometric.onError(true, errorCode + " : " + errString);
            }
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                biometric.onSuccess();
            }
            @Override
            public void onAuthenticationFailed() {
                biometric.onError(true, "Authentication Failed");
            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Autenticación")
                .setDescription("Ingresar usando huella digital")
                .setNegativeButtonText("Cancelar")
                .build();

        if(btn_biometric != null)
            btn_biometric.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    biometricPrompt.authenticate(promptInfo);
                }
            });

        biometric.onError(true, message);

        return this;
    }

    public CLoginOption cancelAuthentication() {

        if(biometricPrompt != null)
            biometricPrompt.cancelAuthentication();

        return this;
    }

    public static String getSHA(HActivity context) {

        try {

            PackageInfo info = context.getPackageManager().getPackageInfo("com.blm.qiuboplus", PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.DEFAULT);
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "";
    }

    public HActivity getContext() {
        return context;
    }

    public interface IClick {
        void onClick(LoginDTO data);
    }

    public interface IBiometric {

        void onSuccess();

        void onError(boolean enabled, String message);

    }

    public class LoginDTO {

        private String id;
        private String email;
        private String firstName;
        private String lastName;
        private String type;

        public LoginDTO(String id, String email, String firstName, String lastName, String type) {
            this.id = id;
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

}
