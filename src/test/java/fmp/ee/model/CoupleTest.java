package fmp.ee.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by pedrozaf on 6/5/15.
 */
public class CoupleTest {

    @Test
    public void test_toChar() throws Exception {
        assertEquals('A', Couple.toChar(1));
        assertEquals('B', Couple.toChar(2));
        assertEquals('M', Couple.toChar(13));
        assertEquals('Y', Couple.toChar(25));
        assertEquals('Z', Couple.toChar(26));
    }

    @Test
    public void test_toChar_fails() throws Exception {
        toChar_fails(' ');
        toChar_fails('a');
        toChar_fails('z');
        toChar_fails('4');
        toChar_fails('%');
    }

    private void toChar_fails(char ch) {
        try {
            Couple.toChar(ch);
            fail(String.format("expected IAE not thrown for value:%s", ch));
        }
        catch (IllegalArgumentException e) {
            // TODO: handle exception
        }
    }
}