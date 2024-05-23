import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material';
import { Constant } from '@shared/utils/constant';
import { PaymentBlockService } from '@core/services/om/payment-block.service';
import { DialogDetailDocumentColumnTableComponent } from '@shared/component/dialog-detail-document-column-table/dialog-detail-document-column-table.component';
import { LoaderService } from '@core/services';

export interface DialogData {
  page: any;

  headerTable: [];
  document: any;
}

@Component({
  selector: 'app-dialog-detail-document',
  templateUrl: './dialog-detail-document.component.html',
  styleUrls: ['./dialog-detail-document.component.scss'],
})
export class DialogDetailDocumentComponent implements OnInit {
  resultSave = null;
  columnTable = [{ key: null, columnName: null, showColumn: null, seq: null }];

  isSaveSuccess = false;
  allPage: any;
  pathPage: string;
  createPage: string;
  searchPage: string;
  backListPage: string;
  editPage: string;

  dataSource = new MatTableDataSource([]);
  header = null;

  listMessageResponse = [];

  p = 1;

  documentDetail;

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  constructor(
    public dialogRef: MatDialogRef<DialogDetailDocumentComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    public constant: Constant,
    private router: Router,
    private dialog: MatDialog,
    private paymentBlockService: PaymentBlockService,
    private loaderService: LoaderService
  ) {
    dialogRef.backdropClick().subscribe(() => {
      console.log('close dialog');
      // ส่ง event param ตาม ปกติเลย
      if (this.isSaveSuccess && this.resultSave.accDocNo) {
        this.dialogRef.close({ event: 'savesucess' });
      } else {
        this.dialogRef.close({ event: 'close' });
      }
    });
  }

  ngOnInit() {
    // setTimeout(() => {
    this.columnTable = [];
    this.columnTable.push({
      key: 'list',
      columnName: 'รายการ',
      showColumn: true,
      seq: 0,
    });

    for (const key of this.constant.LIST_TABLE_DETAIL_DOCUMENT.keys()) {
      const result = this.constant.LIST_TABLE_DETAIL_DOCUMENT.get(key);
      this.columnTable.push({
        key,
        columnName: result.name,
        showColumn: true,
        seq: result.seq,
      });
    }

    // }, 1000); // Execute something() 1 second later.
    this.dataSource.sort = this.sort;

    // this.allPage = this.data.page;
    this.documentDetail = this.data.document;
    // this.searchDetail(this.documentDetail);
    // this.findPage();
    // this.save(0, this.data.head, this.data.item);
  }

  // findPage() {
  //   if (this.allPage.size > 0) {
  //     for (const [key, value] of this.allPage) {
  //       if (key === 'path') {
  //         this.pathPage = value;
  //       } else if (key === 'create') {
  //         this.createPage = value;
  //       } else if (key === 'search') {
  //         this.searchPage = value;
  //       } else if (key === 'backList') {
  //         this.backListPage = value;
  //       } else if (key === 'edit') {
  //         this.editPage = value;
  //       }
  //     }
  //   }
  // }

  // searchDetail(document) {
  //   this.loaderService.loadingToggleStatus(true);
  //   this.paymentBlockService
  //     .searchDetail(document.compCode, document.accDocNo, document.fiscalYear)
  //     .then((data) => {
  //       this.loaderService.loadingToggleStatus(false);
  //       const response = data as any;
  //       const result = response.data;
  //       console.log(result);
  //       if (result) {
  //         this.header = result.header;
  //         this.dataSource = new MatTableDataSource(result.items);
  //         this.dataSource.sort = this.sort;
  //       } else {
  //         this.listMessageResponse.push('ไม่พบเอกสาร');
  //       }
  //     });
  // }

  onNoClick(): void {
    this.dialogRef.close({});
  }

  openDialogDetailDocumentColumnTable(): void {
    const dialogRef = this.dialog.open(DialogDetailDocumentColumnTableComponent, {
      data: { listcolumn: this.columnTable },
    });
    dialogRef.afterClosed().subscribe((result) => {
      console.log(result);
      if (result && result.event) {
        if (result.status === 'Save') {
          this.columnTable = result.value;
          this.getDisplayedColumns();
        }
      }
      console.log(result);
    });
  }

  getDisplayedColumns() {
    this.columnTable.sort((a, b) => (a.seq > b.seq ? 1 : -1));
    return this.columnTable.filter((cd) => cd.showColumn).map((cd) => cd.key);
  }
}
