package by.latushko.anyqueries.service.impl;

import by.latushko.anyqueries.controller.command.identity.RequestParameter;
import by.latushko.anyqueries.exception.MailSenderException;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.model.entity.UserHash;
import by.latushko.anyqueries.service.EmailService;
import by.latushko.anyqueries.util.i18n.MessageManager;
import by.latushko.anyqueries.util.mail.MailSender;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.StringWriter;

import static by.latushko.anyqueries.controller.command.identity.PageUrl.ACTIVATE_URL;
import static by.latushko.anyqueries.util.AppProperty.APP_HOST;
import static by.latushko.anyqueries.util.AppProperty.APP_NAME;
import static by.latushko.anyqueries.util.i18n.MessageKey.*;

public class EmailServiceImpl implements EmailService {
    private static final String VELOCITY_PROPERTIES_LOADER_CLASS = "classpath.resource.loader.class";
    private static final String VELOCITY_RESOURCES_PATH = "classpath";
    private static final String VELOCITY_ACTIVATION_TEMPLATE_PATH = "/template/mail/activation.vm";
    private static final String TEMPLATE_TITLE_PARAMETER = "title";
    private static final String TEMPLATE_TEXT_PARAMETER = "text";
    private static final String TEMPLATE_BUTTON_TEXT_PARAMETER = "buttonText";
    private static final String TEMPLATE_BUTTON_LINK_PARAMETER = "buttonLink";
    private static final String TEMPLATE_NOT_REPLY_NOTICE = "notReply";
    private static final String QUERY_PARAMETERS_DELIMITER = "&";
    private static final String QUERY_PARAMETER_EQUAL_SIGN = "=";
    private final MessageManager manager;

    public EmailServiceImpl(MessageManager manager) {
        this.manager = manager;
    }

    @Override
    public void sendActivationEmail(User user, UserHash hash) throws MailSenderException {
        String body = compileActivationLetter(user, hash, manager);
        MailSender sender = MailSender.getInstance();
        sender.send(user.getEmail(), manager.getMessage(LABEL_ACCOUNT_ACTIVATION), body);
    }

    private String compileActivationLetter(User user, UserHash userHash, MessageManager manager) {
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, VELOCITY_RESOURCES_PATH);
        ve.setProperty(VELOCITY_PROPERTIES_LOADER_CLASS, ClasspathResourceLoader.class.getName());
        ve.init();
        Template template = ve.getTemplate(VELOCITY_ACTIVATION_TEMPLATE_PATH);
        VelocityContext context = new VelocityContext();
        context.put(TEMPLATE_TITLE_PARAMETER, manager.getMessage(MESSAGE_ACTIVATION_EMAIL_GREETING, APP_NAME));
        context.put(TEMPLATE_TEXT_PARAMETER, manager.getMessage(MESSAGE_ACTIVATION_EMAIL_TEXT, user.getFio()));
        context.put(TEMPLATE_BUTTON_TEXT_PARAMETER, manager.getMessage(LABEL_ACTIVATION_BUTTON));
        context.put(TEMPLATE_BUTTON_LINK_PARAMETER, APP_HOST + ACTIVATE_URL + QUERY_PARAMETERS_DELIMITER +
                RequestParameter.HASH + QUERY_PARAMETER_EQUAL_SIGN + userHash.getHash());
        context.put(TEMPLATE_NOT_REPLY_NOTICE, manager.getMessage(MESSAGE_ACTIVATION_NOT_REPLY));
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        return writer.toString();
    }
}
