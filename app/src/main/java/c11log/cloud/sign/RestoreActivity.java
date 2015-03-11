package c11log.cloud.sign;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import c11log.cloud.R;

public class RestoreActivity extends Activity{
    private EditText emailView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        emailView = (EditText) findViewById(R.id.email);
    }

    void attemptRestorePassword() {
        emailView.setError(null);

        String email = emailView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid email address.
        if (TextUtils.isEmpty(email)  ) {
            emailView.setError(getString(R.string.error_field_required));
            focusView = emailView;
            cancel = true;
        }
        if(!(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            emailView.setError((getString((R.string.error_invalid_email))));
            focusView = emailView;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            new AlertDialog.Builder(this)
                                        .setTitle("Reset password")
                    .setMessage("A email with instructions for how to restore your password was sent to your email address.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(RestoreActivity.this, SignInActivity.class);
                            startActivity(intent);
                        }

                    })
                    .show();
        }
    }

    public void restorePasswordListner() {
        attemptRestorePassword();
    }
}
