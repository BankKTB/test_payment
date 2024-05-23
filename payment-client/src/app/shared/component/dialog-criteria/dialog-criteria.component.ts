import { Component, ElementRef, Inject, OnInit, QueryList, ViewChildren } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { Constant } from '@shared/utils/constant';
import { Utils } from '@shared/utils/utils';
export interface DialogData {
  listCriteria: [{ from: null; to: null; optionExclude: false }]; // List filter variant
  keyColumn: string; // Key column that map to service columnName
  typeInput: string; // Type of Input (eg. (STRING, TEXT),NUMBER, DATE, CURRENCY, YEAR)
  typeParam: string;
  min: number; // MIN of Input
  max: number; // MAX of Input
  thaiText: string;
  enableExclude?: boolean;
}
@Component({
  selector: 'app-dialog-criteria',
  templateUrl: './dialog-criteria.component.html',
  styleUrls: ['./dialog-criteria.component.scss'],
})
export class DialogCriteriaComponent implements OnInit {
  @ViewChildren('from') from: QueryList<ElementRef>;
  @ViewChildren('to') to: QueryList<ElementRef>;
  @ViewChildren('optionExclude') optionExclude: QueryList<ElementRef>;

  listCriteriaRange = [
    {
      from: null,
      to: null,
      optionExclude: false,
      typeParam: 'LIST',
      isDelete: false,
      typeInput: 'STRING',
    },
    {
      from: null,
      to: null,
      optionExclude: false,
      typeParam: 'LIST',
      isDelete: false,
      typeInput: 'STRING',
    },
    {
      from: null,
      to: null,
      optionExclude: false,
      typeParam: 'LIST',
      isDelete: false,
      typeInput: 'STRING',
    },
  ];
  constructor(
    public dialogRef: MatDialogRef<DialogCriteriaComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    public constant: Constant,
    private router: Router,
    private utils: Utils
  ) {
    dialogRef.backdropClick().subscribe(() => {});
  }

  ngOnInit() {
    if (this.data.typeInput) {
      this.data.typeInput = this.data.typeInput.toUpperCase();
    }
    if (this.data.listCriteria) {
      const listCriteria = this.data.listCriteria.filter((el) => {
        return el.from !== null;
      });
      for (let i = 0; i < listCriteria.length; i++) {
        let from;
        let to;
        if (this.data.typeInput === 'DATE') {
          from = !this.utils.isEmpty(listCriteria[i].from) ? new Date(listCriteria[i].from) : null;
          to = !this.utils.isEmpty(listCriteria[i].to) ? new Date(listCriteria[i].to) : null;
          // from = !this.utils.isEmpty(listCriteria[i].from) ? '03.08.2021' : null;
          // to = !this.utils.isEmpty(listCriteria[i].to) ? listCriteria[i].to : null;
        } else if (this.data.typeInput === 'YEAR') {
          from = this.utils.isEmpty(listCriteria[i].from)
            ? this.utils.convertYearToBuddhist(listCriteria[i].from)
            : null;
          to = this.utils.isEmpty(listCriteria[i].to)
            ? this.utils.convertYearToBuddhist(listCriteria[i].to)
            : null;
        } else {
          from = listCriteria[i].from;
          to = listCriteria[i].to;
        }
        if (this.listCriteriaRange[i]) {
          this.listCriteriaRange[i] = {
            from,
            to,
            optionExclude: listCriteria[i].optionExclude,
            typeParam: this.data.typeParam,
            isDelete: false,
            typeInput: 'STRING',
          };
        } else {
          this.listCriteriaRange.push({
            from,
            to,
            optionExclude: listCriteria[i].optionExclude,
            typeParam: this.data.typeParam,
            isDelete: false,
            typeInput: 'STRING',
          });
        }
      }
    }
  }

  clickSave() {
    const objResult = [];
    this.listCriteriaRange.forEach((item) => {
      if (item.from !== null) {
        if (!item.isDelete) {
          let from = '';
          let to = '';
          if (this.data.typeInput === 'DATE') {
            if (!this.utils.isEmpty(item.from)) {
              from = this.utils.parseDate(
                item.from.getDate(),
                item.from.getMonth() + 1,
                item.from.getFullYear()
              );
            }
            if (!this.utils.isEmpty(item.to)) {
              to = this.utils.parseDate(
                item.to.getDate(),
                item.to.getMonth() + 1,
                item.to.getFullYear()
              );
            }
          } else {
            from = item.from;
            to = item.to;
          }
          objResult.push({
            from,
            to,
            optionExclude: item.optionExclude,
          });
        }
      }
    });
    this.dialogRef.close({
      event: true,
      status: 'save',
      value: objResult,
      key: this.data.keyColumn,
      typeInput: this.data.typeInput,
    });
    // if (this.data.typeParam === 'STRING') {
    //   const dataFirst = this.listCriteriaRange.filter(function (el) {
    //     return el.from !== null;
    //   });
    //   if (dataFirst[0]) {
    //     this.dialogRef.close({
    //       event: true,
    //       status: 'save',
    //       value: dataFirst[0].from,
    //       key: this.data.keyColumn,
    //       typeInput: this.data.typeInput,
    //     });
    //   }
    // } else {
    //   this.dialogRef.close({
    //     event: true,
    //     status: 'save',
    //     value: this.listCriteriaRange.filter(function (el) {
    //       return el.from !== null;
    //     }),
    //     key: this.data.keyColumn,
    //     typeInput: this.data.typeInput,
    //   });
    // }
  }

  addRow() {
    this.listCriteriaRange.push({
      from: null,
      to: null,
      optionExclude: false,
      typeParam: this.data.typeParam,
      isDelete: false,
      typeInput: 'STRING',
    });
  }

  onDateChange(event, index, name) {
    const dateValue = event.value;
    if (name === 'from') {
      this.listCriteriaRange[index].from = dateValue;
    } else {
      this.listCriteriaRange[index].to = dateValue;
    }
  }

  onBlurSetValue(index) {
    const from = this.from.toArray()[index].nativeElement.value;
    const to = this.to.toArray()[index].nativeElement.value;
    if (from) {
      this.listCriteriaRange[index].from = from;
    } else {
      this.listCriteriaRange[index].from = null;
    }
    if (to) {
      this.listCriteriaRange[index].to = to;
    } else {
      this.listCriteriaRange[index].to = null;
    }
  }

  setOptionInclude(index) {
    const valueIndex = this.optionExclude.toArray()[index] as any;
    const optionExclude = valueIndex._checked;
    if (optionExclude) {
      this.listCriteriaRange[index].optionExclude = optionExclude;
    } else {
      this.listCriteriaRange[index].optionExclude = false;
    }
  }

  deleteRow(index) {
    this.listCriteriaRange[index].isDelete = true;
    // this.listCriteriaRange.splice(index, 1);
  }

  onNoClick() {
    this.dialogRef.close({
      event: true,
      status: 'Close',
      value: '',
    });
  }

  copyFromExcel(event, index, type) {
    const value = event.data;
    if (value && value.length > 10) {
      const list = value.split('\n');
      if (list.length > this.listCriteriaRange.length) {
        const need = list.length - this.listCriteriaRange.length;
        for (let i = 0; i < need; i++) {
          this.listCriteriaRange.push({
            from: null,
            to: null,
            optionExclude: false,
            typeParam: 'LIST',
            isDelete: false,
            typeInput: 'STRING',
          });
        }
      }
      if (type === 'from') {
        for (let i = index; i < list.length; i++) {
          this.listCriteriaRange[i].from = list[i];
        }
      } else if (type === 'to') {
        for (let i = index; i < list.length; i++) {
          this.listCriteriaRange[i].to = list[i];
        }
      }
    }
  }

  clearDate(index, type) {
    if (type === 'from') {
      this.listCriteriaRange[index].from = null;
    } else {
      this.listCriteriaRange[index].to = null;
    }
  }

  copyToClipboard(event) {
    // if (window['clipboardData']) {
    //   //FOR IE
    //   let value = window['clipboardData'].getData('Text');
    // } else {
    //   // for other navigators
    //   navigator['clipboard'].readText().then((clipText) => {
    //     if (clipText) {
    //       const listClipBoard = clipText.split('\n');
    //       if (listClipBoard && listClipBoard.length > 0) {
    //         this.listCriteriaRange = this.listCriteriaRange.filter(function (el) {
    //           return el.from !== null;
    //         });
    //         listClipBoard.forEach((value) => {
    //           if (value !== '') {
    //             this.listCriteriaRange.push({
    //               from: value,
    //               to: null,
    //               optionExclude: false,
    //               typeParam: this.data.typeParam,
    //             });
    //           }
    //         });
    //       }
    //     }
    //   });
    // }
  }

  showAmountList() {
    return this.listCriteriaRange.filter((el) => {
      return el.from !== null;
    }).length;
  }
}
