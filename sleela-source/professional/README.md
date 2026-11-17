# professional: eight lanes at 6000 files per second

The professional circuit runs at ten times the medium current. It has eight lanes, and each stream file is assigned to a lane by a hash of its serial number. Files derived from it stay in that lane.

Three things are shared by all lanes because they must hold across the whole code base:
- **the approval gate:** people do not multiply with lanes;
- **the duplicate set:** content seen in any lane is a duplicate in every lane;
- **the serial source:** ids stay unique.

```
sleela compile circuit/*.sleela medium/MediumProfile.sleela professional/*.sleela -o build/professional.sleela
sleela run build/professional.sleela
```

## What changes from medium, and why

| Setting | Medium | Professional | Reason |
|---------|--------|--------------|--------|
| Files per second | 600 | 6000 | 10x |
| Lanes | 1 | 8 | Current is split across lanes |
| Header required | warning | refused | Every file needs known provenance at this volume |
| Description metadata | warning | refused | The description is the source of truth and must say who made it |
| Generated mark | warning | refused | Unmarked generated code has no known origin |
| Duplicate content | not checked | refused, exactly | Copies multiply work in every later strand |
| Hard lines and width | 20000 and 1000 | 5000 and 400 | Oversized and minified files are refused earlier |
| Soft lines, width, imports, members | 1500, 160, 60, 80 | 1000, 120, 40, 50 | Drift is reported earlier |
| Approvals | 2/s, 25 each | 4/s, 50 each | People do not scale 10x, so batches grow |
| Coalescing | off | one change request per finding kind per 500 ms per lane | Keeps change requests within what people can approve |

## Result (2026-11-16, 5 simulated seconds)

| Terminal | Taken | Rate | Load | Peak queue | Avg wait |
|----------|------:|-----:|-----:|-----------:|---------:|
| APML | 2027 | 405/s | 25% | 8 | 4 ms |
| Bloq | 2023 | 404/s | 25% | 2 | 10 ms |
| Bodi | 7446 | 1489/s | 37% | 13 | 9 ms |
| Sprung | 19788 | 3957/s | 41% | 11 | 10 ms |
| Runyn | 12378 | 2475/s | 51% | 8 | 0 ms |
| Falthruu | 828 | 165/s | 5% | 11 | 33 ms |

- **Balance:** sourced 30000 + produced 7170 = sunk 19788 + consumed 2851 + grounded 14477 + waiting 54.
- **Grounded, by reason:**

  | Reason | Files |
  |--------|------:|
  | out of scope | 7287 |
  | binary | 2058 |
  | missing header | 1946 |
  | duplicate | 901 |
  | missing metadata | 722 |
  | malformed | 594 |
  | too wide | 579 |
  | generated without mark | 211 |
  | too many lines | 179 |

  No file was refused for a full buffer.
- **Runyn:** 5882 findings. They produced 273 change requests, and the other 5609 findings were attached to open requests.
- **Lane loads:** between 3732 and 3760 files per lane.
- **Checks:** 11 of 11 passed. Every duplicate was refused, and only duplicates were. No unmarked generated file was admitted.

## Finding: coalescing is required at 10x

Without coalescing, the tighter soft limits raise about 560 change requests per second, while people can approve 200. Falthruu backs up (2039 waiting), Runyn stops, and the router refused 4836 stream files for a full buffer in 5 seconds. Grouping findings into one change request per kind per window removes the stall entirely.
