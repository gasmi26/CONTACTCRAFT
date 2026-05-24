package com.scm.validators;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileValidatorTest {

    private FileValidator fileValidator;

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder violationBuilder;

    @BeforeEach
    void setUp() {
        fileValidator = new FileValidator();
    }

    @Test
    void testIsValid_NullFile_ReturnsTrue() {
        boolean result = fileValidator.isValid(null, context);
        assertThat(result).isTrue();
    }

    @Test
    void testIsValid_EmptyFile_ReturnsTrue() {
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file", "empty.jpg", "image/jpeg", new byte[0]);
        boolean result = fileValidator.isValid(emptyFile, context);
        assertThat(result).isTrue();
    }

    @Test
    void testIsValid_FileSizeWithinLimit_ReturnsTrue() {
        // 1MB file — within 2MB limit
        byte[] data = new byte[1024 * 1024];
        MockMultipartFile validFile = new MockMultipartFile(
                "file", "valid.jpg", "image/jpeg", data);

        boolean result = fileValidator.isValid(validFile, context);
        assertThat(result).isTrue();
    }

    @Test
    void testIsValid_FileSizeExceedsLimit_ReturnsFalse() {
        // 3MB file — exceeds 2MB limit
        byte[] data = new byte[1024 * 1024 * 3];
        MockMultipartFile largeFile = new MockMultipartFile(
                "file", "large.jpg", "image/jpeg", data);

        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);
        doNothing().when(context).disableDefaultConstraintViolation();
        when(violationBuilder.addConstraintViolation()).thenReturn(context);

        boolean result = fileValidator.isValid(largeFile, context);
        assertThat(result).isFalse();
        verify(context).buildConstraintViolationWithTemplate("File size should be less than 2MB");
    }

    @Test
    void testIsValid_ExactlyAtLimit_ReturnsTrue() {
        // exactly 2MB — at the boundary, should pass
        byte[] data = new byte[1024 * 1024 * 2];
        MockMultipartFile borderFile = new MockMultipartFile(
                "file", "border.jpg", "image/jpeg", data);

        boolean result = fileValidator.isValid(borderFile, context);
        assertThat(result).isTrue();
    }
}
