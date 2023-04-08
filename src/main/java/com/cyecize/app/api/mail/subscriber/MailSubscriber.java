package com.cyecize.app.api.mail.subscriber;

import com.cyecize.app.api.mail.MailSubscriberType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "mail_subscribers")
@Getter
@Setter
@ToString
public class MailSubscriber {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private String email;

    @Enumerated(EnumType.STRING)
    private MailSubscriberType subscriberType;
}
