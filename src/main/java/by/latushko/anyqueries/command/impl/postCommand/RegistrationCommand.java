package by.latushko.anyqueries.command.impl.postCommand;

import by.latushko.anyqueries.command.*;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.model.entity.UserHash;
import by.latushko.anyqueries.exception.MailSenderException;
import by.latushko.anyqueries.util.encryption.PasswordEncoder;
import by.latushko.anyqueries.util.encryption.impl.BCryptPasswordEncoder;
import by.latushko.anyqueries.util.mail.MailSender;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.StringWriter;
import java.time.LocalDateTime;

import static by.latushko.anyqueries.command.ResponseMessage.Type.INFO;

public class RegistrationCommand implements Command {
    @Override
    public ResponseParameter execute(HttpServletRequest request) {
        String firstName = request.getParameter(RequestParameter.FIRST_NAME);
        String lastName = request.getParameter(RequestParameter.LAST_NAME);
        String middleName = request.getParameter(RequestParameter.MIDDLE_NAME);
        String email = request.getParameter(RequestParameter.EMAIL);
        String login = request.getParameter(RequestParameter.LOGIN);
        String password = request.getParameter(RequestParameter.PASSWORD);
        String passwordConfirmed = request.getParameter(RequestParameter.PASSWORD_CONFIRMED);

        //validation

        PasswordEncoder encoder = new BCryptPasswordEncoder();

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setMiddleName(middleName);
        user.setEmail(email);
        user.setLogin(login);
        user.setPassword(encoder.encode(password));
        user.setRole(User.Role.ROLE_USER);
        user.setStatus(User.Status.INACTIVE);

        UserHash userHash = new UserHash();
        userHash.setUser(user);
        userHash.setHash(encoder.encode(email + "абырвалг" + login));
        userHash.setExpires(LocalDateTime.now().plusHours(24));

        MailSender sender = MailSender.getInstance();
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();

        Template t = ve.getTemplate("/template/mail/confirmation.vm");
        VelocityContext context = new VelocityContext();
        context.put("title", "Welcome to ANY-QUERIES.BY"); //todo: read product.domain from app.props file

        String fio = lastName.substring(0, 1).toUpperCase() + lastName.substring(1) + " "
                + firstName.substring(0, 1).toUpperCase() + ". "
                + middleName.substring(0, 1).toUpperCase();

        context.put("text", "Dear " + fio + ", you've sent a registration request. Please, press the button below to activate your account");
        context.put("buttonText", "Activate");
        String server = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        context.put("buttonLink", server + "/controller?command=confirmation&hash=" + userHash.getHash());

        StringWriter writer = new StringWriter();
        t.merge(context, writer);
        String body = writer.toString();

        try {
            sender.send(email, "Account activation", body);
        } catch (MailSenderException e) {
            e.printStackTrace();
        }

        String text = "Ссылка для подтверждения учетной записи отправлена на " + user.getEmail();
        String notice = "Обратите внимание, что ссылка действительна в течении 24 часов. По истечении данного срока Вам придется повторить процедуру регистрации";
        request.setAttribute(RequestAttribute.MESSAGE, new ResponseMessage(INFO, text, notice));
        return new ResponseParameter(PagePath.REGISTRATION_PAGE, ResponseParameter.RoutingType.FORWARD);
    }
}
