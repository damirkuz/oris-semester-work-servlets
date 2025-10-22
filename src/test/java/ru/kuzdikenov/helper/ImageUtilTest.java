package ru.kuzdikenov.helper;

import org.junit.Assert;
import org.junit.Test;

public class ImageUtilTest {

    @Test
    public void testGetPathAfterWebapp() {
        String FILE_PREFIX = "/ORIS/semester-work-servlets-damirkuz/src/main/webapp/savedImages";

        String expected = "/savedImages";
        String actual = ImageUtil.getPathAfterWebapp(FILE_PREFIX);
        Assert.assertEquals(expected, actual);
    }
}
