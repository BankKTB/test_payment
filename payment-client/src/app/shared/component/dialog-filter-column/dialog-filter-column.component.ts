import { Component, OnInit, ViewChild, Inject } from '@angular/core';
import { MatSort } from '@angular/material/sort';
import { SelectionModel } from '@angular/cdk/collections';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { DialogCriteriaComponent } from '@shared/component/dialog-criteria/dialog-criteria.component';
import { Constant } from '@shared/utils/constant';
import { Utils } from '@shared/utils/utils';
export interface DialogData {
  listAllColumn: any;
  filteredColumn: any;
}
@Component({
  selector: 'app-dialog-filter-column',
  templateUrl: './dialog-filter-column.component.html',
  styleUrls: ['./dialog-filter-column.component.scss'],
})
export class DialogFilterColumnComponent implements OnInit {
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  selectionForNotShow = new SelectionModel<any>(true, []);
  selectionForShow = new SelectionModel<any>(true, []);
  selectionForGroup = new SelectionModel<any>(true, []);
  listShow = [];
  listNotShow = [];
  listFiltered = [];
  listGroup = [];
  criteriaObj = { from: null, to: null, optionExclude: false };

  constructor(
    private dialogRef: MatDialogRef<DialogFilterColumnComponent>,
    private snackBar: MatSnackBar,
    private dialog: MatDialog,
    private utils: Utils,
    public constant: Constant,
    @Inject(MAT_DIALOG_DATA) public data: DialogData
  ) {}

  ngOnInit() {
    console.log(this.data.listAllColumn);
    console.log(this.data.filteredColumn);
    this.data.listAllColumn.forEach((item) => {
      const obj = {
        key: item.key,
        text: item.text,
      };
      const itemSorted = this.data.filteredColumn.find((e) => e.id === item.key);
      if (itemSorted) {
        this.listShow.push(obj);
        this.listFiltered.push(itemSorted);
      } else {
        this.listNotShow.push(obj);
      }
    });
  }

  clickSave() {
    console.log(this.listFiltered);
    this.dialogRef.close({
      event: true,
      status: 'Save',
      value: this.listFiltered,
    });
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
          // if (index !== 0) {
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

  notShowAll() {
    this.listShow.forEach((item) => {
      this.listNotShow.push(item);
    });
    this.listShow = [];
  }

  showAll() {
    this.listNotShow.forEach((item) => {
      this.listShow.push(item);
    });
    this.listNotShow = [];
  }

  closeDialog() {
    this.dialogRef.close({
      event: true,
      status: 'Close',
      value: '',
    });
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
  }

  notShowSelected() {
    this.listShow.forEach((item) => {
      if (this.selectionForNotShow.isSelected(item)) {
        this.listNotShow.push(item);
        this.listShow = this.listShow.filter((data) => data.key !== item.key);
      }
    });
    this.listGroup = this.listGroup.filter((n) => {
      return this.listShow.some((b) => n.key === b.key);
    });
    for (let i = 0; i < this.listShow.length; i++) {
      this.listShow[i].seq = i + 1;
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

  openDialogCriteria(columnKey, typeInput, thaiText) {
    console.log(this.listFiltered);
    const foundPrevious = this.listFiltered.find((element) => {
      if (element && element.id === columnKey) {
        return true;
      }
    });
    console.log(foundPrevious);
    const dialogRef = this.dialog.open(DialogCriteriaComponent, {
      width: '60vw',
      data: {
        listCriteria: foundPrevious ? foundPrevious.value : [],
        typeParam: 'LIST',
        keyColumn: columnKey,
        typeInput: 'STRING',
        thaiText,
        enableExclude: true,
      },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event) {
        if (result.status === 'save') {
          console.log(result);
          if (result.value.length > 0) {
            const obj = {
              id: result.key,
              value: result.value,
            };
            this.listFiltered.push(obj);
          }
        }
      }
    });
  }

  checkHaveVariantValue(key) {
    const foundPrevious = this.listFiltered.find((element) => {
      if (element && element.id === key) {
        return true;
      } else {
        return false;
      }
    });
    return foundPrevious;
  }
}
