# sprung: Weighty, short-lived SocketServerNewton servers

"Weighty" is the name for a Sprung framework that scales new SLeeLa servers. Each server, a `SocketServerNewton`, goes through a fixed lifecycle and is torn down, all within 2 seconds.

There are two implementations of the same lifecycle:

- **Simulated** (`SocketServerNewton`, `SprungScaler`, `WeightyDemo`). Time is simulated milliseconds, and sockets and bytes are modelled.
- **Native** (`NativeSocketServerNewton`, `NativeNewtonHost`, `NativeFrames`, `NativeWeightyDemo`). It runs over real TCP sockets in the SLeeLa VM, using real wall-clock time. See [Native sockets](#native-sockets) below.

In both, certificates are modelled by `NewtonCertificateAuthority` and no real cryptography runs. A real deployment would use X.509 certificates over TLS 1.3 (RFC 8446), issued by a real certificate authority.

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
sleela compile sprung/WeightyProfile.sleela sprung/NewtonCertificateAuthority.sleela sprung/SocketServerNewton.sleela sprung/SprungScaler.sleela sprung/WeightyDemo.sleela -o build/weighty.sleela
sleela run build/weighty.sleela
```

## Native sockets

The SLeeLa VM already supports real TCP sockets. The `lib/net` classes such as `SLSocket` are stubs, but the language has built-in calls backed by `impl/core/sleela_net.c` (Linux, Windows and macOS):

| Built-in | What it does |
|---|---|
| `listen(port)` | Opens a listener and returns a handle, or -1 |
| `accept(h)` | Accepts a connection |
| `connect(host, port)` | Opens a connection |
| `sockread(h)` | Reads up to 4,096 bytes as a String; "" at end of stream |
| `sockwrite(h, s)` | Writes a String and returns the bytes written |
| `sockclose(h)` | Closes a handle |

There are also `spawn`, `join`, `lock` and `unlock` for threads, and `timeMonotonicNanos` for a real clock. All were checked in the sandbox with `sleela` 1.3.2-pro.

### How the native version works

- `NativeNewtonHost` owns a real listener on 127.0.0.1 and answers one message at a time: PING, REQ, KEY?, CERT?, W, BYE, QUIT.
- `NativeSocketServerNewton` drives the nine stages over real connections:
  - The served stage keeps one connection open and serves requests for 200 ms of real time.
  - The weighty stage sends 64 frames of 64 KiB, and each frame must be acknowledged before the next.
  - Tear-down sends QUIT, which closes the listener, and revokes the certificate.
- **Frame check.** The host checks each frame's exact length and its 16-character trailing token. This is not a checksum over every byte.
- **Ports.** A port still held by the operating system from an earlier run (TIME_WAIT) can't be reopened. The server moves to the next free port in the range 48000 to 48999.

### Result (2026-11-16, real time)

**One server:** built at 0 ms, served at 200 ms, weighty at 230 ms, torn down at 230 ms.
- It answered 13,920 requests over TCP. The 40 in the profile is a minimum; the served stage runs for its full 200 ms.
- It sent 4 MiB in 30 ms, a Newton figure of 139,810 bytes/ms.

**Scaling over 6 s of real time,** with demand at 200 requests/s, then 2,400, then 400:

| Time | Live servers |
|---|---:|
| 1 s | 1 |
| 3 s | 12 (the peak) |
| 5 s | 2 |

| Total | Value |
|---|---:|
| Servers built, torn down and Vibrant | 85, 85, 85 |
| Longest real lifecycle | 528 ms |
| Requests over TCP | 256,620 |
| Weighty bytes over TCP | 356,515,840 |
| Average Newton figure | 147,692 bytes/ms |
| Certificates issued | 170, none left outstanding |
| Port retries | 15 |

**Edge cases:**
- A corrupted frame was refused by the host and sent again.
- A 120 MiB payload was not started because it could not finish in time.
- Steps taken out of order were refused.

Checks: `native weighty: all 25 checks passed`. This was repeated over three back-to-back runs.

```
sleela compile sprung/WeightyProfile.sleela sprung/NewtonCertificateAuthority.sleela sprung/Native*.sleela -o build/native-weighty.sleela
sleela run build/native-weighty.sleela
```

### Why the native version uses one thread

The first native version ran each host on its own VM thread (`spawn`). It hung after two or three 4 MiB payloads.

At the hang, `/proc` showed the main thread waiting on a futex and the host thread blocked in `recvfrom`. In `impl/core/sleela_core.c`, `OP_SOCKREAD` holds the socket's mutex for the whole blocking read. Small reproductions with threads did not hang, so the exact cause is not yet found.

The native version therefore runs each host and its controller on one thread, as a single-threaded reactor: write, then the host reads and replies, then read. The kernel socket buffers carry the bytes between the two ends. Several servers run at once by taking turns.

**One VM behaviour to know about:** `OP_SOCKREAD` passes every chunk it reads to the VM string intern table, which is never freed. Received data that keeps changing makes memory grow for the life of the process. A long-running server would need a VM change, such as a read that returns a buffer that is not interned.

## Open items

- A threaded host. This needs the VM read-blocking issue above found and fixed.
- A non-interned socket read in the VM, for long-running servers.
- A real certificate authority and TLS.
- Measured stage times for the simulated profile. The native run gives real ones: about 200 ms serving, 30 ms per 4 MiB payload, and a few ms for each other stage.
- How Weighty connects to the Sprung message log, and to your still-undefined "Voltage on Time" score for weighty messages.
