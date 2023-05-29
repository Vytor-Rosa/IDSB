package net.weg.gedesti.component;
import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.Set;

@Component
public class UserPresenceManager {

    private Set<String> onlineUsers = new HashSet<>();

    public void userConnected(String userId) {
        System.out.println("user connected: " + onlineUsers);
        onlineUsers.add(userId);
    }

    public void userDisconnected(String userId) {
        onlineUsers.remove(userId);
    }

    public boolean isUserOnline(String userId) {
        System.out.println(userId);
        return onlineUsers.contains(userId);
    }
}

