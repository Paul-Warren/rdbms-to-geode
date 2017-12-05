
-- Remove item2 (pen) from order1 for customer1
DELETE FROM ORDER_ITEM
WHERE CUSTOMER_ORDER_ID = 'order1' AND ITEM_ID = 'item2';

COMMIT;
-- Delete entire order2 for customer1
DELETE FROM ORDER_ITEM
WHERE CUSTOMER_ORDER_ID = 'order2';

DELETE FROM CUSTOMER_ORDER
WHERE ID = 'order2';

COMMIT;
-- Add a new item available for sale
INSERT INTO ITEM(ID, NAME, PRICE, DESCRIPTION)
VALUES('item4', 'Stapler', 5.00, 'Red Swingline stapler!');

COMMIT;
-- Add a new order for the new item
INSERT INTO CUSTOMER_ORDER(ID, CUSTOMER_ID, ORDER_TS, S_ADDRESS)
VALUES('order4', 'customer2', CURRENT_TIMESTAMP, '999 New-address Parkway' );

INSERT INTO ORDER_ITEM(CUSTOMER_ORDER_ID, ITEM_ID)
VALUES('order4','item4');

COMMIT;