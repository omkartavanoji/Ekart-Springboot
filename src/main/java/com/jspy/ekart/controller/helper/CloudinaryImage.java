package com.jspy.ekart.controller.helper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.Cloudinary;

@Component
public class CloudinaryImage {
	@Value("${cloudinery.cloud}")
	private String cloudName;

	@Value("${cloudinery.secret}")
	private String secretKey;

	@Value("${cloudinery.key}")
	private String apiKey;

	public String uploadFile(MultipartFile file) throws IOException {
		Cloudinary cloudinary = new Cloudinary("cloudinary://" + apiKey + ":" + secretKey + "@" + cloudName);
		Map<String, Object> upload = new HashMap<>();
		upload.put("folder", "Products");
		Map<String, Object> map = cloudinary.uploader().upload(file.getBytes(), upload);
		return (String) map.get("url");
	}
}
