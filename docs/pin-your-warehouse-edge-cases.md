# Pin your warehouse — Materials 9000000013

**Dump:** `/tmp/l2b-pin-warehouse-0013-20261007`  
**Reach:** Tell us Materials → Pin on map  
**Never:** Log Out Confirm · Accept/Decline · force-stop

## Chrome
- Title **Pin your warehouse**
- **Map unavailable** / The map could not be loaded. (emulator/QA maps often fail)
- Hint: Move the map so the pin sits on your warehouse.
- Lat/lng chip (e.g. `37.421998, -122.084000`)
- **Confirm location**
- content-desc **Close** · **Use current**

## Happy Path (PW-L1–L10 / PW-I1–I10)
| ID | Action | Expected |
|----|--------|----------|
| PW-L1 | Open pin sheet | Title |
| PW-L2 | Map unavailable or map chrome | Visible |
| PW-L3 | Move-map hint | Visible |
| PW-L4 | Lat/lng chip | Visible |
| PW-L5 | Confirm location | Visible |
| PW-L6 | Close | Visible |
| PW-L7 | Use current (desc) | Visible |
| PW-L8 | Package | `l2b` |
| PW-L9 | No Accept/Decline/Log Out | True |
| PW-L10 | Not launcher | True |
| PW-I1 | Confirm location | Back to Tell us with coords |
| PW-I2 | Close | Back to Tell us |
| PW-I3 | Device Back | Tell us |
| PW-I4 | Re-open Pin on map | Pin sheet |
| PW-I5 | Use current on sheet | Stay Vendor |
| PW-I6 | Rapid Confirm ×3 | Tell us or pin |
| PW-I7 | Rapid Close | Tell us |
| PW-I8 | Confirm then Pin again | Pin sheet |
| PW-I9 | Stay after Confirm | Tell us Materials |
| PW-I10 | Package | `l2b` |

## Critical Edge (PW-E1–E10)
Rotation, background, offline, not launcher, no Accept, no Log Out, rapid Confirm, map unavailable stays, Close recover, reopen.
