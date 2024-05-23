import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { WebInfo } from '@core/models/web-info';
import { Constant } from '@shared/utils/constant';

export interface DialogData {
  page: any;
  head: any;
  item: [];
  headerTable: [];
  webInfo: {};
}

@Component({
  selector: 'app-dialog-po-change-history',
  templateUrl: './dialog-po-change-history.component.html',
  styleUrls: ['./dialog-po-change-history.component.scss'],
})
export class DialogPoChangeHistoryComponent implements OnInit {
  listDocument = [];
  listBackupDocument = [];
  listPaymentBlock = [
    { id: 'R', name: 'R - CGD ไม่อนุมัติ' },
    { id: ' ', name: '  - ชำระเงินได้' },
    { id: 'V', name: 'V - หักล้างการชำระเงิน' },
    { id: '*', name: '* - ข้ามบัญชี' },
    { id: 'N', name: 'N - ไม่อนุมัติภายใน สรก.' },
    { id: 'E', name: 'E - PTO ไม่อนุมัติ' },
    { id: 'B', name: 'B - ระงับการชำระ' },
    { id: 'P', name: 'P - รอ CGD อนุมัติ' },
    { id: '0', name: '0 - รออนุมัติขั้น1ในสรก.' },
    { id: 'A', name: 'A - รออนุมัติขั้น2ในสรก.' },
    { id: 'F', name: 'F - เลขที่บัญชีผิดพลาด' },
  ];
  webInfo: WebInfo;

  constructor(
    public dialogRef: MatDialogRef<DialogPoChangeHistoryComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    public constant: Constant
  ) {
    dialogRef.disableClose = true;
  }

  ngOnInit() {
    window.scrollTo(0, 0);
    this.setInputfromSelect(this.data.item);
  }

  setInputfromSelect(value) {
    this.listDocument = value;
  }

  onNoClick(): void {
    this.dialogRef.close({ event: 'save' });
    this.dialogRef.close({ event: 'cancel' });
  }
}
