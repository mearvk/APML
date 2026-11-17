-- Least-privilege account for the application. Run once as an administrator.
-- RANDOM PASSWORD (MySQL 8.0.18+) prints a generated password; no secret lives in this repository.
-- The application account cannot DELETE, cannot UPDATE designation history, and can UPDATE
-- only the four columns used to close a user.
CREATE USER IF NOT EXISTS 'apml_app'@'localhost'
  IDENTIFIED WITH caching_sha2_password BY RANDOM PASSWORD;

GRANT SELECT ON apml.users TO 'apml_app'@'localhost';
GRANT SELECT ON apml.designations TO 'apml_app'@'localhost';
GRANT SELECT ON apml.designation_issues TO 'apml_app'@'localhost';
GRANT SELECT ON apml.current_designations TO 'apml_app'@'localhost';
GRANT INSERT ON apml.users TO 'apml_app'@'localhost';
GRANT INSERT ON apml.designations TO 'apml_app'@'localhost';
GRANT INSERT ON apml.designation_issues TO 'apml_app'@'localhost';
GRANT UPDATE (display_name, closed_at, closed_by, closed_reason) ON apml.users TO 'apml_app'@'localhost';
