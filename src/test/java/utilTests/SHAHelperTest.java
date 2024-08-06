package utilTests;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.patsi.utils.SHAHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.util.AssertionErrors.fail;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class SHAHelperTest {
    @Test
    public void testDigest_SHA256() {
        String input = "hello";
        byte[] expectedDigest = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            expectedDigest = md.digest(input.getBytes(StandardCharsets.UTF_8));
        } catch ( NoSuchAlgorithmException e) {
            fail("SHA-256 algorithm should be available");
        }

        byte[] actualDigest = SHAHelper.digest(input.getBytes(StandardCharsets.UTF_8), "SHA-256");
        assertArrayEquals(expectedDigest, actualDigest);
    }

    @Test
    public void testDigest_InvalidAlgorithm() {
        String input = "input";
        assertThrows(IllegalArgumentException.class, () ->
            SHAHelper.digest(input.getBytes(StandardCharsets.UTF_8), "INVALID_ALGO")
        );
    }

    @Test
    public void testBytesToHex() {
        byte[] input = {0x00, 0x01, 0x02, 0x0A, 0x0F};
        String expectedHex = "0001020a0f";
        String actualHex = SHAHelper.bytesToHex(input);
        assertEquals(expectedHex, actualHex);
    }

    @Test
    public void testDigestAndBytesToHex_SHA256() {
        String input = "hello";
        String expectedHexDigest = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            expectedHexDigest = SHAHelper.bytesToHex(digest);
        } catch (NoSuchAlgorithmException e) {
            fail("SHA-256 algorithm should be available");
        }
        byte[] digest = SHAHelper.digest(input.getBytes(StandardCharsets.UTF_8), "SHA-256");
        String actualHexDigest = SHAHelper.bytesToHex(digest);

        assertEquals(expectedHexDigest, actualHexDigest);
    }
}
