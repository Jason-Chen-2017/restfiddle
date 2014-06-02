/*
 * Copyright 2014 Ranjan Kumar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.restfiddle.handler.http;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GetHandler extends GenericHandler {
    Logger logger = LoggerFactory.getLogger(GetHandler.class);

    public void process() throws IOException {

	CloseableHttpClient httpclient = HttpClients.createDefault();
	try {
	    HttpGet httpGet = new HttpGet("http://targethost/");

	    Header[] requestHeaders = httpGet.getAllHeaders();
	    logger.info("request headers length : " + requestHeaders.length);

	    for (Header requestHeader : requestHeaders) {
		logger.info("request header - name : " + requestHeader.getName() + " value : " + requestHeader.getValue());
	    }

	    CloseableHttpResponse httpResponse = httpclient.execute(httpGet);
	    try {
		logger.info("response status : " + httpResponse.getStatusLine());

		HttpEntity responseEntity = httpResponse.getEntity();
		Header[] responseHeaders = httpResponse.getAllHeaders();
		for (Header responseHeader : responseHeaders) {
		    logger.info("response header - name : " + responseHeader.getName() + " value : " + responseHeader.getValue());
		}

		Header contentType = responseEntity.getContentType();
		logger.info("response contentType : " + contentType);

		// logger.info("content : " + EntityUtils.toString(responseEntity));
		EntityUtils.consume(responseEntity);
	    } finally {
		httpResponse.close();
	    }
	} finally {
	    httpclient.close();
	}
    }
}