package ru.kuzdikenov.helper;

import org.junit.Assert;
import org.junit.Test;

public class LoginPasswordUtilTest {

    @Test
    public void testHashPassword() {
        String password = "qwerty007";
        String hash = "1B435C76EBB69FC6130027F05FA139FF";
        String actual = LoginPasswordUtil.encrypt(password);
        Assert.assertEquals(hash, actual);
    }

    @Test
    public void testValidPassword() {
        String validPassword = "qwerty007";
        Assert.assertTrue(LoginPasswordUtil.isValidPassword(validPassword));
    }

    @Test
    public void testInvalidPasswordWithCyrillicAlphabet() {
        String validPassword = "антон";
        Assert.assertFalse(LoginPasswordUtil.isValidPassword(validPassword));
    }

    @Test
    public void testInvalidPasswordWithProhibitSymbols() {
        String validPassword = "qwertyß¨§˜";
        Assert.assertFalse(LoginPasswordUtil.isValidPassword(validPassword));
    }
}
