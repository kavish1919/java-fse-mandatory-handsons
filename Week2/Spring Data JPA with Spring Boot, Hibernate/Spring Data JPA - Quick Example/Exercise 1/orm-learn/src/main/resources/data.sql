INSERT INTO country (co_code, co_name) VALUES ('IN', 'India')
ON DUPLICATE KEY UPDATE co_name = 'India';

INSERT INTO country (co_code, co_name) VALUES ('US', 'United States of America')
ON DUPLICATE KEY UPDATE co_name = 'United States of America';
