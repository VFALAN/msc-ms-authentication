insert into tuser(id_user, active, email,
                  last_name, middle_name, name,
                  user_name, profile)
values (1, b'1', 'testauthentication@mail.com', 'test-lastname',
        'test-middleName', 'testName', 'testUser', 1),
       (2, b'0', 'testDisabeld@mail.com', 'last name disabled',
        'middle_name disabled', 'disabledUser', 'disabledUser', 1),
       (3, b'1', 'nohistory@mail.com', 'last name nohistory',
        'middle_name nohistory', 'nohistory', 'nohistory', 1),
       (4, b'1', 'passwordExpired@mail.com', 'last name passwordExpired',
        'middle_name passwordExpired', 'passwordExpired', 'passwordExpired', 1),
        (5, b'1', 'logHistory@mail.com', 'last name logHistory',
        'middle_name logHistory', 'logHistory', 'logHistory', 1);
insert into tlog_pass(active, expired, password, id_user, expired_date)
VALUES (b'1', b'0', '$2a$10$MACyQrLADzY05eCCJ.PW/elkwtogq77WTfFJKkAM67zYYvcs1ZBIW', 1, '2024-12-11'),
       (b'1', b'0', '$2a$10$MACyQrLADzY05eCCJ.PW/elkwtogq77WTfFJKkAM67zYYvcs1ZBIW', 2, '2024-02-11'),
       (b'0', b'1', '$2a$10$MACyQrLADzY05eCCJ.PW/elkwtogq77WTfFJKkAM67zYYvcs1ZBIW', 4, '2024-02-11');