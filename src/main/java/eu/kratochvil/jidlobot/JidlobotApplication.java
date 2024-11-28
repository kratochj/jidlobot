package eu.kratochvil.jidlobot;


import com.slack.api.socket_mode.SocketModeClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JidlobotApplication {

    private static final Logger log = LoggerFactory.getLogger(JidlobotApplication.class);


    public static void main(String[] args) {
        SpringApplication.run(JidlobotApplication.class, args);
    }

}
