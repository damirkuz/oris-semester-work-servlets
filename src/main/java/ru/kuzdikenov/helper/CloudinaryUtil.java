package ru.kuzdikenov.helper;

import com.cloudinary.Cloudinary;
import ru.kuzdikenov.app.DefaultSettings;

import java.util.HashMap;
import java.util.Map;

public class CloudinaryUtil {

    private static Cloudinary cloudinary;

    public static Cloudinary getInstance() {
        if (cloudinary == null) {
            Map<String, String> config = new HashMap<>();
            config.put("cloud_name", DefaultSettings.CLOUDINARY_CLOUD_NAME);
            config.put("api_key", DefaultSettings.CLOUDINARY_API_KEY);
            config.put("api_secret", DefaultSettings.CLOUDINARY_API_SECRET);
            cloudinary = new Cloudinary(config);
        }

        return cloudinary;
    }
}
