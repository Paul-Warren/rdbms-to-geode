-- operational events
-- remove item2 (pen) from order1 for customer1
DELETE FROM ORDER_ITEM
WHERE CUSTOMER_ORDER_ID = 'order1' AND ITEM_ID = 'item2';

-- delete entire order2 for customer1
DELETE FROM ORDER_ITEM
WHERE CUSTOMER_ORDER_ID = 'order2';

DELETE FROM CUSTOMER_ORDER
WHERE ID = 'order2';

COMMIT;
