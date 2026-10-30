# KYC (Material 9000000017) — gap retest

**Reach:** Profile drawer → **KYC**  
**Dump:** `/tmp/l2b-material-kyc-0017-suite`  
**Suite:** `kyc/material-kyc-all.xml`  
**Never:** force-stop · Log Out Confirm · Accept/Decline · final KYC submit  
Reuse `KycPage` chrome; seed may show **full form** or **error empty** (dual-path).

## Chrome / locators (when present)
| Locator | Type |
|---------|------|
| `KYC` / title | Header |
| Document / Bank sections | Full chrome |
| Error + `Retry` | Empty/error path |
| `content-desc='Back'` | Back |

## Landing (MK-L1–L10)
| ID | Action | Expected |
|----|--------|----------|
| MK-L1 | Open KYC | Displayed |
| MK-L2 | Title or error | Dual-path |
| MK-L3 | Full chrome or error | Dual-path |
| MK-L4 | Package | `l2b` |
| MK-L5 | No Accept | True |
| MK-L6 | No Log Out | True |
| MK-L7 | No Decline | True |
| MK-L8 | Not launcher | True |
| MK-L9 | Retry if error | Retry visible when error |
| MK-L10 | Package again | `l2b` |

## Interact (MK-I1–I10)
| ID | Action | Expected |
|----|--------|----------|
| MK-I1 | Open | Displayed |
| MK-I2 | Back | Not launcher |
| MK-I3 | Reopen | KYC |
| MK-I4 | Retry if present | Stay KYC / vendor |
| MK-I5 | Title/error after reopen | Dual-path |
| MK-I6 | No Accept | True |
| MK-I7 | No Log Out | True |
| MK-I8 | Back again | Stay vendor |
| MK-I9 | Reopen 2 | KYC |
| MK-I10 | Package | `l2b` |

## Edge (MK-E1–E10)
| ID | Case |
|----|------|
| MK-E1 | Rotation |
| MK-E2 | Background recover |
| MK-E3 | Offline stays KYC |
| MK-E4 | Not launcher |
| MK-E5 | No Accept |
| MK-E6 | No Log Out |
| MK-E7 | Rapid Back/reopen |
| MK-E8 | No Decline |
| MK-E9 | Reopen |
| MK-E10 | Package |
