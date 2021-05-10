package com.example.nutrinet.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private NestedScrollView nestedScrollView;
    private TextInputLayout LayoutEmail;
    private TextInputLayout LayoutPassword;
    private TextInputEditText email_box;
    private TextInputEditText password_box;

    private Button login_button;

    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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

    private void initViews(View view) {
        nestedScrollView = (NestedScrollView) view.findViewById(R.id.nestedScrollView);
        LayoutEmail = (TextInputLayout) view.findViewById(R.id.textInputLayoutEmail);
        LayoutPassword = (TextInputLayout) view.findViewById(R.id.textInputLayoutPassword);
        email_box = (TextInputEditText) view.findViewById(R.id.et_email);
        password_box = (TextInputEditText) view.findViewById(R.id.et_password);
        login_button = (Button) view.findViewById(R.id.btn_login);
    }

    /**
     * This method is to initialize listeners
     */
    private void initListeners(View view) {
        login_button.setOnClickListener(this);
    }
    /**
     * This method is to initialize objects to be used
     */
    private void initObjects(View view) {
        databaseHelper = new DatabaseHelper(getActivity());
        inputValidation = new InputValidation(getActivity());
    }

    /**
     * This implemented method is to listen the click on view
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                verifyFromSQLite(v);
                break;
            /*case R.id.textViewLinkRegister:
                // Navigate to RegisterActivity
                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);
                break;*/
        }
    }

    /**
     * This method is to validate the input text fields and verify login credentials from SQLite
     */
    private void verifyFromSQLite(View v) {
        if (!inputValidation.isInputEditTextFilled(email_box, LayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(email_box, LayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(password_box, LayoutPassword, getString(R.string.error_message_email))) {
            return;
        }
        if (databaseHelper.checkUser(email_box.getText().toString().trim()
                , password_box.getText().toString().trim())) {
            //if successful, then proceed in app
            Snackbar.make(nestedScrollView, getString(R.string.msg_Success), Snackbar.LENGTH_LONG).show();
            emptyInputEditText();

            int howmany = MainActivity.pagerAdapter.getCount();

            Intent intent = new Intent (getActivity(), SearchActivity.class);
            startActivity(intent);


        } else {
            // Snack Bar to show success message that record is wrong
            Snackbar.make(nestedScrollView, getString(R.string.error_valid_email_password), Snackbar.LENGTH_LONG).show();
        }
    }
    /**
     * This method is to empty all input edit text
     */
    class AuthenticationPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragmentList = new ArrayList<>();

        public AuthenticationPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return fragmentList.get(i);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        void addFragmet(Fragment fragment) {
            fragmentList.add(fragment);
        }

        void removeFragment(Fragment fragment)
        {
            fragmentList.remove(fragment);
        }
    }
    private void emptyInputEditText() {
        email_box.setText(null);
        password_box.setText(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initViews(view);
        initListeners(view);
        initObjects(view);
        return view;
    }
}