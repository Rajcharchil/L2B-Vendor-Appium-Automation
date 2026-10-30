# Tell us about yourself — 9000000013 (pre-automation catalog)

**Account:** `9000000013` / OTP `1234` (fresh leftover registration).  
**Dump:** `/tmp/l2b-tellus-0013-20261006`  
**Root id:** `operator_basic_info_root`  
**Never:** Log Out Confirm · Accept/Decline · force-stop (logs out) · Continue after a real TPI upload unless moving to the next page on purpose.

## Chrome (live)
- Back · title **Tell us about yourself ?** · green progress
- Photo placeholder + camera
- Prefill **waardian tech** · **manju@gmail.com**
- **Upload TPI certificate document *** (upto 6mb jpeg, pdf)
- **Are you associated with a company?** checkbox
- **Continue** (outer clickable; stays disabled without TPI)

Upload sheet: **Add Document Photo** · Take Photo · Choose from Gallery · **Cancel**

## Happy Path (TU-L1–L10 / TU-I1–I10)
| ID | Action | Expected |
|----|--------|----------|
| TU-L1 | Land after OTP 0013 | Tell us screen |
| TU-L2 | Title + Back | Visible |
| TU-L3 | Prefill name/email | waardian tech, manju@gmail.com |
| TU-L4 | TPI upload row | Visible with 6mb jpeg/pdf hint |
| TU-L5 | Company checkbox copy | Visible |
| TU-L6 | Continue | Visible |
| TU-L7 | Package | Vendor `l2b` |
| TU-L8 | Progress bar | Present (root operator_basic_info) |
| TU-L9 | No Accept/Decline/Log Out | True |
| TU-L10 | Continue without TPI | Does not leave Tell us |
| TU-I1 | Open Tell us | Displayed |
| TU-I2 | Tap company checkbox | Stays on Tell us; Vendor |
| TU-I3 | Un-tap company | Stays on Tell us |
| TU-I4 | Tap Upload TPI | Sheet Take Photo / Gallery / Cancel |
| TU-I5 | Sheet Cancel | Back to Tell us; no file |
| TU-I6 | Device Back on sheet | Sheet closes or stays Tell us |
| TU-I7 | Edit name field (append space then restore) | Still Tell us |
| TU-I8 | Rapid Continue ×3 | Still Tell us (no TPI) |
| TU-I9 | Photo camera chip tap | Picker or permission — no crash |
| TU-I10 | Stay Vendor after scroll/toggle | `l2b` |

## Critical Edge (TU-E1–E10) — no force-stop
| ID | Action | Expected |
|----|--------|----------|
| TU-E1 | Rotate landscape→portrait | Tell us or Vendor |
| TU-E2 | Background 2s | Recover Tell us |
| TU-E3 | Rapid company checkbox ×3 | No crash |
| TU-E4 | Rapid open upload ×3 | One sheet or Tell us |
| TU-E5 | Offline still shows chrome | Tell us visible |
| TU-E6 | Not launcher | True |
| TU-E7 | Continue spam while disabled | Stay Tell us |
| TU-E8 | Long name paste | No crash |
| TU-E9 | Invalid email tweak then restore | Stay Tell us |
| TU-E10 | No Log Out Confirm from this screen | True |

## Next pages (only after this suite is green)
Select/list machines · KYC form · Profile Verification — still untested on 0013.
