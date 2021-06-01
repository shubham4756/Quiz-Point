package com.team.quizpoint.controller;

import com.team.quizpoint.model.ResetPasswordData;
import com.team.quizpoint.service.CustomerAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PasswordChangeController {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private CustomerAccountService customerAccountService;

    @GetMapping("/forgotPasswordEmail")
    public String fotgotPasswordEmail(Model model) {
        model.addAttribute("forgotPassword", new ResetPasswordData());
        return "forgetPassword/forgotPasswordEmail";
    }

    @PostMapping("/password/request")
    public String resetPassword(final ResetPasswordData forgotPasswordForm,Model model) {
        try {
            customerAccountService.forgottenPassword(forgotPasswordForm.getEmail());
            model.addAttribute("success", "Mail has been send on your registered email !!");
        } catch (Exception e) {
            model.addAttribute("error", "Your Given Email is not registered yet !!");
        }
        return "login";
    }

    @GetMapping("/password/change")
    public String changePassword(@RequestParam(required = false) String token,Model model) {
        if (StringUtils.isEmpty(token)) {
            model.addAttribute("error", "Error - Verification Token Is Missing !!");
            return "redirect:login";
        }
        ResetPasswordData data = new ResetPasswordData();
        data.setToken(token);
        setResetPasswordForm(model, data);
        return "forgetPassword/forgetPassword";
    }

    @PostMapping("/password/change")
    public String changePassword(final ResetPasswordData data,Model model) {
        try {
            customerAccountService.updatePassword(data.getPassword(), data.getToken());
            model.addAttribute("success", "Your Password has Been changed !!");
        } catch (Exception e) {
            model.addAttribute("error", "Error - Verification Token Is Invalid !!");
            return "login";
        }
        setResetPasswordForm(model, new ResetPasswordData());
        return "login";
    }

    private void setResetPasswordForm(final Model model, ResetPasswordData data){
        model.addAttribute("forgotPassword",data);
    }
}