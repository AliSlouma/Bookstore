package database.entities;

public interface OnLogin {
    void onLoginSuccess(User user);

    void onLoginFailed();
}
