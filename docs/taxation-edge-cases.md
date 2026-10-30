# Taxation — Material 9000000017

**Reach:** Profile drawer → **Taxation**  
**Dump:** `/tmp/l2b-taxation-0017-suite`  
**Suite:** `taxation/taxation-all.xml`  
**Never:** complete paid submit · Log Out Confirm · Accept/Decline · force-stop

## Chrome / locators
| Locator | Type |
|---------|------|
| `Taxation` | Title |
| `GST Monthly Report` | Card |
| `GST On Demand Report` | Card |
| `GSTR-1 Summary` | Card |
| `Download Report` | CTA |
| `Request Report` | CTA |
| `Reports` / `No reports yet` | Empty list |
| `content-desc='Back'` | Back |

## Landing (TX-L1–L10)
| ID | Action | Expected |
|----|--------|----------|
| TX-L1 | Open Taxation | Page displayed |
| TX-L2 | GST Monthly | Visible |
| TX-L3 | On Demand | Visible |
| TX-L4 | GSTR-1 | Visible |
| TX-L5 | Download Report | Visible |
| TX-L6 | Request Report | Visible |
| TX-L7 | Reports empty | No reports yet / Reports |
| TX-L8 | Package | `l2b` |
| TX-L9 | No Accept / Log Out | True |
| TX-L10 | Title | Taxation |

## Interact (TX-I1–I10)
| ID | Action | Expected |
|----|--------|----------|
| TX-I1 | Open | Displayed |
| TX-I2 | Tap Download (no paid submit) | Stay vendor |
| TX-I3 | Tap Request (no paid submit) | Stay vendor |
| TX-I4 | Back | Home / drawer / vendor |
| TX-I5 | Reopen | Taxation |
| TX-I6–I8 | Monthly / On Demand / GSTR-1 still | Visible |
| TX-I9 | No Log Out | True |
| TX-I10 | Package | `l2b` |

## Edge (TX-E1–E10)
| ID | Case |
|----|------|
| TX-E1 | Rotation |
| TX-E2 | Background recover |
| TX-E3 | Offline stays Taxation |
| TX-E4 | Not launcher |
| TX-E5 | No Accept |
| TX-E6 | No Log Out |
| TX-E7 | Rapid Download ×3 |
| TX-E8 | Reopen |
| TX-E9 | Empty reports still |
| TX-E10 | Package |
