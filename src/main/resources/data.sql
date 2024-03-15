INSERT INTO wallet (wallet_id, balance)
VALUES (1, 1000.00),
       (2, 2000.00),
       (3, 3000.00);


INSERT INTO user (email, password, name, bank_account_number, wallet_id)
VALUES ('user1@example.com', 'pass123', 'User One', 'FR76123459876543210987654321', 1),
       ('user2@example.com', 'pass456', 'User Two', 'FR76234567898765432109876543', 2),
       ('user3@example.com', 'pass789', 'User Three', 'FR78345678909876543212345678', 3);


INSERT INTO transaction (transaction_id, amount, commission, date, time, description, sender_user_id, receiver_user_id)
VALUES (1, 100.00, 5, '2022-03-01', '10:00:00', 'Transaction 1', 1, 2),
       (2, 200.00, 10, '2022-03-02', '11:00:00', 'Transaction 2', 1, 3),
       (3, 300.00, 15, '2022-03-03', '12:00:00', 'Transaction 3', 2, 1),
       (4, 400.00, 20, '2022-03-04', '13:00:00', 'Transaction 4', 2, 3),
       (5, 500.00, 25, '2022-03-05', '14:00:00', 'Transaction 5', 3, 1);

INSERT INTO connection (owner_user_id, receiver_user_id)
VALUES (1, 2),
       (1, 3),
       (2, 1),
       (2, 3),
       (3, 1);