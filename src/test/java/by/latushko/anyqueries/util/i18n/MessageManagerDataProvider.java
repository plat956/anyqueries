package by.latushko.anyqueries.util.i18n;

import org.testng.annotations.DataProvider;

import static by.latushko.anyqueries.util.i18n.MessageKey.*;

public class MessageManagerDataProvider {
    @DataProvider(name = "en")
    public Object[][] en(){
        return new Object[][] {{LOCALE_EN}};
    }

    @DataProvider(name = "ru")
    public Object[][] ru(){
        return new Object[][] {{LOCALE_RU}};
    }

    @DataProvider(name = "be")
    public Object[][] be(){
        return new Object[][] {{LOCALE_BE}};
    }
}
