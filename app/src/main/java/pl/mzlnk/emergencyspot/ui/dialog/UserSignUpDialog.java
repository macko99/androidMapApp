package pl.mzlnk.emergencyspot.ui.dialog;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.NoSuchElementException;

import pl.mzlnk.emergencyspot.R;
import pl.mzlnk.emergencyspot.model.user.NewUserDto;
import pl.mzlnk.emergencyspot.service.network.requests.UserSignUpHttpRequestParams;
import pl.mzlnk.emergencyspot.ui.view.button.NoIconButton;

import static pl.mzlnk.emergencyspot.EmergencySpotApplication.app;

public class UserSignUpDialog extends BaseDialog {

    private EditText login, firstName, lastName, pesel, password;
    private NoIconButton submit;

    private static void checkInput(EditText input) throws NoSuchElementException {
        if (isEmpty(input)) {
            throw new NoSuchElementException("Pole " + input.getHint() + " nie może być puste!");
        }
    }

    private static boolean isEmpty(EditText input) {
        return input.getText().toString().isEmpty();
    }

    @Override
    protected int xmlLayoutResId() {
        return R.layout.d_user_sign_up;
    }

    @Override
    protected void loadViewsFromXml(View rootView) {
        this.login = rootView.findViewById(R.id.d_user_sign_up_input_login);
        this.firstName = rootView.findViewById(R.id.d_user_sign_up_input_first_name);
        this.lastName = rootView.findViewById(R.id.d_user_sign_up_input_last_name);
        this.pesel = rootView.findViewById(R.id.d_user_sign_up_input_pesel);
        this.password = rootView.findViewById(R.id.d_user_sign_up_input_password);
        this.submit = rootView.findViewById(R.id.d_user_sign_up_btn_submit);
    }

    @Override
    protected void prepareListeners() {
        this.submit.setOnClickListener(view -> {
            try {
                checkInput(this.login);
                checkInput(this.firstName);
                checkInput(this.lastName);
                checkInput(this.pesel);
                checkInput(this.password);
            } catch (NoSuchElementException e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }

            NewUserDto userDto = new NewUserDto(
                    login.getText().toString(),
                    firstName.getText().toString(),
                    lastName.getText().toString(),
                    pesel.getText().toString(),
                    password.getText().toString()
            );

            app.networkService.makeRequestForObject(
                    new UserSignUpHttpRequestParams(userDto),
                    v -> {
                        UserSignInDialog dialog = new UserSignInDialog();
                        dialog.show(getFragmentManager(), "dialog");
                        dismissAllowingStateLoss();
                    },
                    error -> Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show())
            ;
        });
    }


}
