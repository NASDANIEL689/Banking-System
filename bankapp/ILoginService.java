package bankapp;

public interface ILoginService {
    boolean login(String username, String password);
    void logout(String username);
}
