import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { PaymentBlockService } from '@core/services/om/payment-block.service';
import { GenerateFileService } from '@core/services/generate-file/generate-file.service';
import { ActivatedRoute } from '@angular/router';
import { DialogResultComponent } from '@shared/component/dialog-result/dialog-result.component';

export interface DialogData {
  generateFileId: string;
}

@Component({
  selector: 'app-dialog-generate-file-result',
  templateUrl: './dialog-generate-file-result.component.html',
  styleUrls: ['./dialog-generate-file-result.component.scss'],
})
export class DialogGenerateFileResultComponent implements OnInit {
  displayedColumns: string[] = ['outputText'];

  dataSourceSmarts = new MatTableDataSource([]);
  dataSourceSwifts = new MatTableDataSource([]);
  dataSourceGiros = new MatTableDataSource([]);
  dataSourceInHouses = new MatTableDataSource([]);
  dataSourceGgiro = new MatTableDataSource([]);
  dataSourceInh = new MatTableDataSource([]);
  dataSourceGM = new MatTableDataSource([]);
  dataSourceErrors = new MatTableDataSource([]);
  referenceDoc = '';
  logId;
  constructor(
    private dialogRef: MatDialogRef<DialogGenerateFileResultComponent>,
    private paymentBlockService: PaymentBlockService,
    private generateFileService: GenerateFileService,
    private activatedRoute: ActivatedRoute,
    private dialog: MatDialog,
    @Inject(MAT_DIALOG_DATA) public data: DialogData
  ) {
    this.logId = this.data.generateFileId;
  }

  ngOnInit() {
    this.logId = this.data.generateFileId;
    this.getLog(this.logId);
  }

  isGroup(index, item): boolean {
    if (item.group) {
      return true;
    } else {
      return false;
    }
  }

  openDialogResultDetail(refDocNumber) {
    const dialogRef = this.dialog.open(DialogResultComponent, {
      disableClose: true,
      data: {
        referenceDoc: refDocNumber,
        type: 'TEXT_RESULT',
      },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.value === 'CONFIRM') {
        // const url = './generate-result/' + this.logId;
        // window.open(url, '_blank');
      } else {
      }
    });
  }

  getLog(logId) {
    this.generateFileService.getLog(this.logId).then((response) => {
      // const response = JSON.parse(sessionStorage.getItem('generateFile' + logId) as any);
      console.log(response);
      if (response.status === 200) {
        const datas = response.data;
        if (datas) {
          this.referenceDoc = datas.reference;
          this.openDialogResultDetail(datas.reference);
          if (datas.smarts && datas.smarts.length > 0) {
            const resultData = this.groupingData(datas.smarts);
            this.dataSourceSmarts = new MatTableDataSource(resultData);
          }
          if (datas.swifts && datas.swifts.length > 0) {
            const resultData = this.groupingData(datas.swifts);
            this.dataSourceSwifts = new MatTableDataSource(resultData);
          }
          if (datas.giros && datas.giros.length > 0) {
            const resultData = this.groupingData(datas.giros);
            this.dataSourceGiros = new MatTableDataSource(resultData);
          }
          if (datas.inhouses && datas.inhouses.length > 0) {
            const resultData = this.groupingData(datas.inhouses);
            this.dataSourceInHouses = new MatTableDataSource(resultData);
          }
          if (datas.ggiro && datas.ggiro.length > 0) {
            const resultData = this.groupingData(datas.ggiro);
            this.dataSourceGgiro = new MatTableDataSource(resultData);
          }
          if (datas.inh && datas.inh.length > 0) {
            const resultData = this.groupingData(datas.inh);
            this.dataSourceInh = new MatTableDataSource(resultData);
          }
          if (datas.gm && datas.gm.length > 0) {
            const resultData = this.groupingData(datas.gm);
            this.dataSourceGM = new MatTableDataSource(resultData);
          }
          if (datas.errors && datas.errors.length > 0) {
            this.dataSourceErrors = new MatTableDataSource(datas.errors);
          }
        }
      } else if (response.status === 202) {
        const dialogRef = this.dialog.open(DialogResultComponent, {
          data: {
            textError: 'ไม่สามารถทำรายการซ้ำได้',
            type: 'ERROR_DIALOG',
          },
        });
        dialogRef.afterClosed().subscribe((result) => {
          window.close();
        });
      } else {
        const dialogRef = this.dialog.open(DialogResultComponent, {
          data: {
            textError: 'เกิดข้อผิดพลาดขึ้น กรุณากลับไปสร้างไฟล์จ่ายอีกครั้ง.',
            type: 'ERROR_DIALOG',
          },
        });
        dialogRef.afterClosed().subscribe((result) => {
          window.close();
        });
      }
    });
  }

  groupingData(list) {
    const resultData = [];
    const groupToValues = list.reduce((obj, item) => {
      obj[item.fileName] = obj[item.fileName] || [];
      obj[item.fileName].push(item.outputText);
      return obj;
    }, {});
    if (groupToValues) {
      let items: any = [];
      Object.entries(groupToValues).map(([fileName, values]) => {
        resultData.push({ group: fileName });
        items = values;
        items.forEach((item) => {
          resultData.push({
            outputText: item,
          });
        });
      });
    }
    return resultData;
  }

  closeDialog() {
    this.dialogRef.close({
      event: true,
      status: 'Close',
      value: '',
    });
  }
}
