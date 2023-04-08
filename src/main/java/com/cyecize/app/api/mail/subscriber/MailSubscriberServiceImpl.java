package com.cyecize.app.api.mail.subscriber;

import com.cyecize.app.api.mail.MailSubscriberType;
import com.cyecize.ioc.annotations.Service;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailSubscriberServiceImpl implements MailSubscriberService {

    private final MailSubscriberRepository mailSubscriberRepository;

    @Override
    public List<String> getAdminEmails() {
        return this.mailSubscriberRepository.selectEmailWhereTypeEquals(MailSubscriberType.ADMIN);
    }
}
