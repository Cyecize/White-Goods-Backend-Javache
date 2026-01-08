package com.cyecize.app.api.auth;

import com.cyecize.app.api.user.User;
import com.cyecize.app.constants.EntityGraphs;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "auth_tokens")
@Getter
@Setter
@ToString
@NamedEntityGraph(name = EntityGraphs.AUTH_TOKEN_USER_ROLES, attributeNodes = {
        @NamedAttributeNode(value = "user", subgraph = "userRoles")
}, subgraphs = @NamedSubgraph(name = "userRoles", attributeNodes = @NamedAttributeNode("roles")))
public class AuthToken {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private LocalDateTime lastAccessDate;

    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    @ToString.Exclude
    private User user;
}
