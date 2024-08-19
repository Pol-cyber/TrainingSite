package com.example.trainingsite.Controllers.Rest;


import com.example.trainingsite.Entity.News;
import com.example.trainingsite.Entity.User;
import com.example.trainingsite.repository.NewsRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.regex.Pattern;

@RestController
@RequestMapping("/news")
public class NewsController {

    @Autowired
    private NewsRep newsRep;

    @PostMapping(value = "/add",consumes= MediaType.MULTIPART_FORM_DATA_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> uploadFile(@RequestParam(required=false, value="image-news",name = "image-news") MultipartFile file,
                                     @RequestParam(name = "title") String title,
                                     @RequestParam(name = "text") String text,
                                     @RequestParam(name = "link") String link){
        try {
            News news = new News();
            news.setTitle(title);
            news.setText(text);
            news.setLink(link);
            if(file.getBytes().length > 0){
                news.setImage(file.getBytes());
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
