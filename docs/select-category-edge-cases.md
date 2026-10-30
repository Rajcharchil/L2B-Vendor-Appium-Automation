# Select category — Materials path 9000000013

**Dump:** `/tmp/l2b-role-mat-0013-20261007` (05–08)  
**Reach:** Role → Vendor / Owner → Next  
**Never:** Log Out Confirm · Accept/Decline · force-stop · Rental Continue when testing Materials

## Chrome
- Title **Please select the category**
- **Materials Vendor** (+ materials blurb when selected)
- **Rental Vendor**
- **Continue**

## Happy Path (SC-L1–L10 / SC-I1–I10)
| ID | Action | Expected |
|----|--------|----------|
| SC-L1 | Open category | Title |
| SC-L2 | Materials Vendor | Visible |
| SC-L3 | Rental Vendor | Visible |
| SC-L4 | Continue | Visible |
| SC-L5 | Package | `l2b` |
| SC-L6 | No Accept/Decline | True |
| SC-L7 | Materials blurb when selected | Visible |
| SC-L8 | Back/device Back → role | Role |
| SC-L9 | No Log Out? | True |
| SC-L10 | Stay category until Continue | True |
| SC-I1 | Tap Materials | Stays category |
| SC-I2 | Tap Rental | Stays category |
| SC-I3 | Re-tap Materials | Stays category |
| SC-I4 | Materials then Continue | Tell us Materials |
| SC-I5 | Device Back | Role |
| SC-I6 | Re-open category Vendor→Next | Category |
| SC-I7 | Rapid Materials ×3 | No crash |
| SC-I8 | Rapid Rental ×3 | No crash |
| SC-I9 | Continue without reselect | Tell us or category |
| SC-I10 | Package after toggle | `l2b` |

## Critical Edge (SC-E1–E10)
Rotation, background, offline, not launcher, no Accept, no Log Out, rapid Continue, Materials after Rental, Vendor package, recover via Role Next.
