# Inventory — Material vendor 9000000017

**Dump:** `/tmp/l2b-inventory-0017-suite` · explore `/tmp/l2b-material-0017-gap-20261007`  
**Reach:** Bottom tab **Inventory** · Drawer **Your Inventory**  
**Never:** Log Out Confirm · Accept/Decline · force-stop · Save changes persist · Add Product Save · CSV upload confirm

## Chrome
- Title **Inventory** · **+ Add Product** · **Import CSV** · **Search Item...**
- **All Material Inventory** + count · rows (name, In stock / Out of stock, reserved, qty, Pieces)
- content-desc: Back · Expand · Filter · Decrease/Increase quantity
- Bottom tabs: Orders · Home · Earning · Inventory · **Save changes** bar when dirty

## Happy Path (INV-L1–L10 / INV-I1–I10)
| ID | Action | Expected |
|----|--------|----------|
| INV-L1 | Bottom Inventory tab | Title Inventory |
| INV-L2 | Add Product CTA | Visible |
| INV-L3 | Import CSV | Visible |
| INV-L4 | Search Item | Visible |
| INV-L5 | All Material Inventory + count | Visible |
| INV-L6 | Stock row (In stock / AAC) | Visible |
| INV-L7 | Material bottom tabs | Stay |
| INV-L8 | Package | `l2b` |
| INV-L9 | No Accept / Log Out | True |
| INV-L10 | Drawer Your Inventory | Same Inventory |
| INV-I1 | Search type AAC | List filters or stays |
| INV-I2 | Clear search | Inventory |
| INV-I3 | Filter open/close | Stays Inventory |
| INV-I4 | Expand | Stays Inventory |
| INV-I5 | Decrease qty (no Save) | Stays Inventory |
| INV-I6 | Increase qty (no Save) | Stays Inventory |
| INV-I7 | Home tab then Inventory | Inventory |
| INV-I8 | Orders tab then Inventory | Inventory |
| INV-I9 | Back when present | Home or Inventory |
| INV-I10 | Package | `l2b` |

## Critical Edge (INV-E1–E10)
Rotation, background, offline, not launcher, no Accept, no Log Out, rapid Filter, empty Search, reopen, material tabs stay.
