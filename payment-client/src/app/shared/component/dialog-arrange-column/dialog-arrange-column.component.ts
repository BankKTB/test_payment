import { Component, OnInit, ViewChild, Inject } from '@angular/core';
import { MatSort } from '@angular/material/sort';
import { SelectionModel } from '@angular/cdk/collections';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
export interface DialogData {
  listColumnShow: any;
}
@Component({
  selector: 'app-dialog-arrange-column',
  templateUrl: './dialog-arrange-column.component.html',
  styleUrls: ['./dialog-arrange-column.component.scss'],
})
export class DialogArrangeColumnComponent implements OnInit {
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  selectionForNotShow = new SelectionModel<any>(true, []);
  selectionForShow = new SelectionModel<any>(true, []);
  selectionForGroup = new SelectionModel<any>(true, []);
  listShow = [];
  listNotShow = [];

  constructor(
    private dialogRef: MatDialogRef<DialogArrangeColumnComponent>,
    private snackBar: MatSnackBar,
    @Inject(MAT_DIALOG_DATA) public data: DialogData
  ) {}

  ngOnInit() {
    console.log(this.data.listColumnShow);
    this.data.listColumnShow.forEach((item) => {
      if (item.key) {
        if (item.hide) {
          this.listNotShow.push(item);
        } else {
          this.listShow.push(item);
        }
      }
    });
    this.initSequence();
  }

  initSequence() {
    this.listShow.forEach((value, index) => {
      this.listShow[index].sequence = index + 1;
    });
  }

  clickSave() {
    const newlistColumn = [];
    let seq = 1;
    if (this.listShow.length > 0) {
      this.listShow.forEach((item) => {
        item.hide = false;
        item.sequence = seq++;
        newlistColumn.push(item);
      });
    }
    if (this.listNotShow.length > 0) {
      this.listNotShow.forEach((item) => {
        item.hide = true;
        item.sequence = seq++;
        newlistColumn.push(item);
      });
    }
    console.log(newlistColumn);
    // this.listShow.sort((a, b) => (a.seq > b.seq ? 1 : -1));
    // this.listNotShow.sort((a, b) => (a.seq > b.seq ? 1 : -1));
    this.dialogRef.close({
      event: true,
      status: 'Save',
      columnsDisplay: newlistColumn,
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
  }

  showAll() {
    this.listNotShow.forEach((item, index) => {
      this.listShow.push(item);
    });
    this.listNotShow = [];
    this.initSequence();
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
      this.listShow[i].sequence = i + 1;
    }
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
      this.listShow[i].sequence = i + 1;
    }
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
          this.listShow[i].sequence = i + 1;
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
        this.listShow[i].sequence = i + 1;
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

  checkSort(data, sortType) {
    data.sortType = sortType;
  }
}
