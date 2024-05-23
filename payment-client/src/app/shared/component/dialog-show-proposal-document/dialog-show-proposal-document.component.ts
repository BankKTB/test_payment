import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { MatTableDataSource } from '@angular/material/table';

import { Constant } from '@shared/utils/constant';
import { PaymentService } from '@core/services/payment/payment.service';
import { SelectionModel } from '@angular/cdk/collections';
import { MatSort } from '@angular/material/sort';
import { MatPaginator } from '@angular/material/paginator';
import { LoaderService, LocalStorageService } from '@core/services';
import { WebInfo } from '@core/models/web-info';

export interface DialogData {
  page: any;

  headerTable: [];
  paymentAlias: any;
}

@Component({
  selector: 'app-dialog-show-proposal-document',
  templateUrl: './dialog-show-proposal-document.component.html',
  styleUrls: ['./dialog-show-proposal-document.component.scss'],
})
export class DialogShowProposalDocumentComponent implements OnInit {
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;

  selectionApprove = new SelectionModel<any>(true, []);

  dataSource = new MatTableDataSource([]);

  displayedColumns: string[] = [
    'proposalBlock',
    'invoiceDocumentNo',
    'invoiceAmountPaid',
    'companyCode',
    'currency',
    'payingBankCode',
    'payingHouseBank',
    'paymentMethod',
    'vendorCode',
    'status',
  ];

  listMessageResponse = [];

  listValidate = [];

  constructor(
    public dialogRef: MatDialogRef<DialogShowProposalDocumentComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    public constant: Constant,
    private router: Router,
    private dialog: MatDialog,
    private paymentService: PaymentService,
    private loaderService: LoaderService,
    private localStorageService: LocalStorageService
  ) {
    this.dialogRef.disableClose = true;
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
    // console.log(this.paginator)
    // console.log(this.sort)

    // this.allPage = this.data.page;

    this.searchProposalDocument(this.data.paymentAlias);
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

  searchProposalDocument(paymentAlias) {
    console.log(paymentAlias);
    this.paymentService
      .searchProposalDocument(paymentAlias.id, this.localStorageService.getWebInfo())
      .then((data) => {
        const response = data as any;
        const result = response.data;
        console.log(result);
        if (result) {
          this.dataSource = new MatTableDataSource(result);
          this.dataSource.data.forEach((item) => {
            if (item.proposalBlock) {
              this.selectionApprove.select(item);
            }
          });
          this.dataSource.sort = this.sort;
          this.dataSource.paginator = this.paginator;
        } else {
          this.listMessageResponse.push('ไม่พบเอกสาร');
        }
      });
  }

  selectAllApproved() {
    this.dataSource.data.forEach((item) => {
      this.selectionApprove.select(item);
    });
  }

  unselectAllApproved() {
    this.dataSource.data.forEach((item) => {
      if (!item.proposalBlock) {
        this.selectionApprove.deselect(item);
      }
    });
  }

  onNoClick(): void {
    this.dialogRef.close({});
  }

  checkboxLabelApproved(row?: any): string {
    return `${this.selectionApprove.isSelected(row) ? 'deselect' : 'select'} row ${
      row.position + 1
    }`;
  }

  onClickCheckedDocument(event, type, row) {
    event.stopPropagation();
    // this.reasonInput.toArray()[row.no - 1].nativeElement.value = '';
    // console.log(this.selectionApprove.selected);
    // if (type === 'approve') {
    //   this.selectionNotApprove.deselect(row);
    // } else {
    //   this.selectionApprove.deselect(row);
    // }
  }

  onSave() {
    const listItem = [];
    this.listValidate = [];
    this.dataSource.data.forEach((item) => {
      if (this.selectionApprove.isSelected(item)) {
        item.approve = true;
        if (!item.proposalBlock) {
          listItem.push(item);
        }
      } else {
        item.approve = false;
      }
    });
    const webInfo: WebInfo = this.localStorageService.getWebInfo();

    if (listItem.length > 0) {
      const request = {
        proposalDocumentList: listItem,
        webInfo,
      };

      this.loaderService.loadingToggleStatus(true);
      this.paymentService.blockDocument(request).then((result) => {
        this.loaderService.loadingToggleStatus(false);
        this.searchProposalDocument(this.data.paymentAlias);
      });
    } else {
      this.listValidate.push('กรุณาเลือกรายการ');
    }
  }
}
