package bankapp;

import java.util.HashMap;
import java.util.Map;

public class LoginService implements ILoginService {
    private Map<String, String> users = new HashMap<>();

    @Override
    public boolean login(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }

    @Override
    public void logout(String username) {
        // Implement logout logic if needed
    }
}
