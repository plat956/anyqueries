package by.latushko.anyqueries.util.http;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public final class QueryParameterHelper {
    private static final Logger logger = LogManager.getLogger();

    private QueryParameterHelper() {
    }

    public static String addParameter(String url, String key, String value) {
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            List<NameValuePair> queryParameters = uriBuilder.getQueryParams();
            for (Iterator<NameValuePair> queryParameterItr = queryParameters.iterator(); queryParameterItr.hasNext();) {
                NameValuePair queryParameter = queryParameterItr.next();
                if (queryParameter.getName().equals(key)) {
                    queryParameterItr.remove();
                }
            }
            uriBuilder.setParameters(queryParameters);
            uriBuilder.addParameter(key, value);
            logger.debug("Parameter {} has been added to url", key);
            return uriBuilder.build().toString();
        } catch (URISyntaxException e) {
            logger.error("Wrong url syntax: {}", url, e);
            return url;
        }
    }

    public static String removeParameter(String url, String... keysArray) {
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            List<NameValuePair> queryParameters = uriBuilder.getQueryParams();
            List<String> keys = Arrays.stream(keysArray).toList();
            queryParameters.removeIf(param -> keys.contains(param.getName()));
            uriBuilder.setParameters(queryParameters);
            logger.debug("Parameters {} have been removed from url", keysArray);
            return uriBuilder.build().toString();
        } catch (URISyntaxException e) {
            logger.error("Wrong url syntax: {}", url, e);
            return url;
        }
    }
}
