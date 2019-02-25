package skeleton;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

@SpringBootApplication
public class SkeletonApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SkeletonApplication.class);
        app.addListeners(new ApplicationPidFileWriter("skeleton.pid"));
        app.run(args);
    }
}
