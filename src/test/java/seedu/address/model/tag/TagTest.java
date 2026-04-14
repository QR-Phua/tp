package seedu.address.model.tag;

import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class TagTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Tag(null));
    }

    @Test
    public void constructor_invalidTagName_throwsIllegalArgumentException() {
        String invalidTagName = "";
        assertThrows(IllegalArgumentException.class, () -> new Tag(invalidTagName));
    }

    @Test
    public void isValidTagName() {
        // null tag name
        assertThrows(NullPointerException.class, () -> Tag.isValidTagName(null));

        // valid tag names
        org.junit.jupiter.api.Assertions.assertTrue(Tag.isValidTagName("abc")); // short
        org.junit.jupiter.api.Assertions.assertTrue(Tag.isValidTagName("12345678901234567890")); // exactly 20 chars

        // invalid tag names
        org.junit.jupiter.api.Assertions.assertFalse(Tag.isValidTagName("123456789012345678901")); // 21 chars
    }

}
