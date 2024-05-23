import { Component, Inject, OnInit } from '@angular/core';
import { WebInfo } from '@core/models/web-info';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Constant } from '@shared/utils/constant';
import { FiService } from '@core/services/fi/fi.service';

export interface DialogData {
  page: any;
  head: any;
  item: [];
  headerTable: [];
  webInfo: {};
  payload: any;
}

@Component({
  selector: 'app-dialog-om-search-detail',
  templateUrl: './dialog-om-search-detail.component.html',
  styleUrls: ['./dialog-om-search-detail.component.scss'],
})
export class DialogOmSearchDetailComponent implements OnInit {
  listDocument = [];
  listBackupDocument = [];
  listPaymentBlock = [
    { id: 'R', name: 'R - CGD ไม่อนุมัติ' },
    { id: ' ', name: '  - ชำระเงินได้' },
    { id: 'V', name: 'V - หักล้างการชำระเงิน' },
    { id: '*', name: '* - ข้ามบัญชี' },
    { id: 'N', name: 'N - ไม่อนุมัติภายใน สรก.' },
    { id: 'E', name: 'E - PTO ไม่อนุมัติ' },
    { id: 'B', name: 'B - ระงับการชำระเงิน' },
    { id: 'P', name: 'P - รอ CGD อนุมัติ' },
    { id: '0', name: '0 - รออนุมัติขั้น1ในสรก.' },
    { id: 'A', name: 'A - รออนุมัติขั้น2ในสรก.' },
    { id: 'F', name: 'F - เลขที่บัญชีผิดพลาด' },
  ];
  webInfo: WebInfo;
  isLoading = false;

  constructor(
    public dialogRef: MatDialogRef<DialogOmSearchDetailComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    public constant: Constant,
    private fiService: FiService
  ) {
    this.dialogRef.disableClose = true;
  }

  ngOnInit() {
    window.scrollTo(0, 0);
    this.loadDetail();
    console.log('DialogShowOmComponent');
    console.log(this.data);
  }

  loadDetail() {
    this.fiService.paymentBlockDetail(this.data.payload).then((data) => {
      console.log(data);
      const response = data as any;
      if (response.status === 200) {
        this.checkNull(response.data);
      }
    });
  }

  checkNull(value) {
    if (value.changeLog) {
      this.listDocument = value.changeLog;
      this.listDocument.forEach((element) => {
        const objectOld = this.listPaymentBlock.find((item) => item.id === element.valueOld);
        element.valueOldName = objectOld.name;
        const objectNew = this.listPaymentBlock.find((item) => item.id === element.valueNew);
        element.valueNewName = objectNew.name;
      });
    }
    if (value.followingDoc) {
      this.listBackupDocument = value.followingDoc;
    }
  }

  onNoClick(): void {
    this.dialogRef.close({ event: 'save' });
    this.dialogRef.close({ event: 'cancel' });
  }
}
