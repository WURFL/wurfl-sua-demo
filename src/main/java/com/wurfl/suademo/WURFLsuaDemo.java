package com.wurfl.suademo;

import com.google.gson.Gson;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.scientiamobile.wurfl.core.Device;
import com.scientiamobile.wurfl.core.GeneralWURFLEngine;
import com.scientiamobile.wurfl.core.WURFLEngine;
import com.scientiamobile.wurfl.core.request.DefaultWURFLRequest;
import com.scientiamobile.wurfl.core.request.WURFLRequest;


public class WURFLsuaDemo {
	public static void main(String[] args) {
		// Frozen User-Agent
		String ua = "Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.0.0 Mobile Safari/537.36";
		// Structured UA (sua) in an escaped JSON format
		String sua_json = "{\"browsers\":[{\"brand\":\"Mozilla\",\"version\":[\"5\",\"0\"]},{\"brand\":\"AppleWebKit\",\"version\":[\"537\",\"36\"]},{\"brand\":\"Version\",\"version\":[\"4\",\"0\"]},{\"brand\":\"Google Chrome\",\"version\":[\"106\",\"0\",\"5249\",\"126\"]},{\"brand\":\"Mobile Safari\",\"version\":[\"537\",\"36\"]}],\"platform\":{\"brand\":\"Android\",\"version\":[\"11\",\"0\"]},\"mobile\":1,\"model\":\"Infinix X6512\"}";

		Gson gson = new Gson();
		// Parse JSON into BrowserInfo complex Java object
		BrowserInfo browserInfo = gson.fromJson(sua_json, BrowserInfo.class);

		// Let's build the brands string from the object data
		StringBuilder brandsBuilder = new StringBuilder();
		if (browserInfo != null && browserInfo.getBrowsers() != null) {
			List<Browser> browsers = browserInfo.getBrowsers();
			for (Browser browser : browsers) {
				if (browser != null) {
					String brand = browser.getBrand();
					List<String> version = browser.getVersion();
					if (brand != null && version != null) {
						brandsBuilder.append("\"").append(brand).append("\";v=\"");
						for (int j = 0; j < version.size(); j++) {
							brandsBuilder.append(version.get(j));
							if (j < version.size() - 1) {
								brandsBuilder.append(".");
							}
						}
						brandsBuilder.append("\",");
					}
				}
			}
			// Remove trailing comma
			if (brandsBuilder.length() > 0) {
				brandsBuilder.setLength(brandsBuilder.length() - 1);
			}
		}

		String brands = brandsBuilder.toString();

		String platform = "";
		String platformVersion = "";
		if (browserInfo != null && browserInfo.getPlatform() != null) {
			Platform platformObj = browserInfo.getPlatform();
			if (platformObj.getBrand() != null) {
				platform = "\"" + platformObj.getBrand() + "\"";
			}
			if (platformObj.getVersion() != null) {
				platformVersion = String.join(".", platformObj.getVersion());
			}
		}

		String model = browserInfo != null && browserInfo.getModel() != null ? browserInfo.getModel() : "";
		String mobile = browserInfo != null && browserInfo.getMobile().contains("1") ? "?1" : "?0";

		// Now we put all the extracted data into a Map to feed the WURFL API
		Map<String, String> headers = new HashMap<>();
		headers.put("User-Agent", ua);
		headers.put("Sec-Ch-Ua", brands);
		headers.put("Sec-Ch-Ua-Full-Version-List", brands);
		headers.put("Sec-Ch-Ua-Platform", platform);
		headers.put("Sec-Ch-Ua-Platform-Version", platformVersion);
		headers.put("Sec-Ch-Ua-Model", model);
		headers.put("Sec-Ch-Ua-Mobile", mobile);


		// Perform the device detection using the WURFL API now
		System.out.println("Running WURFL Java API " + WURFLEngine.API_VERSION);
		String wurflRootPath = "wurfl.xml";
		WURFLEngine engine = new GeneralWURFLEngine(wurflRootPath);
		engine.load();
		WURFLRequest req = new DefaultWURFLRequest(headers);
		Device device = engine.getDeviceForRequest(req);
		System.out.println("Device id: " + device.getId());
		System.out.println("Header Quality: " + engine.headerQuality(req));
		System.out.println("Complete Device Name: " + device.getVirtualCapability("complete_device_name"));
		System.out.println("Advertised Device OS: " + device.getVirtualCapability("advertised_device_os"));
		System.out.println("Advertised Device OS Version: " + device.getVirtualCapability("advertised_device_os_version"));
		System.out.println("Advertised Browser: " + device.getVirtualCapability("advertised_browser"));
		System.out.println("Advertised Browser Version: " + device.getVirtualCapability("advertised_browser_version"));
	}
}