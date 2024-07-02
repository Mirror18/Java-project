package com.mirror.annotation.service;

import com.mirror.annotation.config.OnSmtpEnvCondition;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

@Component
@Conditional(OnSmtpEnvCondition.class)
public class SmtpMailService extends MailService {
}
