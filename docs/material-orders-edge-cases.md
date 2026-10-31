# Material Orders — 9000000017

**Reach:** Bottom **Orders** · Home Upcoming **See all**  
**Never:** Accept/Decline · status-changing lifecycle · Log Out Confirm · force-stop

## Chrome
Tabs Upcoming / Active / Completed · Confirmed cards · Picked up scheduled · Booking for · Get Direction · address · View More Details · Help · Upcoming Orders

## L/I/E
Landing: tabs, card chrome, Get Direction, View More, Help, tabs stay, package, no Accept/Decline.  
Interact: Upcoming/Active/Completed switches, Get Direction (+Back), Help entry (+Back), Home See all, reopen, package.  
Edge: rotation, background, offline, not launcher, no Accept/Log Out, rapid tab switch, reopen.

## Empty vs load-error (F2 / #60)
- **Empty pass:** explicit copy only (`No order…`, `Nothing here`, `No upcoming`, `No orders right now`). Tabs alone are **not** empty.
- **Load-error (bug #60, fail not pass):** `Could not load your orders` / `Check your connection` / Retry without cards.
- Home **See all** missing + empty Home copy → SkipException (MO-I6), not a hard fail.
