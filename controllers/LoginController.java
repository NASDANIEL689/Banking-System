public class LoginController {
    private LoginService loginService = new LoginService();

    public LoginController() {
        // add a default user for demo
        // using reflection to access private map isn't ideal; so use the users map directly by adding a helper in LoginService would be better
    }

    public boolean login(String username, String password) {
        return loginService.login(username, password);
    }

    public void logout(String username) {
        loginService.logout(username);
    }
}
