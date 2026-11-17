-- APML user and designation tables. MySQL 8.0.16 or later (CHECK constraints are
-- enforced from 8.0.16). Load with:  mysql < schema.sql
-- Not executed in the session that wrote it: MySQL was assumed, not installed.
--
-- Rules the schema enforces, from MORAL.md:
--   2  no rank, level or privilege column exists; a designation is a name only
--   3  rows are never deleted; issue and revoke history is append-only
--   4  every write names an actor and a reason (NOT NULL plus a non-empty CHECK)
--   7  closing a user clears display_name and keeps the pseudonym and the record

CREATE DATABASE IF NOT EXISTS apml
  DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE apml;

CREATE TABLE users (
  id            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  pseudonym     VARCHAR(64) CHARACTER SET ascii COLLATE ascii_bin NOT NULL,
  display_name  VARCHAR(120) NULL,
  created_at    TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  opened_by     VARCHAR(64)  NOT NULL,
  opened_reason VARCHAR(255) NOT NULL,
  closed_at     TIMESTAMP(3) NULL,
  closed_by     VARCHAR(64)  NULL,
  closed_reason VARCHAR(255) NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uq_users_pseudonym (pseudonym),
  CONSTRAINT ck_users_opened CHECK (CHAR_LENGTH(opened_by) > 0 AND CHAR_LENGTH(opened_reason) > 0),
  CONSTRAINT ck_users_closed CHECK (
    (closed_at IS NULL AND closed_by IS NULL AND closed_reason IS NULL)
    OR (closed_at IS NOT NULL AND CHAR_LENGTH(closed_by) > 0 AND CHAR_LENGTH(closed_reason) > 0
        AND display_name IS NULL))
) ENGINE=InnoDB;

CREATE TABLE designations (
  code           VARCHAR(40) CHARACTER SET ascii COLLATE ascii_bin NOT NULL,
  label          VARCHAR(120) NOT NULL,
  created_at     TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  defined_by     VARCHAR(64)  NOT NULL,
  defined_reason VARCHAR(255) NOT NULL,
  PRIMARY KEY (code),
  CONSTRAINT ck_designations CHECK (CHAR_LENGTH(label) > 0
    AND CHAR_LENGTH(defined_by) > 0 AND CHAR_LENGTH(defined_reason) > 0)
) ENGINE=InnoDB;

CREATE TABLE designation_issues (
  id               BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  user_id          BIGINT UNSIGNED NOT NULL,
  designation_code VARCHAR(40) CHARACTER SET ascii COLLATE ascii_bin NOT NULL,
  kind             ENUM('ISSUE','REVOKE') NOT NULL,
  actor            VARCHAR(64)  NOT NULL,
  reason           VARCHAR(255) NOT NULL,
  created_at       TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  PRIMARY KEY (id),
  KEY ix_issues_user_code (user_id, designation_code, id),
  CONSTRAINT fk_issues_user FOREIGN KEY (user_id) REFERENCES users (id),
  CONSTRAINT fk_issues_designation FOREIGN KEY (designation_code) REFERENCES designations (code),
  CONSTRAINT ck_issues CHECK (CHAR_LENGTH(actor) > 0 AND CHAR_LENGTH(reason) > 0)
) ENGINE=InnoDB;

-- A designation is held when the latest event for (user, code) is an ISSUE.
CREATE VIEW current_designations AS
SELECT i.user_id, i.designation_code, i.actor AS issued_by, i.reason AS issue_reason,
       i.created_at AS issued_at
FROM designation_issues i
JOIN (SELECT user_id, designation_code, MAX(id) AS last_id
      FROM designation_issues GROUP BY user_id, designation_code) l ON l.last_id = i.id
WHERE i.kind = 'ISSUE';

DELIMITER //

CREATE TRIGGER trg_users_no_delete BEFORE DELETE ON users FOR EACH ROW
BEGIN
  SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'users rows are never deleted; close the user instead';
END//

CREATE TRIGGER trg_users_guard_update BEFORE UPDATE ON users FOR EACH ROW
BEGIN
  IF NOT (NEW.id <=> OLD.id AND NEW.pseudonym <=> OLD.pseudonym AND NEW.created_at <=> OLD.created_at
          AND NEW.opened_by <=> OLD.opened_by AND NEW.opened_reason <=> OLD.opened_reason) THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'identity and opening record are immutable';
  END IF;
  IF NEW.display_name IS NOT NULL AND NOT (NEW.display_name <=> OLD.display_name) THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'display_name may only be cleared';
  END IF;
  IF OLD.closed_at IS NOT NULL AND NOT (NEW.closed_at <=> OLD.closed_at AND NEW.closed_by <=> OLD.closed_by
          AND NEW.closed_reason <=> OLD.closed_reason AND NEW.display_name <=> OLD.display_name) THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'a closed user cannot be changed';
  END IF;
END//

CREATE TRIGGER trg_designations_no_delete BEFORE DELETE ON designations FOR EACH ROW
BEGIN
  SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'designations are never deleted';
END//

CREATE TRIGGER trg_designations_no_update BEFORE UPDATE ON designations FOR EACH ROW
BEGIN
  SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'designations are never edited';
END//

CREATE TRIGGER trg_issues_no_update BEFORE UPDATE ON designation_issues FOR EACH ROW
BEGIN
  SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'designation history is append-only';
END//

CREATE TRIGGER trg_issues_no_delete BEFORE DELETE ON designation_issues FOR EACH ROW
BEGIN
  SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'designation history is append-only';
END//

DELIMITER ;
