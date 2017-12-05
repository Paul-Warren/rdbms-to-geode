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

package com.example.springoneplatform.scs.demo.sink.extractor;


import com.example.springoneplatform.scs.demo.model.etl.ItemPayload;
import com.example.springoneplatform.scs.demo.model.etl.PayloadWrapper;
import com.example.springoneplatform.scs.demo.model.pdx.Item;

/**
 * @author Jeff Cherng
 */
public class ItemExtractor implements PayloadWrapperExtractor<ItemPayload, Item> {

	@Override
	public Item extractData(PayloadWrapper<ItemPayload> payloadWrapper) {
		Item value = null;
		if (payloadWrapper.hasPayload()) {
			value = new Item();
			ItemPayload payload = payloadWrapper.getPayload();
			value.setName(payload.getName());
			value.setDescription(payload.getDescription());
			value.setPrice(payload.getPrice().toString());
		}
		return value;
	}

}
