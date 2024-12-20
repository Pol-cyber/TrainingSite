package com.example.trainingsite.Controllers.Rest;


import com.example.trainingsite.entity.News;
import com.example.trainingsite.configuration.EmailMessagingGateway;
import com.example.trainingsite.repository.NewsRep;
import com.example.trainingsite.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/news")
public class NewsController {

    @Autowired
    private NewsRep newsRep;
    @Autowired
    private UserRepo userRepo;

    @Value("${mail.email}")
    private String emailForNewsletter;

    @Autowired
    private EmailMessagingGateway emailMessagingGateway;

    @PostMapping(value = "/add",consumes= MediaType.MULTIPART_FORM_DATA_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> uploadFile(@RequestParam(required=false, value="image-news",name = "image-news") MultipartFile file,
                                     @RequestParam(name = "title") String title,
                                     @RequestParam(name = "text") String text,
                                     @RequestParam(name = "link") String link,
                                     @RequestParam(name = "addToMailing", defaultValue = "false") Boolean addToMailing){
        try {
            News news = new News();
            news.setTitle(title);
            news.setText(text);
            if(link != null && !link.isEmpty()){
                news.setLink(link);
            }
            if(file.getBytes().length > 0){
                news.setImage(file.getBytes());
            }
            if(addToMailing){
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setSubject(title);
                String textEmail = text;
                if(!link.isEmpty()){
                    textEmail+="\nLink: "+link;
                }
                mailMessage.setText(textEmail);
                String[] emails = userRepo.getEmailsByNewsletterSubIsTrue().toArray(new String[0]);
                mailMessage.setTo(emails);
                mailMessage.setFrom(emailForNewsletter);
                emailMessagingGateway.addToNewsletter(mailMessage);
            }
            newsRep.save(news);
        } catch (Exception e){
            return new ResponseEntity<>("Помилка", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Успішно створено", HttpStatus.OK);
    }


    @PatchMapping("/delete-news/{newsId}")
    public ResponseEntity<String> changeActualNews(@PathVariable(value = "newsId") long newsId){
        try {
            newsRep.changeActual(newsId);
        } catch (Exception e){
            return new ResponseEntity<>("Помилка зміни актуальності", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Новину успішно забарано із актуальних", HttpStatus.OK);
    }
}
