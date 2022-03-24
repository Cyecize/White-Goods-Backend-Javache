package com.cyecize.app.api.mail;

import java.util.List;

public interface MailService {

    <T> void sendEmail(EmailTemplate<T> template, T viewModel, List<String> receiver);
}
