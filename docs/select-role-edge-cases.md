# Select your role — 9000000013 (pre-automation catalog)

**Account:** `9000000013` / OTP `1234`  
**Dump:** `/tmp/l2b-role-mat-0013-20261007`  
**Path for Materials:** Vendor / Owner → Next → category  
**Never:** Log Out Confirm · Accept/Decline · force-stop · permanent Operator Next when testing Materials path

## Chrome
- Title **Select your role ?**
- **Vendor / Owner** — Manages a business, sells materials, or owns equipment.
- **Operator / Driver** — Provides service by operating machinery or driving.
- **Next** (outer clickable)

## Happy Path (SR-L1–L10 / SR-I1–I10)
| ID | Action | Expected |
|----|--------|----------|
| SR-L1 | Land on role | Title visible |
| SR-L2 | Vendor card copy | Visible |
| SR-L3 | Operator card copy | Visible |
| SR-L4 | Next visible | True |
| SR-L5 | Package Vendor | `l2b` |
| SR-L6 | No Accept/Decline | True |
| SR-L7 | No Log Out? | True |
| SR-L8 | Vendor subtitle | Visible |
| SR-L9 | Operator subtitle | Visible |
| SR-L10 | Stay on role until Next | True |
| SR-I1 | Tap Vendor / Owner | Stays role |
| SR-I2 | Tap Operator / Driver | Stays role |
| SR-I3 | Re-tap Vendor | Stays role |
| SR-I4 | Rapid Vendor ×3 | No crash |
| SR-I5 | Rapid Operator ×3 | No crash |
| SR-I6 | Vendor then Next | Category |
| SR-I7 | Device Back from category | Role again |
| SR-I8 | Operator then Vendor | Stays role |
| SR-I9 | Next after Vendor | Leaves to category |
| SR-I10 | Stay Vendor package | `l2b` |

## Critical Edge (SR-E1–E10)
| ID | Action | Expected |
|----|--------|----------|
| SR-E1 | Rotate L↔P | Role or Vendor |
| SR-E2 | Background 2s | Recover role/category |
| SR-E3 | Rapid role toggle | No crash |
| SR-E4 | Offline chrome | Role visible |
| SR-E5 | Not launcher | True |
| SR-E6 | No Accept after toggle | True |
| SR-E7 | No Log Out Confirm | True |
| SR-E8 | Double Next after Vendor | Category or Tell us |
| SR-E9 | Vendor package after bg | `l2b` |
| SR-E10 | Re-open role via Back | Role |

## Materials path note
After Vendor → Next, automate **Please select the category** (Materials Vendor).
