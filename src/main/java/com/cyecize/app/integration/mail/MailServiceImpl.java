package com.cyecize.app.integration.mail;

import com.cyecize.app.api.mail.EmailTemplate;
import com.cyecize.app.api.mail.MailService;
import com.cyecize.app.api.translate.TranslateService;
import com.cyecize.app.error.ApiException;
import com.cyecize.summer.areas.template.services.TemplateRenderingService;
import com.cyecize.summer.common.annotations.Configuration;
import com.cyecize.summer.common.annotations.Service;
import com.cyecize.summer.common.models.Model;
import com.steadystate.css.parser.CSSOMParser;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    @Configuration("mail.from")
    private final String mailFrom;

    private final Session session;

    private final TemplateRenderingService templateRenderingService;

    private final TranslateService translateService;

    @Configuration("website.name")
    private final String websiteName;

    @Configuration("mail.enabled")
    private final boolean mailsEnabled;

    @Override
    public <T> void sendEmail(EmailTemplate<T> template, T viewModel, List<String> receivers) {
        if (!this.mailsEnabled) {
            return;
        }
        final Model model = new Model();
        model.addAttribute("model", viewModel);

        final String commaSeparatedMails = String.join(", ", receivers);
        try {
            final String html = this.convertIntoInline(
                    this.templateRenderingService.render(template.getTemplateName(), model)
            );

            final Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(this.mailFrom));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(commaSeparatedMails)
            );
            message.setSubject(
                    this.translateService.getValue(template.getSubject()) + " - " + this.websiteName
            );

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

    private String convertIntoInline(String originalHtml) throws IOException {
        final Document document = Jsoup.parse(originalHtml);
        final Elements styleTags = document.select("style");
        final StringBuilder stylesBuilder = new StringBuilder();
        for (Element element : styleTags) {
            stylesBuilder.append(element.data());
            stylesBuilder.append(System.lineSeparator());
        }

        final CSSOMParser parser = new CSSOMParser();
        final CSSStyleSheet stylesheet = parser.parseStyleSheet(
                new InputSource(new InputStreamReader(new ByteArrayInputStream(
                        stylesBuilder.toString().getBytes(StandardCharsets.UTF_8)
                ))),
                null,
                null
        );

        final CSSRuleList rules = stylesheet.getCssRules();
        final Map<Element, Map<String, String>> elementStyles = new HashMap<>();

        /*
         * For each rule in the style sheet, find all HTML elements that match
         * based on its selector and store the style attributes in the map with
         * the selected element as the key.
         */
        for (int i = 0; i < rules.getLength(); i++) {
            final CSSRule rule = rules.item(i);
            if (rule instanceof CSSStyleRule) {
                final CSSStyleRule styleRule = (CSSStyleRule) rule;
                final String selector = styleRule.getSelectorText();

                // Ignore pseudo classes, as JSoup's selector cannot handle
                // them.
                if (!selector.contains(":")) {
                    final Elements selectedElements = document.select(selector);
                    for (final Element selected : selectedElements) {
                        if (!elementStyles.containsKey(selected)) {
                            elementStyles.put(selected, new LinkedHashMap<>());
                        }

                        final CSSStyleDeclaration styleDeclaration = styleRule.getStyle();

                        for (int j = 0; j < styleDeclaration.getLength(); j++) {
                            final String propertyName = styleDeclaration.item(j);
                            final String propertyValue = styleDeclaration.getPropertyValue(
                                    propertyName);
                            final Map<String, String> elementStyle = elementStyles.get(selected);
                            elementStyle.put(propertyName, propertyValue);
                        }

                    }
                }
            }
        }

        /*
         * Apply the style attributes to each element and remove the "class"
         * attribute.
         */
        for (final Map.Entry<Element, Map<String, String>> elementEntry : elementStyles.entrySet()) {
            final Element element = elementEntry.getKey();
            final StringBuilder builder = new StringBuilder();
            for (final Map.Entry<String, String> styleEntry : elementEntry.getValue().entrySet()) {
                builder.append(styleEntry.getKey()).append(":").append(styleEntry.getValue())
                        .append(";");
            }
            builder.append(element.attr("style"));
            element.attr("style", builder.toString());
            element.removeAttr("class");
        }

        return document.html();
    }
}
