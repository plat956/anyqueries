//package by.latushko.anyqueries;
//
//import by.latushko.anyqueries.dao.UserDao;
//import by.latushko.anyqueries.dao.impl.UserDaoImpl;
//import by.latushko.anyqueries.entity.User;
//import by.latushko.anyqueries.exception.DaoException;
//import by.latushko.anyqueries.exception.MailSenderException;
//import by.latushko.anyqueries.util.encryption.PasswordEncoder;
//import by.latushko.anyqueries.util.encryption.impl.BCryptPasswordEncoder;
//import by.latushko.anyqueries.util.mail.MailSender;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.apache.velocity.Template;
//import org.apache.velocity.VelocityContext;
//import org.apache.velocity.app.VelocityEngine;
//import org.apache.velocity.runtime.RuntimeConstants;
//import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
//
//import java.io.StringWriter;
//import java.util.List;
//
//public class Main {
//    private static final Logger logger = LogManager.getLogger();
//    public static void main(String[] args) throws DaoException, MailSenderException {
//        logger.info("qwe");
//        //######## BCRYPT PASSWORD ENCRYPTOR ########
//        String password = "qwerty123321";
//        PasswordEncoder encoder = new BCryptPasswordEncoder();
//        String hashed = encoder.encode(password);
//
//        if (encoder.check(password, hashed)) {
//            System.out.println("It matches");
//        } else {
//            System.out.println("It does not match");
//        }
//
//
//        //############## SMTP MAIL SENDER ########
//        VelocityEngine ve = new VelocityEngine();
//        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
//        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
//        ve.init();
//
//        Template t = ve.getTemplate("/template/confirmation.vm");
//        VelocityContext context = new VelocityContext();
//        context.put("title", "Welcome to ANY-QUERIES.BY"); //todo: read product.domain from app.props file
//        context.put("text", "Press the button below to activate your account");
//        context.put("buttonText", "Activate");
//        context.put("buttonLink", "http://any-queries.by/activate?hash=" + hashed);
//
//        StringWriter writer = new StringWriter();
//        t.merge(context, writer);
//        String body = writer.toString();
//
//        MailSender sender = MailSender.getInstance();
//        //sender.send("plat956@gmail.com", "Account activation", body); //todo: remove that comment to call the method :)
//
//
//        //############## DAO ########
//        UserDao userDao = UserDaoImpl.getInstance();
//        List<User> userList = userDao.findAll();
//        System.out.println(userList);
//    }
//}
