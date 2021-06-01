package com.team.quizpoint.service;

import com.team.quizpoint.context.email.ForgotPasswordEmailContext;
import com.team.quizpoint.model.User;
import com.team.quizpoint.model.token.SecureToken;
import com.team.quizpoint.repository.SecureTokenRepository;
import com.team.quizpoint.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.mail.MessagingException;
import java.util.Objects;

@Service("customerAccountService")
public class CustomerAccountService {
    @Autowired
    UserService userService;

    @Autowired
    MongoTemplate template;

    @Autowired
    private SecureTokenService secureTokenService;

    @Autowired
    SecureTokenRepository secureTokenRepository;

    @Value("${site.base.url.https}")
    private String baseURL;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void forgottenPassword(String email) {
        User user= userRepository.findUserByEmail(email).get();
        sendResetPasswordEmail(user);
    }

    public void updatePassword(String password, String token) throws Exception {
        SecureToken secureToken = secureTokenService.findByToken(token);
        if(Objects.isNull(secureToken) || !StringUtils.equals(token, secureToken.getToken()) || secureToken.isExpired()){
            throw new Exception("Token is not valid");
        }
        User user = userRepository.findUserByUserId(secureToken.getUser().getUserId());
        if(Objects.isNull(user)){
            throw new Exception("unable to find user for the token");
        }
        secureTokenService.removeToken(secureToken);
        template.updateFirst(BasicQuery.query(Criteria.where("email").is(user.getEmail())),new Update().set("password",passwordEncoder.encode(password)),User.class,"users");
    }

    protected void sendResetPasswordEmail(User user) {
        SecureToken secureToken= secureTokenService.createSecureToken();
        secureToken.setUser(user);
        secureTokenRepository.save(secureToken);
        ForgotPasswordEmailContext emailContext = new ForgotPasswordEmailContext();
        emailContext.init(user);
        emailContext.setToken(secureToken.getToken());
        emailContext.buildVerificationUrl(baseURL, secureToken.getToken());
        try {
            emailService.sendMail(emailContext);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

//    public boolean loginDisabled(String username) {
//        User user = userRepository.findUserByEmail(username).get();
//        return user != null;
//    }
}