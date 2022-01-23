package by.latushko.anyqueries.validator.impl;

import org.testng.annotations.DataProvider;

import java.util.HashMap;
import java.util.Map;

import static by.latushko.anyqueries.controller.command.identity.RequestParameter.*;

public class FormValidatorDataProvider {
    @DataProvider(name = "categoryCorrect")
    public Object[][] categoryCorrect(){
        Map<String, String[]> correctData = new HashMap<>();
        correctData.put(NAME, new String[]{"History"});
        correctData.put(COLOR, new String[]{"#fff"});
        return new Object[][] {{correctData}};
    }

    @DataProvider(name = "categoryWrong")
    public Object[][] categoryWrong(){
        Map<String, String[]> wrongData = new HashMap<>();
        wrongData.put(NAME, new String[]{"&*&*SADasdads"});
        wrongData.put(COLOR, new String[]{"rgb(0,0,0)"});
        return new Object[][] {{wrongData}};
    }

    @DataProvider(name = "changePasswordCorrect")
    public Object[][] changePasswordCorrect(){
        Map<String, String[]> correctData = new HashMap<>();
        correctData.put(PASSWORD_OLD, new String[]{"A@dmin123321"});
        correctData.put(PASSWORD_NEW, new String[]{"Qw@erty123321"});
        correctData.put(PASSWORD_NEW_REPEAT, new String[]{"Qw@erty123321"});
        return new Object[][] {{correctData}};
    }

    @DataProvider(name = "changePasswordWrong")
    public Object[][] changePasswordWrong(){
        Map<String, String[]> correctData = new HashMap<>();
        correctData.put(PASSWORD_OLD, new String[]{"Qw@erty123321"});
        correctData.put(PASSWORD_NEW, new String[]{"zzzzzzzzzzz"});
        correctData.put(PASSWORD_NEW_REPEAT, new String[]{"^&*&*(sdadas"});
        return new Object[][] {{correctData}};
    }
}
