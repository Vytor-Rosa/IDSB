package net.weg.gedesti.component;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
public class UserPresenceManagerInterceptor implements ChannelInterceptor {

    private final UserPresenceManager userPresenceManager;

    public UserPresenceManagerInterceptor(UserPresenceManager userPresenceManager) {
        this.userPresenceManager = userPresenceManager;
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String userId = accessor.getUser().getName();
            userPresenceManager.userConnected(userId);
        } else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
            String userId = accessor.getUser().getName();
            userPresenceManager.userDisconnected(userId);
        }
    }
}




