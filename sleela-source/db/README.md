# db: MySQL port for users and designations

A small port that stores APML users and the designations issued to them in MySQL. It has a schema, a least-privilege account, and a SLeeLa connector and port.

**Status.** MySQL was assumed and not installed. The SQL has not been run against a server. The SLeeLa code compiles and its 47 checks pass, with statements compared as text and a loopback seam standing in for the database.

## Files

| File | What it is |
|---|---|
| `schema.sql` | Database `apml`: tables `users`, `designations`, `designation_issues`, view `current_designations`, and guard triggers. Needs MySQL 8.0.16+ (CHECK constraints are enforced from that version). |
| `grants.sql` | Account `apml_app@localhost` with a generated password (MySQL 8.0.18+). It can SELECT and INSERT, and can UPDATE only the four columns used to close a user. It has no DELETE. |
| `ApmlDbConnector.sleela` | The MySQL connector: coordinates, link state, a statement journal, and quoting. |
| `ApmlSqlText.sleela` | Text checks and MySQL string quoting, written with the VM string builtins so no `/lib` is needed. |
| `ApmlUserDesignationPort.sleela` | The port: one statement per act. |
| `DbPortDemo.sleela` | The 47 checks. |

## Model

- **users**: a pseudonym (a-z, 0-9, hyphen, 3 to 64 characters), an optional display name, and who opened or closed the user and why.
- **designations**: the catalogue of names that can be issued, each with a code, a label, and who defined it and why. A designation carries no rank, level or privilege (MORAL.md statement 2). What the catalogue holds is the owner's decision. None are seeded.
- **designation_issues**: an append-only history of ISSUE and REVOKE events. A user holds a designation when the latest event for that user and code is an ISSUE. The view `current_designations` computes this.

## What is enforced, and where

| Rule | Port | Database |
|---|---|---|
| Every write names an actor and a reason (MORAL.md 2, 4) | `checkWho` returns 1 or 2 | NOT NULL plus CHECK on non-empty |
| History is never edited or deleted (3) | no DELETE, UPDATE or DROP is ever built | triggers refuse UPDATE and DELETE on history and catalogue, and DELETE on users; the account has no DELETE |
| Closing keeps the record (7) | `closeUser` clears the display name only | CHECK: a closed user has no display name; a trigger makes the identity and opening record immutable and a closed user unchangeable |
| A closed user receives nothing new | `issueDesignation` requires `closed_at IS NULL` | |
| No double issue, no revoke of what is not held | `NOT EXISTS` / `EXISTS` on `current_designations` | |
| Every refusal is explained (4) | `refusalName(code)` | trigger messages |

## Port result codes

0 done, 1 actor missing, 2 reason missing, 3 bad pseudonym, 4 bad designation code, 5 link not open, 6 text empty, too long or with control characters, 7 bad argument, 9 statement journaled (adapter required), 10 database changed no row, 11 database error.

Code 10 covers "user closed or unknown, designation unknown, already held, or not held". The single-statement design does not tell these cases apart. Splitting them needs a read before the write.

## Connecting to a real MySQL

The connector does not dial MySQL itself. A host adapter maps its calls to the SLeeLa MySQL ABI: `openLink` to `slsql_open`, `execute` to `slsql_execute`, `query` to `slsql_query`, and `closeLink` to `slsql_close`, the same shape as `lib/advantage/connect/SLSqlConnector.sleela` in mearvk/SLeeLa. Until an adapter is attached, `execute` and `query` journal the statement and return 9. Nothing is reported as written.

- The connector never holds the password. `configure` takes a flag saying a secret is pinned, and the adapter reads the secret from the keystore. `coordinate()` omits the user and the secret.
- The adapter ABI is text-only with no prepared statements, so every value is quoted by `ApmlSqlText.quote`. That quoting assumes a utf8mb4 connection and that `NO_BACKSLASH_ESCAPES` is not set in `sql_mode`. Identifiers are fixed in the port and never come from callers.

## Setup, once MySQL is available

```
mysql -u root -p < schema.sql
mysql -u root -p < grants.sql      # prints the generated apml_app password; store it in the keystore
```

## Build and run

```
./build.sh                                   # the last step builds db/
bash tests/run-regression.sh                 # includes db, compared with tests/expected/db.txt
```

Result on 2026-11-17: `db port: all 47 checks passed`, and `regression: all 9 passed`.

Text lengths and positions count UTF-16 code units, because the checks use the VM builtins `javaStringLength` and `javaStringCharAt`. A character outside the Basic Multilingual Plane, such as an emoji, counts as 2 toward the 64, 120 and 255 limits. MySQL counts it as 1, so the port is the stricter of the two.

## Open items

- Which designations exist, and who may define and issue them. This is the owner's decision.
- Whether a closed user may still have a designation revoked. The port allows it so that a stale designation can be cleared.
- The host adapter for `slsql_*`.
- Whether the web app (`source/`) should read designations, which would need a JDBC driver dependency.
