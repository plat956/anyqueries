package by.latushko.anyqueries.util.http;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;

import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;

public class QueryParameterHelper {
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
            return uriBuilder.build().toString();
        } catch (URISyntaxException e) {
            return url;
        }
    }

    public static String removeParameter(String url, String key) {
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            List<NameValuePair> queryParameters = uriBuilder.getQueryParams();
            queryParameters.removeIf(param -> param.getName().equals(key));
            uriBuilder.setParameters(queryParameters);
            return uriBuilder.build().toString();
        } catch (URISyntaxException e) {
            return url;
        }
    }
}
