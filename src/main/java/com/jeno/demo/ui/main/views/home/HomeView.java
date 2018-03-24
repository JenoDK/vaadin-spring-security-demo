package com.jeno.demo.ui.main.views.home;

import com.jeno.demo.data.security.CustomUserDetails;
import com.jeno.demo.data.security.SecurityHolder;
import com.jeno.demo.model.Role;
import com.jeno.demo.model.RoleName;
import com.jeno.demo.model.User;
import com.jeno.demo.ui.main.views.state.State;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.PostConstruct;

@UIScope
@SpringView(name = State.StateUrlConstants.HOME)
public class HomeView extends VerticalLayout implements View {

    @Autowired
    private SecurityHolder securityHolder;

    @PostConstruct
    void init() {
        addComponent(new Label(getGreeting()));
    }

    private String getGreeting() {
        User user = securityHolder.getUser();
        boolean isAdmin = user.getRoles().stream()
                .map(Role::getName)
                .anyMatch(roleName -> RoleName.ROLE_ADMIN.equals(roleName));
        return "Greetings " + (isAdmin ? "admin" : "user") + " " + user.getUsername();
    }

}
