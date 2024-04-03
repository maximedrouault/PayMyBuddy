INSERT INTO wallet (wallet_id, balance)
VALUES  (1, 1200.00),
        (2, 2300.50),
        (3, 3150.75),
        (4, 2850.00),
        (5, 1950.25);


INSERT INTO user (email, password, name, bank_account_number, wallet_id)
VALUES  ('user1@example.com', '$2a$12$b2dB2W39ThUNSK3DBrRoTe9mJMkopItV4SuUqxK37fYfR9HWlZeO2', 'User One', 'FR12345678901234567890123456', 1),
        ('user2@example.com', '$2a$12$tDbK9W9lCz6AWqYfuqF/J.9746URT29DIDYWanh88QcLH0yZDbRtW', 'User Two', 'FR22345678901234567890123456', 2),
        ('user3@example.com', '$2a$12$B0mbnwYnAXoZkluOO3g3uuphi1Ardbqh.tEbfLnGA4AnsddeDoGv6', 'User Three', 'FR32345678901234567890123456', 3),
        ('user4@example.com', '$2a$12$JRgrr20kht5htfGJnMbD2uKU3WSE8X.MAX/IHL/Cu7g8mV3LsiAfm', 'User Four', 'FR42345678901234567890123456', 4),
        ('user5@example.com', '$2a$12$Z2V3QmSzgr3GpnGgaJzaEuOdHKN13ios.uQ1GtxlewoftyNQ0AFk2', 'User Five', 'FR52345678901234567890123456', 5);


INSERT INTO transaction (transaction_amount, commission_amount, date, time, description, sender_user_id, receiver_user_id)
VALUES  (100.25, 5.01, '2023-03-25', '10:00:00', 'Payment for services', 1, 2),
        (150.75, 7.54, '2023-03-26', '11:00:00', 'Grocery shopping', 1, 3),
        (200.50, 10.03, '2023-03-27', '12:00:00', 'Online Course', 2, 1),
        (250.40, 12.52, '2023-03-28', '13:00:00', 'Book Purchase', 2, 3),
        (75.65, 3.78, '2023-03-29', '14:00:00', 'Music Subscription', 2, 4),
        (50.30, 2.52, '2023-03-30', '15:00:00', 'Cafe Visit', 3, 1),
        (125.90, 6.30, '2023-03-31', '16:00:00', 'Utility Bill', 3, 2),
        (175.45, 8.77, '2023-04-01', '17:00:00', 'Concert Ticket', 3, 4),
        (90.10, 4.50, '2023-04-02', '18:00:00', 'Book Club Membership', 3, 5),
        (60.25, 3.01, '2023-04-03', '19:00:00', 'Charity Donation', 3, 1),
        (200.15, 10.01, '2023-04-04', '20:00:00', 'Tech Upgrade', 3, 5),
        (85.55, 4.28, '2023-04-05', '21:00:00', 'Art Supplies', 3, 2),
        (95.00, 4.75, '2023-04-06', '22:00:00', 'Online Workshop', 3, 4),
        (300.99, 15.05, '2023-04-04', '09:00:00', 'Tech Gadget', 4, 2),
        (350.80, 17.54, '2023-04-05', '10:30:00', 'Anniversary Gift', 4, 3),
        (400.40, 20.02, '2023-04-06', '11:30:00', 'Flight Ticket', 5, 1),
        (45.35, 2.27, '2023-04-07', '12:30:00', 'App Subscription', 5, 2),
        (80.75, 4.04, '2023-04-08', '13:30:00', 'Gym Membership', 5, 3),
        (130.60, 6.53, '2023-04-09', '14:30:00', 'Language Course', 5, 4);


INSERT INTO connection (owner_user_id, receiver_user_id)
VALUES  (1, 2),
        (1, 3),
        (2, 3),
        (2, 4),
        (3, 4),
        (3, 5),
        (4, 5),
        (4, 1),
        (5, 1),
        (5, 2);