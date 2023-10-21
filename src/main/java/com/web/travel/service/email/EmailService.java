package com.web.travel.service.email;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import com.web.travel.dto.ResDTO;
import com.web.travel.service.AuthService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import com.web.travel.payload.request.MailRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
@Service
public class EmailService {
    @Autowired
    private JavaMailSender sender;
    @Autowired
    private Configuration config;
    @Autowired
    private AuthService authService;
    @Value("${travel.app.client.host}")
    private String clientHost;
    public ResDTO sendWelcomeEmail(MailRequest request) {
        ResDTO response = new ResDTO();
        response.setData(null);
        MimeMessage message = sender.createMimeMessage();
        Map<String, Object> model = new HashMap<>();
        model.put("name", request.getName());
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            //add attachment
            //helper.addAttachment("logo.jpg", new ClassPathResource("logo.jpg"));
            //render template
            Template t = config.getTemplate("email-template.ftl");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
            helper.setTo(request.getTo());
            helper.setText(html, true);
            helper.setSubject(request.getSubject());
            helper.setFrom(request.getFrom(), "Travel Vn");
            sender.send(message);
            response.setCode(HttpServletResponse.SC_OK);
            response.setMessage("Mail sent to: " + request.getTo());
            response.setStatus(Boolean.TRUE);
        } catch (MessagingException | IOException | TemplateException e) {
            response.setMessage("Mail sent failure : " + e.getMessage());
            response.setStatus(Boolean.FALSE);
            response.setCode(HttpServletResponse.SC_BAD_REQUEST);
        }
        return response;
    }

    public ResDTO sendResetPasswordEmail(MailRequest request, Map<String, Object> model) {
        ResDTO response = new ResDTO();
        response.setData(null);
        MimeMessage message = sender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            //add attachment
            //helper.addAttachment("logo.jpg", new ClassPathResource("logo.jpg"));
            //render template
            Template t = config.getTemplate("reset-password/reset-password-template.html");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
            helper.setTo(request.getTo());
            helper.setText(html, true);
            helper.setSubject(request.getSubject());
            helper.setFrom(request.getFrom(), "Travel Vn");
            sender.send(message);
            response.setCode(HttpServletResponse.SC_OK);
            response.setMessage("Mail sent to: " + request.getTo());
            response.setStatus(Boolean.TRUE);
        } catch (MessagingException | IOException | TemplateException e) {
            response.setMessage("Mail sent failure : " + e.getMessage());
            response.setStatus(Boolean.FALSE);
            response.setCode(HttpServletResponse.SC_BAD_REQUEST);
        }
        return response;
    }

    public ResDTO sendConfirmationEmail(String email){
        ResDTO response = new ResDTO();
        response.setData(null);

        MailRequest request = new MailRequest();
        request.setFrom("travel-vn");
        request.setSubject("ACCOUNT CONFIRMATION");
        request.setTo(email);

        Map<String, Object> model = new HashMap<>();
        model.put("name", authService.getUserFullNameFromEmail(email));
        model.put("clientHost", clientHost);

        String confirmationCode = authService.generateConfirmationCode();
        String token = authService.encodeResetPasswordToken(authService.createConfirmationCodeToken(email, confirmationCode));
        String[] confirmationCodeSplit = confirmationCode.split("");
        for(int i = 0; i < confirmationCodeSplit.length; i++){
            model.put("digit" + String.valueOf(i), confirmationCodeSplit[i]);
        }

        MimeMessage message = sender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            //add attachment
            //helper.addAttachment("logo.jpg", new ClassPathResource("logo.jpg"));
            //render template
            Template t = config.getTemplate("confirmation/confirmation-template.html");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
            helper.setTo(request.getTo());
            helper.setText(html, true);
            helper.setSubject(request.getSubject());
            helper.setFrom(request.getFrom(), "Travel Vn");
            sender.send(message);
            response.setCode(HttpServletResponse.SC_OK);
            response.setMessage("Mail sent to: " + request.getTo());
            response.setStatus(Boolean.TRUE);

            Map<String, String> tokenDTO = new HashMap<>();
            tokenDTO.put("token", token);
            response.setData(tokenDTO);

        } catch (MessagingException | IOException | TemplateException e) {
            response.setMessage("Mail sent failure : " + e.getMessage());
            response.setStatus(Boolean.FALSE);
            response.setCode(HttpServletResponse.SC_BAD_REQUEST);
        }
        return response;
    }
}