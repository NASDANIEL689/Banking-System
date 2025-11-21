public class LegacyLoginController {
    private LoginService loginService = new LoginService();

    public LegacyLoginController() {
        // legacy simple login helper retained for console controllers
    }

    public boolean login(String username, String password) {
        return loginService.login(username, password);
    }

    public void logout(String username) {
        loginService.logout(username);
    }
}
