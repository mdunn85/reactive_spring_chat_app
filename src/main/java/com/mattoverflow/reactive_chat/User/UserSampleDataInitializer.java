package com.mattoverflow.reactive_chat.User;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Log4j2
@Component
@Profile("demo")
@AllArgsConstructor
public class UserSampleDataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private UserRepository userRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        this.userRepository.deleteAll()
                .thenMany(Flux.just("Matt", "Chloe").map(User::new).flatMap(userRepository::save))
                .subscribe(user -> log.info("Saving user with name: " + user.getName()));
    }
}
