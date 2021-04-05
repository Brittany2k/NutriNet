package com.example.nutrinet;

import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {

    private NestedScrollView nestedScrollView;
    private TextInputLayout LayoutName;
    private TextInputLayout LayoutEmail;
    private TextInputLayout LayoutPassword;
    private TextInputLayout LayoutConfirmPassword;
    private TextInputEditText TextName;
    private TextInputEditText TextEmail;
    private TextInputEditText TextPassword;
    private TextInputEditText TextConfirmPassword;
    private Button ButtonRegister;
    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;
    private User user;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        initViews(view);
        initListeners(view);
        initObjects(view);

       return view;
    }

    private void initViews(View view) {
        nestedScrollView = (NestedScrollView) view.findViewById(R.id.nestedScrollView);
        LayoutName = (TextInputLayout) view.findViewById(R.id.textInputLayoutName);
        LayoutEmail = (TextInputLayout) view.findViewById(R.id.textInputLayoutEmail);
        LayoutPassword = (TextInputLayout) view.findViewById(R.id.textInputLayoutPassword);
        LayoutConfirmPassword = (TextInputLayout) view.findViewById(R.id.textInputLayoutConfirmPassword);
        TextName = (TextInputEditText) view.findViewById(R.id.et_name);
        TextEmail = (TextInputEditText) view.findViewById(R.id.et_email);
        TextPassword = (TextInputEditText) view.findViewById(R.id.et_password);
        TextConfirmPassword = (TextInputEditText) view.findViewById(R.id.et_repassword);
        ButtonRegister = (Button) view.findViewById(R.id.btn_register);

    }
    /**
     * This method is to initialize listeners
     */
    private void initListeners(View view) {
        ButtonRegister.setOnClickListener(this);

    }
    /**
     * This method is to initialize objects to be used
     */
    private void initObjects(View view) {
        inputValidation = new InputValidation(getActivity());
        databaseHelper = new DatabaseHelper(getActivity());
        user = new User();
    }

    /**
     * This implemented method is to listen the click on view
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                postDataToSQLite();
                break;
            /*case R.id.appCompatTextViewLoginLink:
                finish();
                break;*/
        }
    }
    /**
     * This method is to validate the input text fields and post data to SQLite
     */
    private void postDataToSQLite() {
        if (!inputValidation.isInputEditTextFilled(TextName, LayoutName, getString(R.string.error_message_name))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(TextEmail, LayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(TextEmail, LayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(TextPassword, LayoutPassword, getString(R.string.error_message_password))) {
            return;
        }
        if (!inputValidation.isInputEditTextMatches(TextPassword, TextConfirmPassword,
                LayoutConfirmPassword, getString(R.string.error_password_match))) {
            return;
        }
        if (!databaseHelper.checkUser(TextEmail.getText().toString().trim())) {
            user.setName(TextName.getText().toString().trim());
            user.setEmail(TextEmail.getText().toString().trim());
            user.setPassword(TextPassword.getText().toString().trim());
            databaseHelper.addUser(user);
            // Snack Bar to show success message that record saved successfully
            Snackbar.make(nestedScrollView, getString(R.string.msg_Success), Snackbar.LENGTH_LONG).show();
            emptyInputEditText();
        } else {
            // Snack Bar to show error message that record already exists
            Snackbar.make(nestedScrollView, getString(R.string.error_email_exists), Snackbar.LENGTH_LONG).show();
        }
    }
    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        TextName.setText(null);
        TextEmail.setText(null);
        TextPassword.setText(null);
        TextConfirmPassword.setText(null);
    }


}