package com.example.pchart;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import static android.text.TextUtils.isEmpty;

public class PasswordResetDialog extends DialogFragment {

    private static final String TAG = "PasswordResetDialog";

    //widgets
    private EditText mEmail;

    //vars
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_resetpassword, container, false);
        mEmail = view.findViewById(R.id.email_password_reset);
        mContext = getActivity();

        TextView confirmDialog = view.findViewById(R.id.dialogConfirm);
        confirmDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEmpty(mEmail.getText().toString().trim())) {
                    Log.d(TAG, "onClick: attempting to send reset link to: " + mEmail.getText().toString().trim());
                    sendPasswordResetEmail(mEmail.getText().toString());
                    getDialog().dismiss();
                } else if (isEmpty(mEmail.getText().toString())) {
                    mEmail.setError(getString(R.string.enter_email));
                    mEmail.setFocusable(true);
                } else {
                    Toast.makeText(mContext, R.string.no_spaces, Toast.LENGTH_SHORT).show();
                    mEmail.setText("");
                    mEmail.setFocusable(true);
                }
            }
        });

        return view;
    }

    /**
     * Send a password reset link to the email provided
     *
     * @param email
     */
    public void sendPasswordResetEmail(String email) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: Password Reset Email sent.");
                            Toast.makeText(mContext, "Password Reset Link Sent To Your Email",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d(TAG, "onComplete: No user associated with that email.");
                            Toast.makeText(mContext, "No User Is Associated With That Email",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * Return true if the @param is null
     *
     * @param string
     * @return
     */
    private boolean isEmpty(String string) {
        return string.equals("");
    }
}
