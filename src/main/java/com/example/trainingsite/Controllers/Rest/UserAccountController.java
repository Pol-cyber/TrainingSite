package com.example.trainingsite.Controllers.Rest;


import com.example.trainingsite.entity.User;
import com.example.trainingsite.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user/account")
public class UserAccountController {

    private UserRepo userRepo;

    public UserAccountController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    @Transactional
    @PatchMapping(value = "/changeImage",consumes= MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> changeUserImage(@RequestParam(value="account-image",name = "account-image") MultipartFile file, Authentication authentication) {
        try{
            if(file.getBytes().length > 0){
                byte[] bytes = file.getBytes();
                User user = ((User) authentication.getPrincipal());
                userRepo.updateImageByLogin(user.getUsername(), bytes);
                user.setImage(bytes);
            } else {
                return new ResponseEntity<>("Помилка зображення", HttpStatus.CONFLICT);
            }
        } catch (Exception e){
           return new ResponseEntity<>("Помилка сервера, повторіть пізніше", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Зображення збережено, оновіть сторінку", HttpStatus.OK);
    }


    @Transactional
    @PatchMapping(value = "/changeSubscription")
    public ResponseEntity<String> changeSubscription(@RequestParam boolean subscribe, Authentication authentication) {
        try {
            // Оновлюємо підписку в залежності від значення параметра "subscribe"
            userRepo.updateNewsletterSubscription(((User) authentication.getPrincipal()).getUsername(), subscribe);
        } catch (Exception e) {
            return new ResponseEntity<>("Помилка сервера, повторіть пізніше", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Повідомляємо користувача, чи він підписався, чи відписався
        if (subscribe) {
            return new ResponseEntity<>("Ви успішно підписалися на розсилку", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Ви успішно відписалися від розсилки", HttpStatus.OK);
        }
    }


}
