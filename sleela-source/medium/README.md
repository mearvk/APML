# medium: one lane at 600 files per second

The medium profile has a single lane. Its limits are permissive: a missing header or generated mark is admitted with a warning, and duplicate files are not checked. One approval covers up to 25 changes, and two approvals are available per second.

```
sleela compile circuit/*.sleela medium/*.sleela -o build/medium.sleela
sleela run build/medium.sleela
```

## Result (2026-11-16, 5 simulated seconds)

| Terminal | Taken | Rate | Load | Peak queue | Avg wait |
|----------|------:|-----:|-----:|-----------:|---------:|
| APML | 408 | 81/s | 40% | 13 | 14 ms |
| Bloq | 406 | 81/s | 40% | 2 | 10 ms |
| Bodi | 1392 | 278/s | 69% | 44 | 27 ms |
| Sprung | 2854 | 570/s | 57% | 9 | 10 ms |
| Runyn | 1465 | 293/s | 58% | 5 | 0 ms |
| Falthruu | 217 | 43/s | 10% | 20 | 84 ms |

- **Balance:** sourced 3000 + produced 1585 = sunk 2854 + consumed 623 + grounded 1095 + waiting 13.
- **Grounded, by reason:** out of scope 749, binary 211, malformed 57, too wide 53, missing header 25. No file was refused for a full buffer.
- **Warnings:** 70 descriptions without metadata, 27 unmarked generated files and 170 Java files without a header were admitted, each with a warning.
- **Checks:** 7 of 7 passed.

## Sensitivity finding

With one approval covering 10 changes instead of 25, the circuit stalls. Change requests arrive at about 43 per second, but people can clear only 20 per second. Falthruu's queue fills, and Runyn stops because it has nowhere to send change requests. Runyn's queue then fills, and the router refused 315 stream files for a full buffer in 5 seconds. **The approval rate sets the capacity of the whole circuit.**
