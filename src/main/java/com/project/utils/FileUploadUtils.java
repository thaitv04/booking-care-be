package com.project.utils;

import com.project.exceptions.FuncErrorException;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class FileUploadUtils {
    public long MAX_FILE_SIZE = 5 * 1024 * 1024;

    public String IMAGE_PATTERN = "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)";

    public String DATE_FORMAT = "yyyyMMddHHmmss";

    public String FILE_NAME_FORMAT = "%s_%s";

    public boolean isAllowedExtension(final String fileName, final String pattern) {
        final Matcher matcher = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE).matcher(fileName);
        return matcher.matches();
    }

    public void assertAllowedExtension(MultipartFile file, final String pattern) throws FuncErrorException {
        final long size = file.getSize();
        if(size > MAX_FILE_SIZE) {
            throw new FuncErrorException("Max file size is 5MB");
        }

        final String fileName = file.getOriginalFilename();
        final String extension = FilenameUtils.getExtension(fileName);
        if (!isAllowedExtension(fileName, pattern)) {
            throw new FuncErrorException("Only jpg, png, gif, or BMP files are supported");
        }
    }

    public String getFileName(final String fileName) {
        final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        final String date = dateFormat.format(System.currentTimeMillis());
        return String.format(FILE_NAME_FORMAT, fileName, date);
    }
}
