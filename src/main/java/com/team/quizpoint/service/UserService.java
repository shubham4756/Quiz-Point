package com.team.quizpoint.service;

import com.mongodb.client.result.UpdateResult;
import com.team.quizpoint.context.email.AccountVerificationEmailContext;
import com.team.quizpoint.model.AttendedQuiz;
import com.team.quizpoint.model.Quiz;
import com.team.quizpoint.model.token.SecureToken;
import com.team.quizpoint.model.User;
import com.team.quizpoint.repository.SecureTokenRepository;
import com.team.quizpoint.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.mail.MessagingException;
import java.util.Objects;

@Service
public class UserService {

    @Autowired
    MongoTemplate template;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private SecureTokenService secureTokenService;

    @Autowired
    SecureTokenRepository secureTokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${site.base.url.https}")
    private String baseURL;

    public User addUser(User user) {
        User u = template.insert(user,"users");
        sendRegistrationConformationEmail(u);
        return u;
    }

    public void sendRegistrationConformationEmail(User user) {
        SecureToken secureToken= secureTokenService.createSecureToken();
        secureToken.setUser(user);
        secureTokenRepository.save(secureToken);
        AccountVerificationEmailContext emailContext = new AccountVerificationEmailContext();
        emailContext.init(user);
        emailContext.setToken(secureToken.getToken());
        emailContext.buildVerificationUrl(baseURL, secureToken.getToken());
        try {
            emailService.sendMail(emailContext);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public boolean emailVerificaton(String token) throws InvalidCsrfTokenException{
        SecureToken secureToken = secureTokenService.findByToken(token);
        if(Objects.isNull(secureToken) || !StringUtils.equals(token, secureToken.getToken()) || secureToken.isExpired())
            throw new InvalidCsrfTokenException(new DefaultCsrfToken(token,token,token),"Not valid Token!!");

        User user = userRepository.findUserByUserId(secureToken.getUser().getUserId());
        if(Objects.isNull(user)){
            return false;
        }
        template.updateFirst(BasicQuery.query(Criteria.where("email").is(user.getEmail())),new Update().set("active",true),User.class,"users");
        secureTokenService.removeToken(secureToken);
        return true;
    }

    public boolean updateProfile(User newUser,User oldUser) {
        UpdateResult updateResult = template.updateFirst(BasicQuery.query(Criteria.where("email").is(oldUser.getEmail())),
                new Update().set("firstname", newUser.getFirstname())
                        .set("lastname", newUser.getLastname())
                        .set("gender", newUser.getGender())
                        .set("contactNumber", newUser.getContactNumber())
                        .set("city", newUser.getCity()), User.class, "users");
        return updateResult.wasAcknowledged();
    }

    public boolean changePassword(String oldPassword,String newPassword,String email) {
        User user = userRepository.findUserByEmail(email).get();
        if(user!=null && passwordEncoder.matches(oldPassword,user.getPassword())) {
            template.updateFirst(BasicQuery.query(Criteria.where("email").is(email)),
                    new Update().set("password",passwordEncoder.encode(newPassword)),User.class,"users");
            return true;
        } else {
            System.out.println("User is null");
            return false;
        }
    }

    public boolean addQuizInUser(String username, String quizId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(username));

        Update update = new Update();
        update.push("createdQuizzes", quizId);

        return template.updateFirst(query, update, "users").wasAcknowledged();

//        template.updateFirst();
    }

    public String getUserIdByEmail(String email) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));

        System.out.println(email);

        System.out.println(template.findOne(query,User.class, "users"));

        return template.findOne(query,User.class, "users").getUserId();
    }

    public String getEmailByUserId(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(userId));

        System.out.println(userId);

        System.out.println(template.findOne(query,User.class, "users"));

        return template.findOne(query,User.class, "users").getEmail();
    }

    public boolean existsUserByEmail(String email) {
        return userRepository.existsUserByEmail(email);
    }

    public void addAttendedQuizInUser(String userId, AttendedQuiz attendedQuiz) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(userId));

        Update update = new Update();
        update.push("attendedQuizzes", attendedQuiz);

        System.out.println(template.updateFirst(query, update, "users").getModifiedCount());
    }

    public void updateTotalPoints(String userId, String quizId, String points) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(userId));
        query.addCriteria(Criteria.where("attendedQuizzes").elemMatch(Criteria.where("quizId").is(quizId)));

        Update update = new Update();
        update.set("attendedQuizzes.$.totalPoints", points);

        System.out.println(userId);
        System.out.println(quizId);
        System.out.println(points);

        System.out.println(template.findOne(query, User.class, "users"));

        template.updateFirst(query,
                update,
                User.class);
    }

    public String getUserPoints(String userId, String quizId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(userId));
        query.addCriteria(Criteria.where("attendedQuizzes").elemMatch(Criteria.where("quizId").is(quizId)));

        query.fields().include("attendedQuizzes.$.");

        return template.findOne(query, User.class, "users").getAttendedQuizzes().get(0).getTotalPoints();
    }

}
