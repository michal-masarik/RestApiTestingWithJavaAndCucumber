package name.lattuada.trading.tests.api;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import name.lattuada.trading.tests.ConfigReader;

/**
 * This Utility class provides basic REST API support for {@link ApiFacade}
 * 
 */
public final class RestUtility {

	private final HttpHeaders headers;
	private final String baseUrl;
	private final RestTemplate restTemplate;

	public RestUtility() {
		this.baseUrl = ConfigReader.getInstance().getProperty("baseUrl");
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		restTemplate = new RestTemplate();
	}

	/**
	 * this method provides GET request for REST API
	 */
	public <T> T get(String uri, Class<T> valueType) {
		return restTemplate.getForObject(getUrl(uri), valueType);
	}

	/**
	 * this method provides POST request for REST API
	 */
	public <T, B> T post(String uri, B body, Class<T> bodyType) {
		HttpEntity<B> httpEntity = new HttpEntity<>(body, headers);
		return restTemplate.postForObject(getUrl(uri), httpEntity, bodyType);
	}

	private String getUrl(String uri) {
		return baseUrl + (uri.startsWith("/") ? "" : '/') + uri;
	}
}
