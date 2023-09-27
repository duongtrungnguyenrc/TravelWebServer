package com.web.travel.service.email;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.web.travel.dto.ResDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import com.web.travel.payload.request.MailRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResDTO sendWelcomeEmail(MailRequest request, Map<String, Object> model) {
        ResDTO response = new ResDTO();
        response.setData(null);
        MimeMessage message = sender.createMimeMessage();
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
            helper.setFrom(request.getFrom());
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
}