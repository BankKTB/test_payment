import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { MatSort } from '@angular/material/sort';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Constant } from '@shared/utils/constant';
import { Router } from '@angular/router';
import { PaymentBlockService } from '@core/services/om/payment-block.service';
import { FiService } from '@core/services/fi/fi.service';
import { LocalStorageService } from '@core/services';
import { DialogOmSearchDetailComponent } from '@shared/component/dialog-om-search-detail/dialog-om-search-detail.component';

export interface DialogData {
  items: any;
}

@Component({
  selector: 'app-dialog-show-document-reference',
  templateUrl: './dialog-show-document-reference.component.html',
  styleUrls: ['./dialog-show-document-reference.component.scss'],
})
export class DialogShowDocumentReferenceComponent implements OnInit {
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  dataSource = new MatTableDataSource([]);
  header = null;
  displayedColumns: string[] = [
    // 'choose',
    'docType',
    'compCode',
    'accDocNo',
    'fiscalYear',
    'headerReference',
    'dateDoc',
    'dateAcct',
    'vendorName',
    'amount',
    'paymentMethod',
    'clearingDocNo',
    'clearingFiscalYear',
    'clearingDateAcct',
  ];

  constructor(
    public dialogRef: MatDialogRef<DialogShowDocumentReferenceComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    public constant: Constant,
    private router: Router,
    private dialog: MatDialog,
    private paymentBlockService: PaymentBlockService,
    private fiService: FiService,
    private localStorageService: LocalStorageService
  ) {
    this.dialogRef.disableClose = true;
    // dialogRef.backdropClick().subscribe(() => {
    //   this.dialogRef.close({ event: 'save' });
    // });
  }

  ngOnInit() {
    console.log(this.data);
    this.dataSource = new MatTableDataSource<any>(this.data.items);
  }

  onNoClick(): void {
    this.dialogRef.close({ event: 'save' });
  }

  clickConfirm(): void {}

  clickCancel(): void {
    this.dialogRef.close({ event: 'close' });
  }

  chooseDataSearchDetail(element) {
    const url =
      './detail-fi-kb?compCode=' +
      element.compCode +
      '&docNo=' +
      element.accDocNo +
      '&docYear=' +
      element.fiscalYear;
    window.open(url, 'name', 'width=1200,height=700');
  }
}
