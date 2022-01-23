package by.latushko.anyqueries.util.i18n;

import org.testng.annotations.Test;

import static by.latushko.anyqueries.util.i18n.MessageKey.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

public class MessageManagerTest {

    @Test(dataProvider = "en", dataProviderClass = MessageManagerDataProvider.class)
    public void testGetEnMessage(String lang) {
        MessageManager manager = MessageManager.getManager(lang);
        String actual = manager.getMessage(MESSAGE_CATEGORY_CREATED);
        String expected = "Category created successfully";
        assertEquals(actual, expected);
    }

    @Test(dataProvider = "en", dataProviderClass = MessageManagerDataProvider.class)
    public void testGetEnMessageFalse(String lang) {
        MessageManager manager = MessageManager.getManager(lang);
        String actual = manager.getMessage(MESSAGE_SUCCESS);
        String expected = "Operation completed qwerty";
        assertNotEquals(actual, expected);
    }

    @Test(dataProvider = "ru", dataProviderClass = MessageManagerDataProvider.class)
    public void testGetRuMessage(String lang) {
        MessageManager manager = MessageManager.getManager(lang);
        String actual = manager.getMessage(LABEL_TELEGRAM_CONTACT);
        String expected = "Связаться с разработчиком";
        assertEquals(actual, expected);
    }

    @Test(dataProvider = "ru", dataProviderClass = MessageManagerDataProvider.class)
    public void testGetRuMessageFalse(String lang) {
        MessageManager manager = MessageManager.getManager(lang);
        String actual = manager.getMessage(LABEL_NO);
        String expected = "Неа";
        assertNotEquals(actual, expected);
    }

    @Test(dataProvider = "be", dataProviderClass = MessageManagerDataProvider.class)
    public void testGetBeMessage(String lang) {
        MessageManager manager = MessageManager.getManager(lang);
        String actual = manager.getMessage(MESSAGE_LANG_CHANGED);
        String expected = "Мова паспяхова зменена";
        assertEquals(actual, expected);
    }

    @Test(dataProvider = "be", dataProviderClass = MessageManagerDataProvider.class)
    public void testGetBeMessageFalse(String lang) {
        MessageManager manager = MessageManager.getManager(lang);
        String actual = manager.getMessage(LABEL_EMAIL_EXISTS);
        String expected = "email існуе 1234";
        assertNotEquals(actual, expected);
    }
}