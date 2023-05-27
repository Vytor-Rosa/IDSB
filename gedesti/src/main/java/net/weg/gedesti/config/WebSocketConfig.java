package net.weg.gedesti.config;
import net.weg.gedesti.component.UserPresenceManagerInterceptor;
import net.weg.gedesti.component.UserPresenceManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final UserPresenceManager userPresenceManager;

    public WebSocketConfig(UserPresenceManager userPresenceManager) {
        this.userPresenceManager = userPresenceManager;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/api");

    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/api/ws") // Caminho para conexão
                .setAllowedOrigins("http://localhost:3000") // Quem pode usar
                .withSockJS() // Protocolo
                .setSessionCookieNeeded(true); // Permissão leitura dos cookies
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new UserPresenceManagerInterceptor(userPresenceManager));
    }

}