DELETE FROM CUSTOMER;
DELETE FROM CUSTOMER_ORDER;
DELETE FROM ITEM;
DELETE FROM ORDER_ITEM;

-- customer records
INSERT INTO CUSTOMER(ID, NAME)
VALUES('customer1', 'Brook Foster');

INSERT INTO CUSTOMER(ID, NAME)
VALUES('customer2', 'Katie Blair');

-- item records
INSERT INTO ITEM(ID, NAME, PRICE, DESCRIPTION)
VALUES('item1', 'Pencil', 0.99, 'That is a nice pencil!');

INSERT INTO ITEM(ID, NAME, PRICE, DESCRIPTION)
VALUES('item2', 'Pen', 1.49, 'That is a nice pen!');

INSERT INTO ITEM(ID, NAME, PRICE, DESCRIPTION)
VALUES('item3', 'Paper', 0.10, 'The best paper!');

-- customer order records
-- customer1 with 2 orders
INSERT INTO CUSTOMER_ORDER(ID, CUSTOMER_ID, ORDER_TS, S_ADDRESS)
VALUES('order1', 'customer1', CURRENT_TIMESTAMP, '123 Fake Street' );

INSERT INTO CUSTOMER_ORDER(ID, CUSTOMER_ID, ORDER_TS, S_ADDRESS)
VALUES('order2', 'customer1', CURRENT_TIMESTAMP, '404 Not-found Lane' );

-- customer2 with 1 order
INSERT INTO CUSTOMER_ORDER(ID, CUSTOMER_ID, ORDER_TS, S_ADDRESS)
VALUES('order3', 'customer2', CURRENT_TIMESTAMP, '987 Some-address Road' );

-- order item records
-- order1 with pen and paper
INSERT INTO ORDER_ITEM(CUSTOMER_ORDER_ID, ITEM_ID)
VALUES('order1','item2');

INSERT INTO ORDER_ITEM(CUSTOMER_ORDER_ID, ITEM_ID)
VALUES('order1','item3');

-- order2 with pencil, pen, and paper
INSERT INTO ORDER_ITEM(CUSTOMER_ORDER_ID, ITEM_ID)
VALUES('order2','item1');

INSERT INTO ORDER_ITEM(CUSTOMER_ORDER_ID, ITEM_ID)
VALUES('order2','item2');

INSERT INTO ORDER_ITEM(CUSTOMER_ORDER_ID, ITEM_ID)
VALUES('order2','item3');

-- order3 with pencil and pen
INSERT INTO ORDER_ITEM(CUSTOMER_ORDER_ID, ITEM_ID)
VALUES('order3','item1');

INSERT INTO ORDER_ITEM(CUSTOMER_ORDER_ID, ITEM_ID)
VALUES('order3','item2');

COMMIT;

select * from CUSTOMER
