import { Component, ElementRef, Inject, OnInit, QueryList, ViewChildren } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Constant } from '@shared/utils/constant';
import { Router } from '@angular/router';
import { DialogSearchMasterDataComponent } from '@shared/component/dialog-search-master-data/dialog-search-master-data.component';

export interface DialogData {
  listFiArea: [{ from: null; to: null; optionExclude: false }];
}
@Component({
  selector: 'app-dialog-om-fi-area',
  templateUrl: './dialog-om-fi-area.component.html',
  styleUrls: ['./dialog-om-fi-area.component.scss'],
})
export class DialogOmFiAreaComponent implements OnInit {
  @ViewChildren('from') from: QueryList<ElementRef>;
  @ViewChildren('to') to: QueryList<ElementRef>;
  @ViewChildren('optionExclude') optionExclude: QueryList<ElementRef>;

  listFiArea = [
    { from: null, to: null, optionExclude: false },
    { from: null, to: null, optionExclude: false },
    { from: null, to: null, optionExclude: false },
  ];
  listMessageResponse = [];

  constructor(
    public dialogRef: MatDialogRef<DialogOmFiAreaComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    public constant: Constant,
    private router: Router,
    private dialog: MatDialog
  ) {
    dialogRef.backdropClick().subscribe(() => {
      console.log('close dialog');
      // ส่ง event param ตาม ปกติเลย
      // if (this.isSaveSuccess && this.resultSave.accDocNo) {
      //   this.dialogRef.close({event: 'savesucess'});
      // } else {
      //   this.dialogRef.close({event: 'close'});
      // }
    });
  }

  ngOnInit() {
    console.log(this.data.listFiArea);
    if (this.data.listFiArea[0].from) {
      this.listFiArea = this.data.listFiArea;
    }
  }

  addInputCompany() {
    this.listFiArea.push({
      from: null,
      to: null,
      optionExclude: false,
    });
  }

  setFiArea(index) {
    const fiAreaFrom = this.from.toArray()[index].nativeElement.value;
    const fiAreaTo = this.to.toArray()[index].nativeElement.value;
    if (fiAreaFrom) {
      this.listFiArea[index].from = fiAreaFrom;
    } else {
      this.listFiArea[index].from = '';
    }
    if (fiAreaTo) {
      this.listFiArea[index].to = fiAreaTo;
    } else {
      this.listFiArea[index].to = '';
    }
  }

  setOptionInclude(index) {
    const valueIndex = this.optionExclude.toArray()[index] as any;

    const optionExclude = valueIndex._checked;
    if (optionExclude) {
      this.listFiArea[index].optionExclude = optionExclude;
    } else {
      this.listFiArea[index].optionExclude = false;
    }
  }

  deleteInputFiArea(index) {
    if (this.listFiArea.length > 1) {
      this.listFiArea.splice(index, 1);
    }
  }

  onNoClick() {
    this.dialogRef.close({
      event: true,
      status: 'Close',
      value: '',
    });
  }

  copyFromExcel(event, index, type) {
    // console.log(event);

    const value = event.data;

    // console.log('value' + value);

    if (value.length > 10) {
      // console.log('index' + index);

      console.log(value.split('\n'));
      const list = value.split('\n');

      if (list.length > this.listFiArea.length) {
        const need = list.length - this.listFiArea.length;
        for (let i = 0; i < need; i++) {
          this.listFiArea.push({
            from: null,
            to: null,
            optionExclude: false,
          });
        }
      }
      console.log(this.listFiArea);
      if (type === 'fiAreaFrom') {
        for (let i = index; i < list.length; i++) {
          this.listFiArea[i].from = list[i];
        }
      } else if (type === 'fiAreaTo') {
        for (let i = index; i < list.length; i++) {
          this.listFiArea[i].to = list[i];
        }
      }
    }
  }

  openDialogSearchFiArea(index, type): void {
    const dialog = this.dialog.open(DialogSearchMasterDataComponent, {
      data: { type },
    });
    dialog.afterClosed().subscribe((result) => {
      if (result && result.event) {
        if (type === 'fiAreaFrom') {
          this.listFiArea[index].from = result.value;
        } else if (type === 'fiAreaTo') {
          this.listFiArea[index].to = result.value;
        }
      }
    });
  }

  clickSave() {
    this.dialogRef.close({
      event: true,
      status: 'save',
      value: this.listFiArea,
    });
  }
}
