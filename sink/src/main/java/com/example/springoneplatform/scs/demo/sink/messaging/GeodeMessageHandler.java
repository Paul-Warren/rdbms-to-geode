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

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

/**
 * @author Jeff Cherng
 */
public class GeodeMessageHandler {
	private final ClientCache clientCache;

	public GeodeMessageHandler(ClientCache clientCache) {
		super();
		this.clientCache = clientCache;
	}

	public void handleMessage(Message<GeodeDataWrapper> message) {
		MessageHeaders headers = message.getHeaders();
		GeodeDataWrapper payload = message.getPayload();
		String regionName = (String) headers.get("srcGroup");
		Region<Object, Object> region = clientCache.getRegion(regionName);
		region.putAll(payload.getDataMapForPut());
		region.removeAll(payload.getKeySetForRemove());
	}
}
