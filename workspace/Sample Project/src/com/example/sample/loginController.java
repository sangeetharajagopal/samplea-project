package com.example.sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class loginController {

	@RequestMapping("/")
	public String index(HttpServletRequest req, HttpServletResponse resp) {
		return "index";
	}


	@RequestMapping("/welcome")
	public String welcome(HttpServletRequest req, HttpServletResponse resp) {
		return "welcome";
	}

	@RequestMapping("/loginWithGoogle")
	public ModelAndView loginWithGoogle() {
		return new ModelAndView(
				"redirect:https://accounts.google.com/o/oauth2/auth?redirect_uri=http://sample1-dot-omega-palace-135510.appspot.com/callback&client_id=662437971315-t45sdpg1j900sa84ppah7ooobe7m2h6b.apps.googleusercontent.com&response_type=code&approval_prompt=force&scope=email&access_type=online");
	}

	@RequestMapping(value = "/callback")
	public String get_code1(@RequestParam String code, HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		// code for getting authorization_code
		System.out.println("Getting Authorization.");
		String auth_code = code;
		System.out.println(auth_code);

		// code for getting access token

		URL url = new URL("https://www.googleapis.com/oauth2/v3/token?"
				+ "client_id=662437971315-t45sdpg1j900sa84ppah7ooobe7m2h6b.apps.googleusercontent.com"
				+ "&client_secret=9Ms4DAr0y1xiLFWMgFYr1RK3" + "redirect_uri=http://sample1-dot-omega-palace-135510.appspot.com/callback"
				+ "grant_type=authorization_code&" + "code=" + auth_code);
		HttpURLConnection connect = (HttpURLConnection) url.openConnection();
		connect.setRequestMethod("POST");
		connect.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connect.setDoOutput(true);
		BufferedReader in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
		String inputLine;
		String response = "";
		while ((inputLine = in.readLine()) != null) {
			response += inputLine;
		}
		in.close();
		System.out.println(response.toString());
		
		ObjectMapper objMapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<String, Object>();

		map = objMapper.readValue(response, new TypeReference<Map<String, String>>() {
		});
		String access_token = (String) map.get("access_token");
		System.out.println("Access token =" + access_token);
		System.out.println("access token caught");

		URL obj1 = new URL("https://www.googleapis.com/oauth2/v3/userinfo?access_token=" + access_token);
		HttpURLConnection conn = (HttpURLConnection) obj1.openConnection();
		BufferedReader in1 = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine1;
		String responsee = "";
		while ((inputLine1 = in1.readLine()) != null) {
			responsee += inputLine1;
		}
		in1.close();
		System.out.println(responsee.toString());
		
		Map<String, Object> map2 = new HashMap<String, Object>();

		map2 = objMapper.readValue(response, new TypeReference<Map<String, String>>() {
		});
		String userMail1 = (String) map2.get("email");
		String userName1 = (String) map2.get("name");

		System.out.println(userMail1);
		System.out.println(userName1);
		return "welcome";

	}

}
