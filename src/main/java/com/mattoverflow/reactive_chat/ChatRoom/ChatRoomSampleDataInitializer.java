package com.mattoverflow.reactive_chat.ChatRoom;

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
public class ChatRoomSampleDataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final ChatRoomRepository chatRoomRepository;


    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        chatRoomRepository.deleteAll()
                .thenMany(Flux.just("Welcome", "Help").map(ChatRoom::new).flatMap(chatRoomRepository::save))
                .thenMany(chatRoomRepository.findAll())
                .subscribe(chatRoom -> log.info("Saving chat room with name: " + chatRoom.getName()));
    }
}
