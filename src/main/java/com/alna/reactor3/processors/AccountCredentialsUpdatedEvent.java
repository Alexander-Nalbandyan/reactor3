package com.alna.reactor3.processors;

public class AccountCredentialsUpdatedEvent extends EventBase {

    private String accountEmail;

    public AccountCredentialsUpdatedEvent(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    @Override
    public String toString() {
        return "AccountCredentialsUpdatedEvent{" +
                "accountEmail='" + accountEmail + '\'' +
                "} " + super.toString();
    }
}
