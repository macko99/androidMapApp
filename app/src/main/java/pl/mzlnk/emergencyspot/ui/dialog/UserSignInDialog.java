package pl.mzlnk.emergencyspot.ui.dialog;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.function.BiConsumer;

import pl.mzlnk.emergencyspot.R;
import pl.mzlnk.emergencyspot.ui.view.button.NoIconButton;

public class UserSignInDialog extends BaseDialog {

    private EditText login, password;
    private NoIconButton submit;

    private TextView newAccount;

    private BiConsumer<String, String> onSubmitListener;

    @Override
    protected int xmlLayoutResId() {
        return R.layout.d_user_sign_in;
    }

    @Override
    protected void loadViewsFromXml(View rootView) {
        this.login = rootView.findViewById(R.id.d_user_sign_in_input_login);
        this.password = rootView.findViewById(R.id.d_user_sign_in_input_password);
        this.submit = rootView.findViewById(R.id.d_user_sign_in_btn_submit);
        this.newAccount = rootView.findViewById(R.id.d_user_sign_in_btn_new_account);
    }

    @Override
    protected void prepareListeners() {
        this.submit.setOnClickListener(view -> {
            if(this.onSubmitListener != null) {
                this.onSubmitListener.accept(this.login.getText().toString(), this.password.getText().toString());
            }
        });

        this.newAccount.setOnClickListener(view -> {
            UserSignUpDialog dialog = new UserSignUpDialog();
            dialog.show(getFragmentManager(), "dialog");

            dismissAllowingStateLoss();
        });
    }

    public final void setOnSubmitListener(BiConsumer<String, String> onSubmitListener) {
        this.onSubmitListener = onSubmitListener;
    }

}
