# Profile Verification — Material track (9000000017)

**Reach probe:** Home → Profile drawer → Profile Verification / KYC & Payment / KYC  
**Suite:** `profileverification/profile-verification-all.xml`  
**Never:** Accept/Decline · Log Out Confirm · force-stop  

**Note:** Warm verified **0017** pe PV hub often **unreachable**. Probe documents reachability; if missing, assert vendor still healthy (not launcher) + recover Home. Full hub needs pending-KYC / registration Continue (maps blocked on 0013).

## Locators (when shown)
| Locator | Type |
|---------|------|
| `Profile Verification` | Title |
| `KYC` / `Verification` | Steps / chrome |
| Drawer rows | Entry |

## Probe cases (PV-L1–L5 + PV-E1–E5 = 10)
| ID | Action | Expected |
|----|--------|----------|
| PV-L1 | Probe reachability | Shown → title/steps; else document unreachable + vendor healthy |
| PV-L2 | No Accept / Decline / Log Out | True |
| PV-L3 | Package | `l2b` |
| PV-L4 | Not launcher | True |
| PV-L5 | Recover Material Home | Home displayed |
| PV-E1 | Background after probe | Not launcher |
| PV-E2 | No Accept | True |
| PV-E3 | No Log Out | True |
| PV-E4 | Reopen Home | Home |
| PV-E5 | Package | `l2b` |
