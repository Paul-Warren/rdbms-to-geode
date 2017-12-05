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

package com.example.springoneplatform.scs.demo.processor.extractor;


import com.example.springoneplatform.scs.demo.model.etl.CustomerOrderPayload;
import com.example.springoneplatform.scs.demo.model.etl.PayloadWrapper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

/**
 * ResultSetExtractor for extracting customer order data
 *
 * @author Jeff Cherng
 */
public class CustomerOrderResultSetExtractor implements ResultSetExtractor<PayloadWrapper<CustomerOrderPayload>> {

    @Override
    public PayloadWrapper<CustomerOrderPayload> extractData(ResultSet rs) throws SQLException, DataAccessException {
        CustomerOrderPayload result = null;
        while (rs.next()) {
            if (result == null) {
                result = new CustomerOrderPayload();
                result.setId(rs.getString("ID"));
                result.setCustomerId(rs.getString("CUSTOMER_ID"));
                result.setShippingAddress(rs.getString("S_ADDRESS"));
                result.setOrderDate(rs.getDate("ORDER_TS").getTime());
                result.setItemSet(new HashSet<String>());
            }
            result.getItemSet().add(rs.getString("ITEM_ID"));
        }
        return new PayloadWrapper<>(result);
    }
}
