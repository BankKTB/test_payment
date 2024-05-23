import { Component, OnInit, ViewChild, Inject } from '@angular/core';
import { MatSort } from '@angular/material/sort';
import { SelectionModel } from '@angular/cdk/collections';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Constant } from '@shared/utils/constant';
export interface DialogData {
  listAllColumn: any;
  listColumnSorted: any;
}
@Component({
  selector: 'app-dialog-sort-column',
  templateUrl: './dialog-sort-column.component.html',
  styleUrls: ['./dialog-sort-column.component.scss'],
})
export class DialogSortColumnComponent implements OnInit {
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  selectionForNotShow = new SelectionModel<any>(true, []);
  selectionForShow = new SelectionModel<any>(true, []);
  selectionForGroup: any;
  listShow = [];
  listNotShow = [];
  listColumn: any;
  listSort: any;
  constructor(
    public constant: Constant,
    private dialogRef: MatDialogRef<DialogSortColumnComponent>,
    private snackBar: MatSnackBar,
    @Inject(MAT_DIALOG_DATA) public data: DialogData
  ) {}

  ngOnInit() {
    this.data.listAllColumn.forEach((item) => {
      const obj = {
        key: item.key,
        sortType: '',
        text: item.text,
      };
      const itemSorted = this.data.listColumnSorted.find((e) => e.id === item.key);
      if (itemSorted) {
        obj.sortType = itemSorted.direction;
        this.listShow.push(obj);
      } else {
        this.listNotShow.push(obj);
      }
    });
  }

  clickSave() {
    let sortListColumn = [];
    this.listShow.forEach((item, index) => {
      sortListColumn.push({
        id: item.key,
        direction: item.sortType,
        sequence: index + 1,
      });
    });
    sortListColumn = sortListColumn.sort((a, b) => (a.sequence > b.sequence ? 1 : -1));
    this.dialogRef.close({
      event: true,
      status: 'Save',
      multiSort: sortListColumn,
    });
  }

  closeDialog() {
    this.dialogRef.close({
      event: true,
      status: 'Close',
      value: '',
    });
  }

  notShowAll() {
    this.listShow.forEach((item) => {
      this.listNotShow.push(item);
    });
    this.listShow = [];
    this.clearAllSelected();
  }

  showAll() {
    this.listNotShow.forEach((item) => {
      this.listShow.push(item);
    });
    this.listNotShow = [];
    this.clearAllSelected();
  }

  showSelected() {
    let sequence = 0;
    for (let i = 0; i < this.listShow.length; i++) {
      if (this.selectionForNotShow.isSelected(this.listShow[i])) {
        sequence = i;
        break;
      } else {
        sequence = this.listShow.length;
      }
    }

    this.listNotShow.reverse().forEach((item) => {
      if (this.selectionForShow.isSelected(item)) {
        this.listShow.splice(sequence, 0, item);
        this.listNotShow = this.listNotShow.filter((data) => data.key !== item.key);
      }
    });
    this.listNotShow.reverse();
    for (let i = 0; i < this.listShow.length; i++) {
      this.listShow[i].seq = i + 1;
    }
    this.clearAllSelected();
  }

  notShowSelected() {
    this.listShow.forEach((item) => {
      if (this.selectionForNotShow.isSelected(item)) {
        this.listNotShow.push(item);
        this.listShow = this.listShow.filter((data) => data.key !== item.key);
      }
    });
    for (let i = 0; i < this.listShow.length; i++) {
      this.listShow[i].seq = i + 1;
    }
    this.clearAllSelected();
  }

  up() {
    if (this.listShow.filter((data) => this.selectionForNotShow.isSelected(data)).length === 0) {
      this.snackBar.open('กรุณา เลือก รายการ', '', {
        panelClass: '_warning',
      });
    } else if (
      this.listShow.filter((data) => this.selectionForNotShow.isSelected(data)).length === 1
    ) {
      let sequence = 0;
      let object = null;
      this.listShow.forEach((item, index) => {
        if (this.selectionForNotShow.isSelected(item)) {
          if (index !== 0) {
            sequence = index;
            object = item;
            this.listShow.splice(index, 1);
          }
        }
      });
      if (sequence !== 0) {
        this.listShow.splice(sequence - 1, 0, object);
        for (let i = 0; i < this.listShow.length; i++) {
          this.listShow[i].seq = i + 1;
        }
      }
    } else if (
      this.listShow.filter((data) => this.selectionForNotShow.isSelected(data)).length > 1
    ) {
      this.snackBar.open('กรุณา เลือกเพียง 1 รายการ', '', {
        panelClass: '_warning',
      });
    }
  }

  down() {
    if (this.listShow.filter((data) => this.selectionForNotShow.isSelected(data)).length === 0) {
      this.snackBar.open('กรุณา เลือก รายการ', '', {
        panelClass: '_warning',
      });
    } else if (
      this.listShow.filter((data) => this.selectionForNotShow.isSelected(data)).length === 1
    ) {
      let sequence = 0;
      let object = null;
      this.listShow.forEach((item, index) => {
        if (this.selectionForNotShow.isSelected(item)) {
          sequence = index;
          object = item;
          this.listShow.splice(index, 1);
        }
      });
      // if (sequence !== 0) {
      this.listShow.splice(sequence + 1, 0, object);
      for (let i = 0; i < this.listShow.length; i++) {
        this.listShow[i].seq = i + 1;
        // }
      }
    } else if (
      this.listShow.filter((data) => this.selectionForNotShow.isSelected(data)).length > 1
    ) {
      this.snackBar.open('กรุณา เลือกเพียง 1 รายการ', '', {
        panelClass: '_warning',
      });
    }
  }

  clearAllSelected() {
    this.selectionForNotShow.clear();
    this.selectionForShow.clear();
  }

  checkboxLabelForNotShow(row?: any): string {
    return `${this.selectionForNotShow.isSelected(row) ? 'deselect' : 'select'} row ${
      row.position + 1
    }`;
  }

  checkboxLabelForShow(row?: any): string {
    return `${this.selectionForShow.isSelected(row) ? 'deselect' : 'select'} row ${
      row.position + 1
    }`;
  }

  checkboxLabelForGroupSummary(row?: any): string {
    return `${this.selectionForGroup.isSelected(row) ? 'deselect' : 'select'} row ${
      row.position + 1
    }`;
  }

  checkSort(index, sortType) {
    if (this.listShow[index].sortType === sortType) {
      this.listShow[index].sortType = null;
    } else {
      this.listShow[index].sortType = sortType;
    }
  }
}
