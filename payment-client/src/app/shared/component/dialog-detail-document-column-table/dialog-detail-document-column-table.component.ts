import { AfterViewInit, Component, Inject, OnInit } from '@angular/core';
import { SelectionModel } from '@angular/cdk/collections';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Constant } from '@shared/utils/constant';
import { MatSnackBar } from '@angular/material/snack-bar';
import { LoaderService } from '@core/services';

export interface DialogData {
  listcolumn: any;
}

@Component({
  selector: 'app-dialog-detail-document-column-table',
  templateUrl: './dialog-detail-document-column-table.component.html',
  styleUrls: ['./dialog-detail-document-column-table.component.scss'],
})
export class DialogDetailDocumentColumnTableComponent implements OnInit, AfterViewInit {
  selectionForNotShow = new SelectionModel<any>(true, []);
  selectionForShow = new SelectionModel<any>(true, []);

  items = [];

  basket = [];

  listShow = [];
  listNotShow = [];

  constructor(
    private dialogRef: MatDialogRef<DialogDetailDocumentColumnTableComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    public constant: Constant,
    private snackBar: MatSnackBar,
    private loaderService: LoaderService
  ) {
    dialogRef.disableClose = false;
    dialogRef.backdropClick().subscribe(() => {
      this.dialogRef.close({ event: false });
    });
  }

  ngOnInit() {}

  ngAfterViewInit() {
    setTimeout(() => {
      if (this.data.listcolumn.length > 0) {
        let sequence = 1;
        this.data.listcolumn.forEach((item) => {
          if (item.showColumn) {
            item.seq = sequence++;
            this.items.push(item);
          } else {
            this.basket.push(item);
          }
        });
      }
    }, 200); // Execute something() 1 second later.
  }

  clickSave() {
    const newlistcolumn = [];
    let seq = 0;
    if (this.items.length > 0) {
      this.items.forEach((item) => {
        item.showColumn = true;
        item.seq = seq++;
        newlistcolumn.push(item);
      });
    }
    if (this.basket.length > 0) {
      this.basket.forEach((item) => {
        item.showColumn = false;
        item.seq = seq++;
        newlistcolumn.push(item);
      });
    }
    newlistcolumn.sort((a, b) => (a.seq > b.seq ? 1 : -1));
    console.log(newlistcolumn);
    this.dialogRef.close({
      event: true,
      status: 'Save',
      value: newlistcolumn,
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
    this.items.forEach((item) => {
      this.basket.push(item);
    });
    this.items = [];
  }

  showAll() {
    this.basket.forEach((item) => {
      this.items.push(item);
    });
    this.basket = [];
  }

  showSelected() {
    let sequence = 0;
    for (let i = 0; i < this.items.length; i++) {
      if (this.selectionForNotShow.isSelected(this.items[i])) {
        sequence = i;
        break;
      } else {
        sequence = this.items.length;
      }
    }
    this.basket.forEach((item) => {
      if (this.selectionForShow.isSelected(item)) {
        // this.items.push(item);
        this.items.splice(sequence, 0, item);
        this.basket = this.basket.filter((data) => data.key !== item.key);
      }
    });

    for (let i = 0; i < this.items.length; i++) {
      this.items[i].seq = i + 1;
    }
  }

  notShowSelected() {
    this.items.forEach((item) => {
      if (this.selectionForNotShow.isSelected(item)) {
        this.basket.push(item);
        this.items = this.items.filter((data) => data.key !== item.key);
      }
    });
  }

  up() {
    if (this.items.filter((data) => this.selectionForNotShow.isSelected(data)).length === 0) {
      this.snackBar.open('กรุณา เลือก รายการ', '', {
        panelClass: '_warning',
      });
    } else if (
      this.items.filter((data) => this.selectionForNotShow.isSelected(data)).length === 1
    ) {
      let sequence = 0;
      let object = null;
      this.items.forEach((item, index) => {
        if (this.selectionForNotShow.isSelected(item)) {
          if (index !== 0) {
            sequence = index;
            object = item;
            this.items.splice(index, 1);
          }
        }
      });
      if (sequence !== 0) {
        this.items.splice(sequence - 1, 0, object);
        for (let i = 0; i < this.items.length; i++) {
          this.items[i].seq = i + 1;
        }
      }
    } else if (this.items.filter((data) => this.selectionForNotShow.isSelected(data)).length > 1) {
      this.snackBar.open('กรุณา เลือกเพียง 1 รายการ', '', {
        panelClass: '_warning',
      });
    }
  }

  down() {
    if (this.items.filter((data) => this.selectionForNotShow.isSelected(data)).length === 0) {
      this.snackBar.open('กรุณา เลือก รายการ', '', {
        panelClass: '_warning',
      });
    } else if (
      this.items.filter((data) => this.selectionForNotShow.isSelected(data)).length === 1
    ) {
      let sequence = 0;
      let object = null;
      this.items.forEach((item, index) => {
        if (this.selectionForNotShow.isSelected(item)) {
          if (index !== 0) {
            sequence = index;
            object = item;
            this.items.splice(index, 1);
          }
        }
      });
      if (sequence !== 0) {
        this.items.splice(sequence + 1, 0, object);
        for (let i = 0; i < this.items.length; i++) {
          this.items[i].seq = i + 1;
        }
      }
    } else if (this.items.filter((data) => this.selectionForNotShow.isSelected(data)).length > 1) {
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
}
