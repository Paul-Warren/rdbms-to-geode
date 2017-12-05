/*
 * Copyright 2017 the original author or authors.
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

package com.example.springoneplatform.scs.demo.sink.messaging;

import com.example.springoneplatform.scs.demo.model.etl.PayloadWrapper;
import com.example.springoneplatform.scs.demo.sink.extractor.PayloadWrapperExtractor;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.annotation.Aggregator;
import org.springframework.integration.annotation.CorrelationStrategy;
import org.springframework.integration.annotation.ReleaseStrategy;
import org.springframework.messaging.Message;

import java.util.List;

/**
 * @author Jeff Cherng
 */
public class GeodeMessageAggregator {

	private final ApplicationContext context;
	private final int groupCount;
	private final int batchSize;

	public GeodeMessageAggregator(ApplicationContext context, int groupCount, int batchSize) {
		this.context = context;
		this.groupCount = groupCount;
		this.batchSize = batchSize;
	}

	@Aggregator
	public GeodeDataWrapper output(List<Message<PayloadWrapper>> messages) {
		GeodeDataWrapper geodeDataWrapper = new GeodeDataWrapper();
		for (Message<PayloadWrapper> message : messages) {
			String geodeRegionName = (String) message.getHeaders().get("srcGroup");
			Object geodeKey = message.getHeaders().get("srcKey");
			PayloadWrapper<?> payloadWrapper = message.getPayload();
			PayloadWrapperExtractor extractor =
					(PayloadWrapperExtractor) context.getBean(geodeRegionName + "Extractor");
			if(payloadWrapper.hasPayload()){
				geodeDataWrapper.getKeySetForRemove().remove(geodeKey);
				geodeDataWrapper.getDataMapForPut().put(geodeKey, extractor.extractData(payloadWrapper));
			} else {
				geodeDataWrapper.getDataMapForPut().remove(geodeKey);
				geodeDataWrapper.getKeySetForRemove().add(geodeKey);
			}
		}
		return geodeDataWrapper;
	}

	@CorrelationStrategy
	public String correlation(Message<?> message) {
		int hashCode = message.getHeaders().get("srcKey").hashCode();
		int divisor = groupCount;
		return "[" + (String) message.getHeaders().get("srcGroup") + "]" 
				+ ",[" + Math.floorMod(hashCode, divisor) + "]";
	}

	@ReleaseStrategy
	public boolean canMessagesBeReleased(List<Message<?>> messages) {
		return messages.size() >= batchSize;
	}

}
