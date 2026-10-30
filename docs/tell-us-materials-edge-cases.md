# Tell us (Materials) — 9000000013

**Dump:** `/tmp/l2b-tellus-mat-0013-20261007`  
**Reach:** Role → Vendor → Category → Materials → Continue  
**Never:** Log Out Confirm · Accept/Decline · force-stop · Pin permanently if it submits · Continue after full valid submit unless advancing on purpose

## Chrome
- **Tell us about yourself ?** · Back · Add profile photo
- Prefill **waardian tech** · **manju@gmail.com**
- **Type of company *** → expands Sole Proprietorship / LLP / Pvt. Ltd / Others
- **Warehouse location** · Set where you stock material. · **Use current** · **Pin on map**
- **Continue**

## Happy Path (TM-L1–L10 / TM-I1–I10)
| ID | Action | Expected |
|----|--------|----------|
| TM-L1 | Land Tell us Materials | Title |
| TM-L2 | Prefill name/email | Visible |
| TM-L3 | Type of company * | Visible |
| TM-L4 | Warehouse location | Visible |
| TM-L5 | Use current + Pin on map | Visible |
| TM-L6 | Continue | Visible |
| TM-L7 | Back | Visible |
| TM-L8 | Package | `l2b` |
| TM-L9 | No Accept/Decline/Log Out | True |
| TM-L10 | Continue without company | Stay Tell us |
| TM-I1 | Open company type options | Sole/LLP/Pvt/Others |
| TM-I2 | Tap Sole Proprietorship | Stays Tell us |
| TM-I3 | Tap LLP | Stays Tell us |
| TM-I4 | Use current (permission OK) | Stay Vendor |
| TM-I5 | Pin on map then device Back | Back to Tell us |
| TM-I6 | Device Back from Tell us | Category |
| TM-I7 | Re-open Materials path | Tell us |
| TM-I8 | Rapid Continue ×3 | Stay Tell us if incomplete |
| TM-I9 | Photo chip tap | No crash |
| TM-I10 | Stay Vendor | `l2b` |

## Critical Edge (TM-E1–E10)
Rotation, background, offline, not launcher, rapid company type, rapid Continue, no Log Out, invalid email restore, long name restore, map Back recover.
