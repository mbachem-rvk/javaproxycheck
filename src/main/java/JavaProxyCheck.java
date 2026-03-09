package de.lassitu;

import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.HttpURLConnection;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;


class JavaProxyCheck {
		public static void main(String args[]) {
				ProxySelector s = ProxySelector.getDefault();

				if (args.length == 0) {
						System.err.println("usage: java -jar JavaProxyCheck.jar http://www.example.com/");
						System.exit(64);
				}

				boolean do_dowload = false;
				for (String a : args) {
						if (a.equals("--download")) {
								do_dowload = true;
						}
				}

				for (String a : args) {
						if (a.startsWith("-")) {
								continue;
						}

						try {
								System.out.printf("%-40.40s %s\n", a, s.select(new URI(a)));
						} catch (URISyntaxException | IllegalArgumentException e) {
								System.out.printf("%-40.40s Invalid URI: %s\n", a, e.getMessage());
						}

						if (do_dowload) {
								var uri = URI.create(a);
								try {
										HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
										conn.setRequestMethod("GET");
										conn.connect();
										int responsecode = conn.getResponseCode();
										System.out.printf("\nResponse Code: %d\n", responsecode);

										InputStream content = (InputStream)conn.getInputStream();
										BufferedReader in	 = new BufferedReader (new InputStreamReader (content));
										String line;

										while ((line = in.readLine()) != null) {
												System.out.println(line);
										}

								} catch (IOException ex) {
										// do exception handling here
								}
						}
				}
		}
}