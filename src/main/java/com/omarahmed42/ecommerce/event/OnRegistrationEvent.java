package com.omarahmed42.ecommerce.event;

import org.springframework.context.ApplicationEvent;

import com.omarahmed42.ecommerce.model.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OnRegistrationEvent extends ApplicationEvent {
    private User user;

    public OnRegistrationEvent(User user) {
        super(user);
        this.user = user;
    }
}
