package com.cyecize.app.integration.mail;

import com.cyecize.app.api.mail.EmailTemplate;
import com.cyecize.app.api.mail.MailService;
import com.cyecize.app.error.ApiException;
import com.cyecize.summer.areas.template.services.TemplateRenderingService;
import com.cyecize.summer.common.annotations.Configuration;
import com.cyecize.summer.common.annotations.Service;
import com.cyecize.summer.common.models.Model;
import lombok.RequiredArgsConstructor;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    @Configuration("mail.from")
    private final String mailFrom;

    private final Session session;

    private final TemplateRenderingService templateRenderingService;

    @Override
    public <T> void sendEmail(EmailTemplate<T> template, T viewModel, List<String> receivers) {
        final Model model = new Model();
        model.addAttribute("model", viewModel);

        final String commaSeparatedMails = String.join(", ", receivers);
        try {
            final String html = this.templateRenderingService.render(template.getTemplateName(), model);

            final Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(this.mailFrom));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(commaSeparatedMails));
            message.setSubject(template.getSubject());

            final MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(html, "text/html; charset=utf-8");

            final Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);
            Transport.send(message);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ApiException("Could not send email!");
        }
    }
}
