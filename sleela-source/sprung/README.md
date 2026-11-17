# sprung: Weighty, short-lived SocketServerNewton servers

"Weighty" is the name for a Sprung framework that scales new SLeeLa servers. Each server, a `SocketServerNewton`, goes through a fixed lifecycle and is torn down, all within 2 seconds.

**This is a simulation.** Time is simulated milliseconds, and sockets, bytes and certificates are modelled. No ports are opened and no real cryptography runs. A real deployment would use X.509 certificates over TLS 1.3 (RFC 8446), issued by a real certificate authority.

## Lifecycle of one server

| # | Stage | Base time | What happens |
|---|---|---:|---|
| 1 | built | 120 ms | Created and bound |
| 2 | served | 200 ms | Ordinary data is sent (40 requests) |
| 3 | cycled | 50 ms | Connections are drained and buffers reset |
| 4 | renewed | 80 ms | Configuration and keys are refreshed |
| 5 | certified | 150 ms | The authority issues a certificate for this server |
| 6 | weighty | payload ÷ link | The weighty payload is sent in checksummed frames. A valid certificate is required. |
| 7 | recycled | 60 ms | Payload buffers are released |
| 8 | vibrant | 80 ms | Renewed again: the old certificate is revoked, a new one is issued, and a health check runs |
| 9 | torn down | 40 ms | The certificate is revoked and the server is gone |

The default weighty payload is 4 MiB in 64 frames of 64 KiB, sent over a 50,000 bytes/ms link (about 84 ms). The base lifecycle is 863 ms. Each server varies every stage time by up to 30% either way.

**"Weight"** is measured in bytes. A server's **Newton** figure is its weighty throughput in bytes per millisecond.

## Rules

- **Order:** every stage must follow the one before it. Anything else is refused with a reason, such as "weighty send out of order: server is built".
- **Certificates:** a weighty send is refused unless the certificate is issued for this server, is within its 1,500 ms validity and has not been revoked.
- **Budget:** before starting a stage, the server checks that the stage plus tear-down still fits in 2,000 ms. If it doesn't, the server tears down at once with the reason "budget".
- **Integrity:** a frame whose checksum doesn't match is detected and sent again.
- **Tear-down:** allowed from any live stage. It always revokes the certificate. Nothing is accepted afterwards.

## Scaling

`SprungScaler` keeps enough servers alive for the current demand, up to 16 at once, and reuses each slot after its server tears down. All servers share one certificate authority. The number of servers to keep alive is the demand divided by one server's rate (40 requests per lifecycle, about 46 requests per second), rounded up.

## Result (2026-11-16)

**One server:** built at 100 ms, Vibrant at 890 ms, torn down at 930 ms. It sent 4,276,224 bytes, all 64 frames were verified, and its Newton figure was 38,130 bytes/ms. Two certificates were issued and both were revoked.

**Scaling over 10 s**, with demand at 80 requests/s, then 600, then 120:

| Time | Live servers |
|---|---:|
| 1 s | 2 |
| 3.5 s | 14 (the peak) |
| 8 s | 3 |

| Total | Value |
|---|---:|
| Servers built and torn down | 76 and 76 |
| Servers that reached Vibrant | 76 |
| Longest lifecycle | 1,010 ms |
| Requests served | 3,040 |
| Weighty bytes sent | 318,767,104 |
| Certificates issued and revoked | 152 and 152 |
| Refused certificate checks | 0 |

**Edge cases:**
- A corrupted frame was detected and sent again once.
- A 120 MiB payload was never started, because it could not finish inside the budget. That server was torn down at 560 ms and was not counted as Vibrant.
- A weighty send was refused before certification, and again under an expired certificate.

Checks: `weighty: all 30 checks passed`.

```
sleela compile sprung/*.sleela -o build/weighty.sleela
sleela run build/weighty.sleela
```

## Open items

- Real sockets. The `lib/net` classes in SLeeLa, such as `SLSocket`, are stubs for now, so a real server needs a native bridge.
- A real certificate authority and TLS.
- Measured stage times to replace the assumed ones.
- How Weighty connects to the Sprung message log, and to your still-undefined "Voltage on Time" score for weighty messages.
