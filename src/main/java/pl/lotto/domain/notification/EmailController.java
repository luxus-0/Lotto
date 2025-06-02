package pl.lotto.domain.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/emails")
public class EmailController {

    private final EmailNotificationSender emailSender;

    @PostMapping("/send")
    public ResponseEntity<Object> send(){
        emailSender.send();
        return ResponseEntity.status(CREATED).build();
    }
}
