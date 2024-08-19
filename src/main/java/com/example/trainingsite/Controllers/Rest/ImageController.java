package com.example.trainingsite.Controllers.Rest;


import com.example.trainingsite.repository.NewsRep;
import com.example.trainingsite.repository.UserRepo;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bdimage")
public class ImageController {

    private UserRepo userRepo;
    private NewsRep newsRep;

    public ImageController(NewsRep newsRep,UserRepo userRepo){
        this.newsRep = newsRep;
        this.userRepo = userRepo;
    }


    @GetMapping(value = "{imageId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public Resource downloadImage(@PathVariable("imageId") int imageId) {
        byte[] image = newsRep.findByteImage(imageId);
        return new ByteArrayResource(image);
    }

    @GetMapping(value = "user/{username}", produces = MediaType.IMAGE_JPEG_VALUE)
    public Resource downloadUserImage(@PathVariable("username") String username) {
        byte[] image = userRepo.findByteImage(username);
        return new ByteArrayResource(image);
    }
}


