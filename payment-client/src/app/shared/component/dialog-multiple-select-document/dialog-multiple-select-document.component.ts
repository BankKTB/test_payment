import { Component, ElementRef, Inject, OnInit, QueryList, ViewChildren } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Constant } from '@shared/utils/constant';
import { Router } from '@angular/router';

export interface DialogData {
  list: [{ from: null; to: null; optionExclude: false }];
}

@Component({
  selector: 'app-dialog-multiple-select-document',
  templateUrl: './dialog-multiple-select-document.component.html',
  styleUrls: ['./dialog-multiple-select-document.component.scss'],
})
export class DialogMultipleSelectDocumentComponent implements OnInit {
  @ViewChildren('from') from: QueryList<ElementRef>;
  @ViewChildren('to') to: QueryList<ElementRef>;
  @ViewChildren('optionExclude') optionExclude: QueryList<ElementRef>;

  list = [
    { from: null, to: null, optionExclude: false },
    { from: null, to: null, optionExclude: false },
    { from: null, to: null, optionExclude: false },
  ];
  listMessageResponse = [];

  constructor(
    public dialogRef: MatDialogRef<DialogMultipleSelectDocumentComponent>,
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
    console.log(this.data.list);
    if (this.data.list[0].from) {
      this.list = this.data.list;
    }
  }

  addInputCompany() {
    this.list.push({
      from: null,
      to: null,
      optionExclude: false,
    });
  }

  setData(index) {
    const from = this.from.toArray()[index].nativeElement.value;
    const to = this.to.toArray()[index].nativeElement.value;
    if (from) {
      this.list[index].from = from;
    } else {
      this.list[index].from = '';
    }
    if (to) {
      this.list[index].to = to;
    } else {
      this.list[index].to = '';
    }
  }

  setOptionInclude(index) {
    const from = this.from.toArray()[index].nativeElement.value;
    if (from) {
      const valueIndex = this.optionExclude.toArray()[index] as any;

      const optionExclude = valueIndex._checked;
      if (optionExclude) {
        this.list[index].optionExclude = optionExclude;
      } else {
        this.list[index].optionExclude = false;
      }
    }
  }

  deleteInputSpecialType(index) {
    if (this.list.length > 1) {
      this.list.splice(index, 1);
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

      if (list.length > this.list.length) {
        const need = list.length - this.list.length;
        for (let i = 0; i < need; i++) {
          this.list.push({
            from: null,
            to: null,
            optionExclude: false,
          });
        }
      }
      console.log(this.list);
      if (type === 'from') {
        for (let i = index; i < list.length; i++) {
          this.list[i].from = list[i];
        }
      } else if (type === 'to') {
        for (let i = index; i < list.length; i++) {
          this.list[i].to = list[i];
        }
      }
    }
  }

  clickSave() {
    this.dialogRef.close({
      event: true,
      status: 'save',
      value: this.list,
    });
  }
}
